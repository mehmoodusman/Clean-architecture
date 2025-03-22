package com.mehmood.memorynotes.presentation


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.mehmmod.memorynotes.databinding.FragmentListBinding

import com.mehmood.memorynotes.framework.ListViewModel
import androidx.navigation.findNavController

class ListFragment : Fragment(), ListAction {

    private val notesListAdapter = NotesListAdapter(arrayListOf(), this)
    private lateinit var viewModel: ListViewModel
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       binding.notesListView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = notesListAdapter
        }

        binding.addNote.setOnClickListener { goToNoteDetails() }
         viewModel = ViewModelProvider(this)[ListViewModel::class.java]


        observeViewModel()
    }

    fun observeViewModel() {
        viewModel.notes.observe(viewLifecycleOwner, Observer {notesList ->
            binding.loadingView.visibility = View.GONE
            binding.notesListView.visibility = View.VISIBLE
            notesListAdapter.updateNotes(notesList.sortedByDescending { it.updateTime })
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getNotes()
    }

    private fun goToNoteDetails(id: Long = 0L) {

        val action = ListFragmentDirections.actionGoToNote(id)
        binding.notesListView.findNavController().navigate(action)
    }

    override fun onClick(id: Long) {
        goToNoteDetails(id)
    }

}
