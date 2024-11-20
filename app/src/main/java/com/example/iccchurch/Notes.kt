package com.example.iccchurch

import NotesStorage
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.iccchurch.databinding.FragmentNotesBinding

class Notes : Fragment() {
    private lateinit var notesStorage: NotesStorage
    private lateinit var notesAdapter: NotesAdapter
    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notesStorage = NotesStorage(requireContext())

        // Set up RecyclerView with click handlers
        notesAdapter = NotesAdapter(
            onItemClick = { note -> openNoteInAddNotesActivity(note) },
            onDeleteClick = { note -> confirmDeleteNote(note) }
        )
        binding.notesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.notesRecyclerView.adapter = notesAdapter

        loadNotes()

        binding.addNoteButton.setOnClickListener {
            val intent = Intent(requireContext(), AddNotes::class.java)
            startActivity(intent)
        }
    }

    private fun loadNotes() {
        val notes = notesStorage.getNotes()
        notesAdapter.submitList(notes)
    }

    private fun openNoteInAddNotesActivity(note: Note) {
        val intent = Intent(requireContext(), AddNotes::class.java).apply {
            putExtra("note_title", note.title)
            putExtra("note_content", note.content)
        }
        startActivity(intent)
    }

    private fun confirmDeleteNote(note: Note) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Note")
            .setMessage("Are you sure you want to delete this note?")
            .setPositiveButton("Yes") { dialog, _ ->
                deleteNoteAndRefresh(note)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun deleteNoteAndRefresh(note: Note) {
        val updatedNotes = notesStorage.getNotes().toMutableList().apply {
            remove(note)
        }
        notesStorage.saveNotes(updatedNotes)
        loadNotes()  // Refresh the list
        Toast.makeText(requireContext(), "Note deleted.", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        loadNotes()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
