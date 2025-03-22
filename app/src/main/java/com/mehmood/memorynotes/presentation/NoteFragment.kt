package com.mehmood.memorynotes.presentation

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuProvider
import androidx.lifecycle.ViewModelProvider
import com.mehmmod.memorynotes.R
import com.mehmood.core.data.Note
import com.mehmood.memorynotes.framework.NoteViewModel
import androidx.navigation.findNavController
import com.mehmmod.memorynotes.databinding.FragmentNoteBinding

class NoteFragment : Fragment() {

    private var noteId = 0L
    private lateinit var viewModel: NoteViewModel
    private var currentNote = Note("", "", 0L, 0L)
    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]

        arguments?.let {
            noteId = NoteFragmentArgs.fromBundle(it).noteId
        }

        initializeMenuOptions()
        observeViewModel() // Now we wait for data before calling getNote

        binding.checkButton.setOnClickListener {
            if (binding.titleView.text.toString().isNotEmpty() || binding.contentView.text.toString().isNotEmpty()) {
                val time = System.currentTimeMillis()
                currentNote.title = binding.titleView.text.toString()
                currentNote.content = binding.contentView.text.toString()
                currentNote.updateTime = time
                if (currentNote.id == 0L) {
                    currentNote.creationTime = time
                }
                viewModel.saveNote(currentNote)
            } else {
                it.findNavController().popBackStack()
            }
        }

    }

    private fun observeViewModel() {
        viewModel.saved.observe(viewLifecycleOwner) { isSaved ->
            if (isSaved) {
                Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show()
                hideKeyboard()
                binding.titleView.findNavController().popBackStack()
            } else {
                Toast.makeText(context, "Something went wrong, please try again", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.currentNote.observe(viewLifecycleOwner) { note ->
            note?.let {
                currentNote = it
                binding.titleView.setText(it.title, TextView.BufferType.EDITABLE)
                binding.contentView.setText(it.content, TextView.BufferType.EDITABLE)
            }
        }

        // 🔥 Ensure we call getNote AFTER observer is set
        if (noteId != 0L) {
            viewModel.getNote(noteId)
        }
    }

    private fun initializeMenuOptions(){

        // Add menu provider safely
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.note_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.deleteNote -> {
                        if (context != null && noteId != 0L) {
                            AlertDialog.Builder(requireContext())
                                .setTitle("Delete note")
                                .setMessage("Are you sure you want to delete this note?")
                                .setPositiveButton("Yes") { _, _ ->
                                    viewModel.deleteNote(currentNote)
                                }
                                .setNegativeButton("Cancel", null)
                                .create()
                                .show()
                        }
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner)
    }
    private fun hideKeyboard() {
        val imm = context?.getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(binding.titleView.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 🔥 Prevent memory leaks
    }
}
