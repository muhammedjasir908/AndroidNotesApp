package com.muhammedjasir.androidnotesapp.presentation.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.muhammedjasir.androidnotesapp.R
import com.muhammedjasir.androidnotesapp.databinding.FragmentHomeBinding
import com.muhammedjasir.androidnotesapp.presentation.create_notes.CreateNoteFragment
import com.muhammedjasir.androidnotesapp.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)

    private lateinit var notesAdapter: NoteAdapter

    private val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRv()
        collectNotes()

        binding.imageAddNoteBtn.setOnClickListener {
            replaceFragment(CreateNoteFragment.newInstance(),true)
        }

        binding.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.onSearchQueryChanged(newText.toString())
                return true
            }
            })
    }

    private val onClicked = object : NoteAdapter.OnItemClickListener {
        override fun onClicked(notesId: Int) {
            val fragment: Fragment
            val bundle = Bundle()
            bundle.putInt(getString(R.string.noteId),notesId)
            fragment = CreateNoteFragment.newInstance()
            fragment.arguments = bundle

            replaceFragment(fragment,true)
        }
    }

    private fun collectNotes() = viewLifecycleOwner.lifecycleScope.launch {
        viewModel.notes.collectLatest {
            notesAdapter.submitList(it)
        }
    }

    private fun setUpRv() = binding.apply {
        notesAdapter = NoteAdapter().apply { setOnClickListener(onClicked) }
        notesRecyclerview.setHasFixedSize(true)
        notesRecyclerview.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        notesRecyclerview.adapter = notesAdapter
    }

    fun replaceFragment(fragment: Fragment,isTransition: Boolean){
        val fragmentTransition = requireActivity().supportFragmentManager.beginTransaction()

        if(isTransition){
            fragmentTransition.setCustomAnimations(
                android.R.anim.slide_out_right,
                android.R.anim.slide_in_left
            )
        }

        fragmentTransition.replace(R.id.flFragment,fragment)
            .addToBackStack(fragment.javaClass.simpleName)
        fragmentTransition.commit()
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {  }
            }
    }
}