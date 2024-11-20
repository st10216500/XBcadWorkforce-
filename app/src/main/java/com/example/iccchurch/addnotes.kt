package com.example.iccchurch

import NotesStorage
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.iccchurch.databinding.ActivityAddnotesBinding

class AddNotes : AppCompatActivity() {
    private lateinit var notesStorage: NotesStorage
    private lateinit var binding: ActivityAddnotesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddnotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        notesStorage = NotesStorage(this)

        // Get intent data if available
        val title = intent.getStringExtra("note_title")
        val content = intent.getStringExtra("note_content")

        if (title != null && content != null) {
            binding.titleTextArea.setText(title)
            binding.notesTextArea.setText(content)
        }

        binding.doneButton.setOnClickListener {
            saveNote()
        }

        binding.backButton.setOnClickListener {
            showBackConfirmationDialog()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showBackConfirmationDialog()
            }
        })
    }

    private fun saveNote() {
        val title = binding.titleTextArea.text.toString()
        val content = binding.notesTextArea.text.toString()

        if (title.isBlank() || content.isBlank()) {
            Toast.makeText(this, "Please fill in both title and notes.", Toast.LENGTH_SHORT).show()
            return
        }

        val notes = notesStorage.getNotes().toMutableList().apply {
            // Replace an existing note with the same title (for simplicity)
            removeIf { it.title == title }
            add(Note(title, content))
        }
        notesStorage.saveNotes(notes)

        Toast.makeText(this, "Note saved.", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun showBackConfirmationDialog() {
        AlertDialog.Builder(this)
            .setMessage("Are you sure you want to go back? Unsaved changes will be lost.")
            .setPositiveButton("Yes") { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}
