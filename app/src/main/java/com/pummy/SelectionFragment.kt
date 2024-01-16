package com.pummy

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController

class SelectionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_selection, container, false)

        val breedIdentificationButton = rootView.findViewById<ImageView>(R.id.breedIdentification_btn)
        breedIdentificationButton.setOnClickListener {
            // Start the BreedIdentificationActivity
            val intent = Intent(requireContext(), BreedIdentificationActivity::class.java)
            startActivity(intent)
        }

        val dogBreedsButton = rootView.findViewById<ImageView>(R.id.dogBreads_btn)
        dogBreedsButton.setOnClickListener {
            // Navigate to BreedIdentificationFragment
            val intent = Intent(requireContext(), BreedListActivity::class.java)
            startActivity(intent)
        }

        return rootView
    }
}
