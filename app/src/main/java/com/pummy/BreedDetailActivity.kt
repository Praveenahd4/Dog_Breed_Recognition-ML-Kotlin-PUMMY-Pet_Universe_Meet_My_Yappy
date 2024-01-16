package com.pummy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.pummy.databinding.ActivityBreedDetailBinding

class BreedDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBreedDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBreedDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the breed information from the intent
        val breed = intent.getParcelableExtra<Breed>("breed")

        // Set the title of the toolbar to the selected breed name
        val toolbar: Toolbar = findViewById(R.id.toolbar_breedDetail)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = breed?.name ?: "Breed Detail" // Default title if breed is null
            setDisplayHomeAsUpEnabled(true) // Enable the back button
        }

        binding.toolbarBreedDetail.setNavigationOnClickListener {
            onBackPressed()
        }

        val breedImageView: ImageView = findViewById(R.id.breed_image)
        val breedNameTextView: TextView = findViewById(R.id.breed_name)
        val lifespanTextView: TextView = findViewById(R.id.breed_lifespan)
        val descriptionTextView: TextView = findViewById(R.id.description)
        val temperamentTextView: TextView = findViewById(R.id.temperament)
        val healthConsiderationsTextView: TextView = findViewById(R.id.health_considerations)
        val sizeAndWeightTextView: TextView = findViewById(R.id.size_and_weight)
        val historyAndOriginTextView: TextView = findViewById(R.id.history_and_origin)
        val specialRequirementsTextView: TextView = findViewById(R.id.special_requirements)
        val popularityTextView: TextView = findViewById(R.id.popularity)
        val notableCharacteristicsTextView: TextView = findViewById(R.id.notable_characteristics)
        val exerciseNeedsTextView: TextView = findViewById(R.id.exercise_needs)
        val groomingNeedsTextView: TextView = findViewById(R.id.grooming_needs)
        val activityLevelTextView: TextView = findViewById(R.id.activity_level)


        // Set the breed image using Glide
        breed?.imageResourceId?.let {
            Glide.with(this)
                .load(getImageResourceIdByName(it))
                .into(breedImageView)
        }
        // Set attributes with their names and values
        breedNameTextView.text = breed?.name
        lifespanTextView.text = Html.fromHtml("<b>Lifespan:</b> ${breed?.lifespan}")
        descriptionTextView.text = Html.fromHtml("<b>Description:</b> ${breed?.description}")
        temperamentTextView.text = Html.fromHtml("<b>Temperament:</b> ${breed?.temperament}")
        healthConsiderationsTextView.text = Html.fromHtml("<b>Health Considerations:</b> ${breed?.healthConsiderations}")
        sizeAndWeightTextView.text = Html.fromHtml(" ${breed?.sizeAndWeight}")
        historyAndOriginTextView.text = Html.fromHtml("<b>History and Origin:</b> ${breed?.historyAndOrigin}")
        specialRequirementsTextView.text = Html.fromHtml("<b>Special Requirements:</b> ${breed?.specialRequirements}")
        popularityTextView.text = Html.fromHtml("<b>Popularity:</b> ${breed?.popularity}")
        notableCharacteristicsTextView.text = Html.fromHtml("<b>Notable Characteristics:</b> ${breed?.notableCharacteristics}")
        exerciseNeedsTextView.text = Html.fromHtml("<b>Exercise Needs:</b> ${breed?.exerciseNeeds}")
        groomingNeedsTextView.text = Html.fromHtml("<b>Grooming Needs:</b> ${breed?.groomingNeeds}")
        activityLevelTextView.text = Html.fromHtml("<b>Activity Level:</b> ${breed?.activityLevel}")

        // Set up back navigation
        binding.toolbarBreedDetail.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun getImageResourceIdByName(resourceName: String): Int {
        return resources.getIdentifier(resourceName, "drawable", packageName)
    }
}
