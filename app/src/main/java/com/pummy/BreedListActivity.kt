package com.pummy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pummy.databinding.ActivityBreedListBinding
import java.io.IOException

class BreedListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: ActivityBreedListBinding
    private lateinit var adapter: BreedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBreedListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the toolbar
        setSupportActionBar(binding.toolbarBreedList)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Dog Breeds List"
        }
        binding.toolbarBreedList.setNavigationOnClickListener {
            onBackPressed()
        }

        recyclerView = binding.breedRecyclerView
        val breeds = loadBreedDataFromJson()

        adapter = BreedAdapter(breeds) { breed ->
            // Create an intent to launch a new activity to display breed details
            val intent = Intent(this, BreedDetailActivity::class.java)
            intent.putExtra("breed", breed)
            startActivity(intent)
        }
        binding.breedRecyclerView.adapter = adapter
        binding.breedRecyclerView.layoutManager = LinearLayoutManager(this)


    }
    private fun loadBreedDataFromJson(): List<Breed> {
        val jsonFileName = "breed_data.json"
        val json: String?
        try {
            val inputStream = assets.open(jsonFileName)
            json = inputStream.bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            e.printStackTrace()
            return emptyList()
        }

        val gson = Gson()
        val breedListType = object : TypeToken<List<Breed>>() {}.type
        return gson.fromJson(json, breedListType)
    }
}
