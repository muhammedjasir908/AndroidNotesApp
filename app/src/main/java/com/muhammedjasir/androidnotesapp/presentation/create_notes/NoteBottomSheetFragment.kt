package com.muhammedjasir.androidnotesapp.presentation.create_notes

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.muhammedjasir.androidnotesapp.R
import com.muhammedjasir.androidnotesapp.databinding.FragmentNotesBottomSheetBinding
import com.muhammedjasir.androidnotesapp.util.extensions.makeGone
import com.muhammedjasir.androidnotesapp.util.extensions.makeVisible

class NoteBottomSheetFragment: BottomSheetDialogFragment() {

    private var _binding: FragmentNotesBottomSheetBinding? = null
    private val binding get() = _binding!!
    private var selectedColor = "#3e434e"

    companion object {

        var noteId: Int? = null

        const val DRAGGING = "DRAGGING"
        const val SETTLING = "SETTLING"
        const val EXPANDED = "EXPANDED"
        const val COLLAPSED = "COLLAPSED"
        const val HIDDEN = "HIDDEN"

        fun newInstance(id: Int?): NoteBottomSheetFragment {
            val args = Bundle()
            val fragment = NoteBottomSheetFragment()
            fragment.arguments = args
            noteId = id
            return fragment
        }
    }

