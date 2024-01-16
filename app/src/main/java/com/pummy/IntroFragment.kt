package com.pummy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.pummy.R

class IntroFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_intro, container, false)

        val introButton = rootView.findViewById<Button>(R.id.explore_breeds)

        introButton.setOnClickListener {
            // Navigate to SelectionFragment
            //val action = IntroFragmentDirections.actionIntroFragmentToSelectionFragment()
//            findNavController().navigate(action)
            findNavController().navigate(R.id.action_introFragment_to_selectionFragment)
        }

        return rootView
    }
}
