package com.example.iccchurch

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Button
import android.widget.TextView // If you are also using TextView



// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Annoucement.newInstance] factory method to
 * create an instance of this fragment.
 */
class Annoucement : Fragment() {

    private lateinit var leadershipCodeEditText: EditText
    private lateinit var submitCodeButton: Button
    private lateinit var eventNameLabel: TextView
    private lateinit var eventNameEditText: EditText
    private lateinit var descriptionLabel: TextView
    private lateinit var descriptionEditText: EditText
    private lateinit var submitButton: Button

    private val leadershipCode = "12345" // Define the leadership code here

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    // Fragment Lifecycle: onCreateView - Set up the UI and logic
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_annoucement, container, false)

        // Initialize UI Elements
        leadershipCodeEditText = view.findViewById(R.id.leadershipCodeEditText)
        submitCodeButton = view.findViewById(R.id.submitCodeButton)
        eventNameLabel = view.findViewById(R.id.eventNameLabel)
        eventNameEditText = view.findViewById(R.id.eventNameEditText)
        descriptionLabel = view.findViewById(R.id.descriptionLabel)
        descriptionEditText = view.findViewById(R.id.descriptionEditText)
        submitButton = view.findViewById(R.id.submitButton)

        // Code Validation and View Toggling
        submitCodeButton.setOnClickListener {
            val enteredCode = leadershipCodeEditText.text.toString()
            if (enteredCode == leadershipCode) {
                toggleEventDetailsVisibility(View.VISIBLE) // Show event details if code is correct
            } else {
                Toast.makeText(context, "Invalid Code", Toast.LENGTH_SHORT).show() // Show error
            }
        }

        // Handle Submit Action
        submitButton.setOnClickListener {
            val eventName = eventNameEditText.text.toString()
            val description = descriptionEditText.text.toString()
            // Send data to Home page via SharedPreferences or ViewModel
            // Here you could add code to update the RecyclerView in Home page
            Toast.makeText(context, "Event Submitted: $eventName", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    // Toggle Visibility of Event Details Section
    private fun toggleEventDetailsVisibility(visibility: Int) {
        eventNameLabel.visibility = visibility
        eventNameEditText.visibility = visibility
        descriptionLabel.visibility = visibility
        descriptionEditText.visibility = visibility
        submitButton.visibility = visibility
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Annoucement.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Annoucement().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}