    @SuppressLint("RestrictedApi", "InflateParams")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)

        val view = LayoutInflater.from(context).inflate(R.layout.fragment_notes_bottom_sheet, null)
        dialog.setContentView(view)

        val param = (view.parent as View).layoutParams as CoordinatorLayout.LayoutParams

        val behavior = param.behavior

        if(behavior is BottomSheetBehavior<*>){
            behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    BottomSheetBehavior.STATE_DRAGGING.apply {  }
                    BottomSheetBehavior.STATE_SETTLING.apply {  }
                    BottomSheetBehavior.STATE_EXPANDED.apply {  }
                    BottomSheetBehavior.STATE_COLLAPSED.apply {  }
                    BottomSheetBehavior.STATE_HIDDEN.apply {
                        dismiss()
                        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                }

                override fun onSlide(p0: View, p1: Float) { }
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBottomSheetBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(noteId != null){
            binding.layoutDeleteNote.makeVisible()
        }else{
            binding.layoutDeleteNote.makeGone()
        }
        setListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setListener() {
        binding.apply {
            fNoteBlue.setOnClickListener {
                imageNoteBlue.setImageResource(R.drawable.round_done_24)
                imageNoteCyan.setImageResource(0)
                imageNoteBrown.setImageResource(0)
                imageNoteGreen.setImageResource(0)
                imageNoteIndigo.setImageResource(0)
                imageNoteOrange.setImageResource(0)
                imageNotePurple.setImageResource(0)
                imageNoteRed.setImageResource(0)
                imageNoteYellow.setImageResource(0)
                selectedColor = "#2196f3"

                val intent = Intent(getString(R.string.bottom_sheet_action))
                intent.putExtra(getString(R.string.action),getString(R.string.blue))
                intent.putExtra(getString(R.string.selected_color),selectedColor)
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }

            fNoteCyan.setOnClickListener {
                imageNoteBlue.setImageResource(0)
                imageNoteCyan.setImageResource(R.drawable.round_done_24)
                imageNoteBrown.setImageResource(0)
                imageNoteGreen.setImageResource(0)
                imageNoteIndigo.setImageResource(0)
                imageNoteOrange.setImageResource(0)
                imageNotePurple.setImageResource(0)
                imageNoteRed.setImageResource(0)
                imageNoteYellow.setImageResource(0)
                selectedColor = "#00e5ff"

                val intent = Intent(getString(R.string.bottom_sheet_action))
                intent.putExtra(getString(R.string.action),getString(R.string.cyan))
                intent.putExtra(getString(R.string.selected_color),selectedColor)
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }

            fNoteBrown.setOnClickListener {
                imageNoteBlue.setImageResource(0)
                imageNoteCyan.setImageResource(0)
                imageNoteBrown.setImageResource(R.drawable.round_done_24)
                imageNoteGreen.setImageResource(0)
                imageNoteIndigo.setImageResource(0)
                imageNoteOrange.setImageResource(0)
                imageNotePurple.setImageResource(0)
                imageNoteRed.setImageResource(0)
                imageNoteYellow.setImageResource(0)
                selectedColor = "#3e2723"

                val intent = Intent(getString(R.string.bottom_sheet_action))
                intent.putExtra(getString(R.string.action),getString(R.string.brown))
                intent.putExtra(getString(R.string.selected_color),selectedColor)
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }

            fNoteGreen.setOnClickListener {
                imageNoteBlue.setImageResource(0)
                imageNoteCyan.setImageResource(0)
                imageNoteBrown.setImageResource(0)
                imageNoteGreen.setImageResource(R.drawable.round_done_24)
                imageNoteIndigo.setImageResource(0)
                imageNoteOrange.setImageResource(0)
                imageNotePurple.setImageResource(0)
                imageNoteRed.setImageResource(0)
                imageNoteYellow.setImageResource(0)
                selectedColor = "#00c853"

                val intent = Intent(getString(R.string.bottom_sheet_action))
                intent.putExtra(getString(R.string.action),getString(R.string.green))
                intent.putExtra(getString(R.string.selected_color),selectedColor)
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }

            fNoteIndigo.setOnClickListener {
                imageNoteBlue.setImageResource(0)
                imageNoteCyan.setImageResource(0)
                imageNoteBrown.setImageResource(0)
                imageNoteGreen.setImageResource(0)
                imageNoteIndigo.setImageResource(R.drawable.round_done_24)
                imageNoteOrange.setImageResource(0)
                imageNotePurple.setImageResource(0)
                imageNoteRed.setImageResource(0)
                imageNoteYellow.setImageResource(0)
                selectedColor = "#1a237e"

                val intent = Intent(getString(R.string.bottom_sheet_action))
                intent.putExtra(getString(R.string.action),getString(R.string.indigo))
                intent.putExtra(getString(R.string.selected_color),selectedColor)
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }

            fNoteOrange.setOnClickListener {
                imageNoteBlue.setImageResource(0)
                imageNoteCyan.setImageResource(0)
                imageNoteBrown.setImageResource(0)
                imageNoteGreen.setImageResource(0)
                imageNoteIndigo.setImageResource(0)
                imageNoteOrange.setImageResource(R.drawable.round_done_24)
                imageNotePurple.setImageResource(0)
                imageNoteRed.setImageResource(0)
                imageNoteYellow.setImageResource(0)
                selectedColor = "#ff6d00"

                val intent = Intent(getString(R.string.bottom_sheet_action))
                intent.putExtra(getString(R.string.action),getString(R.string.orange))
                intent.putExtra(getString(R.string.selected_color),selectedColor)
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }

            fNotePurple.setOnClickListener {
                imageNoteBlue.setImageResource(0)
                imageNoteCyan.setImageResource(0)
                imageNoteBrown.setImageResource(0)
                imageNoteGreen.setImageResource(0)
                imageNoteIndigo.setImageResource(0)
                imageNoteOrange.setImageResource(0)
                imageNotePurple.setImageResource(R.drawable.round_done_24)
                imageNoteRed.setImageResource(0)
                imageNoteYellow.setImageResource(0)
                selectedColor = "#aa00ff"

                val intent = Intent(getString(R.string.bottom_sheet_action))
                intent.putExtra(getString(R.string.action),getString(R.string.purple))
                intent.putExtra(getString(R.string.selected_color),selectedColor)
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }

            fNoteRed.setOnClickListener {
                imageNoteBlue.setImageResource(0)
                imageNoteCyan.setImageResource(0)
                imageNoteBrown.setImageResource(0)
                imageNoteGreen.setImageResource(0)
                imageNoteIndigo.setImageResource(0)
                imageNoteOrange.setImageResource(0)
                imageNotePurple.setImageResource(0)
                imageNoteRed.setImageResource(R.drawable.round_done_24)
                imageNoteYellow.setImageResource(0)
                selectedColor = "#d50000"

                val intent = Intent(getString(R.string.bottom_sheet_action))
                intent.putExtra(getString(R.string.action),getString(R.string.red))
                intent.putExtra(getString(R.string.selected_color),selectedColor)
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }

            fNoteYellow.setOnClickListener {
                imageNoteBlue.setImageResource(0)
                imageNoteCyan.setImageResource(0)
                imageNoteBrown.setImageResource(0)
                imageNoteGreen.setImageResource(0)
                imageNoteIndigo.setImageResource(0)
                imageNoteOrange.setImageResource(0)
                imageNotePurple.setImageResource(0)
                imageNoteRed.setImageResource(0)
                imageNoteYellow.setImageResource(R.drawable.round_done_24)
                selectedColor = "#ffeb3b"

                val intent = Intent(getString(R.string.bottom_sheet_action))
                intent.putExtra(getString(R.string.action),getString(R.string.yellow))
                intent.putExtra(getString(R.string.selected_color),selectedColor)
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }

            //Add Image
            layoutImage.setOnClickListener {
                val intent = Intent(getString(R.string.bottom_sheet_action))
                intent.putExtra(getString(R.string.action),getString(R.string.image))
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
                dismiss()
            }

            //Add Url
            layoutWebUrl.setOnClickListener {
                val intent = Intent(getString(R.string.bottom_sheet_action))
                intent.putExtra(getString(R.string.action),getString(R.string.webUrl))
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
                dismiss()
            }

            //Delete Notes
            layoutDeleteNote.setOnClickListener {
                val intent = Intent(getString(R.string.bottom_sheet_action))
                intent.putExtra(getString(R.string.action),getString(R.string.deleteNote))
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
                dismiss()
            }
        }
    }
}