package com.pummy

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.pummy.databinding.ActivityBreedIdentificationBinding
import com.pummy.ml.EfficientnetModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.ResizeOp.ResizeMethod
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class BreedIdentificationActivity : AppCompatActivity() {


    private lateinit var galleryImg: Button
    private lateinit var cameraImg: Button
    private lateinit var predBtn: Button
    private lateinit var resultView: TextView
    private lateinit var otherResultView: TextView
    private lateinit var imageView: ImageView
    private lateinit var bitmap: Bitmap
    private lateinit var model: EfficientnetModel

    private lateinit var predictionsChart: HorizontalBarChart
    private lateinit var binding: ActivityBreedIdentificationBinding

    private val CAMERA_REQUEST_CODE = 1
    private val GALLERY_REQUEST_CODE = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBreedIdentificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the toolbar
        setSupportActionBar(binding.toolbarBreedIdentification)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Dog Breed Identification"
        }


        galleryImg = binding.addImageBtn
        cameraImg = binding.takeImageBtn
        predBtn = binding.btnPredict
        resultView = binding.textPrediction
        otherResultView = binding.otherPredictions
        imageView = binding.imagePalce
        predictionsChart = binding.barChart

        model = EfficientnetModel.newInstance(this)

        var labels = application.assets.open("labels.txt").bufferedReader().readLines()


        binding.toolbarBreedIdentification.setNavigationOnClickListener {
            onBackPressed()
        }
        cameraImg.setOnClickListener {
            openCamera()
        }
        galleryImg.setOnClickListener {
            openGallery()
        }
        predBtn.setOnClickListener {
            val drawable = imageView.drawable
            bitmap = (drawable as BitmapDrawable).bitmap

            // Show loading indicator
            resultView.text = "Processing..."

            // Use Coroutines to offload heavy computation from the main thread
            lifecycleScope.launch {
                val tensorImage = processImageInBackground(bitmap)

                val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
                inputFeature0.loadBuffer(tensorImage.buffer)

                // Runs model inference and gets result.
                val outputs = model.process(inputFeature0)
                val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray

                // Create a list of index-score pairs for sorting
                val predictions = outputFeature0.mapIndexed { index, score -> index to score }
                    .sortedByDescending { it.second }
                    .take(5) // Take the top 5 predictions

                // Display the top prediction
                val topPrediction = predictions.first()
                if (topPrediction.second * 100 < 40) {
                    resultView.text = "\uD83D\uDC3E Hey there! \uD83D\uDC36 It looks like I'm having a bit of trouble with this image. The model isn't quite sure if there's a dog in it or if there might be multiple dogs hanging out. Sometimes, images can be tricky to interpret. If you'd like, you can try another picture, and I'll do my best to give correct predictions! \uD83D\uDC15\uD83D\uDCF8\n"
                    otherResultView.visibility = View.INVISIBLE
                    predictionsChart.visibility = View.INVISIBLE
                } else {
                    val topPredictionText =
                        "${labels[topPrediction.first]}: ${"%.2f".format(topPrediction.second * 100)}%"
                    resultView.text = "Top Prediction:\n$topPredictionText"

                    // Display the other predictions if the top prediction score is above 30%
                    val otherPredictions = predictions.drop(1).take(4)
                    val otherPredictionsText =
                        otherPredictions.joinToString("\n") { (index, score) ->
                            "${labels[index]}: ${"%.2f".format(score * 100)}%"
                        }
                    otherResultView.text = "Other Predictions:\n$otherPredictionsText"
                    otherResultView.visibility = View.VISIBLE
                    predictionsChart.visibility = View.VISIBLE
                }
                // Create a list of BarEntry for the top 5 predictions
                val entries = predictions.mapIndexed { i, (_, score) ->
                    BarEntry(i.toFloat(), score * 100)
                }
                val colors = entries.mapIndexed { i, _ ->
                    if (i == 0) {
                        Color.GREEN // Top prediction in green
                    } else {
                        Color.GRAY // Rest in gray
                    }
                }

                // Create a BarDataSet for the horizontal bar chart
                val dataSet = BarDataSet(entries, "Predictions")
                dataSet.colors = colors


                // Configure and set data for the horizontal bar chart
                val data = BarData(dataSet)
                predictionsChart.data = data
                predictionsChart.setFitBars(true)
                predictionsChart.description = null

                // Customize the x-axis labels with predicted label names
                val xAxis = predictionsChart.xAxis
                xAxis.valueFormatter = IndexAxisValueFormatter(predictions.map { (index, _) ->
                    labels[index]
                })
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                xAxis.granularity = 1f

                // Customize the data labels on top of bars
                data.setValueFormatter(PercentageValueFormatter())
                data.setValueTextSize(12f)

                predictionsChart.invalidate()

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    if (data != null) {
                        val bitmap = data.extras?.get("data") as Bitmap
                        imageView.setImageBitmap(bitmap)
                        Toast.makeText(this, "Image captured", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show()
                    }
                }

                GALLERY_REQUEST_CODE -> {
                    val uri = data?.data
                    if (uri != null) {
                        imageView.setImageURI(uri)
                        Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to select image", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    // Camera intent
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    // Gallery intent
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }


    class PercentageValueFormatter : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return String.format("%.2f%%", value)
        }
    }


    private suspend fun processImageInBackground(bitmap: Bitmap): TensorImage {
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeMethod.BILINEAR))
            .add(NormalizeOp(0f, 255f))
            .build()

        return withContext(Dispatchers.Default) {
            var tensorImage = TensorImage(DataType.FLOAT32)
            tensorImage.load(bitmap)
            imageProcessor.process(tensorImage)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release the model resources when the activity is destroyed
        model.close()
    }
}

