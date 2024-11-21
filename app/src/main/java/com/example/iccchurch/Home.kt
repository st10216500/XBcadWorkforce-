package com.example.iccchurch

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth

class Home : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var eventNameTextView: TextView
    private lateinit var descriptionTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Initialize Views
        eventNameTextView = view.findViewById(R.id.eventNameTextView)
        descriptionTextView = view.findViewById(R.id.descriptionTextView)
        val logoutButton: Button = view.findViewById(R.id.btnLogout)

        // Set up Logout Button
        logoutButton.setOnClickListener {
            showConfirmationDialog()
        }

        // Load announcement data
        loadAnnouncementData()

        return view
    }

    private fun loadAnnouncementData() {
        val sharedPreferences = requireContext().getSharedPreferences("AnnouncementPrefs", android.content.Context.MODE_PRIVATE)
        val eventName = sharedPreferences.getString("eventName", "No Event Name")
        val description = sharedPreferences.getString("description", "No Description")

        eventNameTextView.text = eventName
        descriptionTextView.text = description
    }

    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirmation")
        builder.setMessage("Are you sure you want to logout?")

        builder.setPositiveButton("Yes") { dialog, _ ->
            logoutUser()

        }
        builder.setNegativeButton("No") { dialog, _ ->

            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun logoutUser() {
        // Sign out user using FirebaseAuth
        auth.signOut()

        // Redirect to Login Activity
        val intent = Intent(requireContext(), Login::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
    }
}

