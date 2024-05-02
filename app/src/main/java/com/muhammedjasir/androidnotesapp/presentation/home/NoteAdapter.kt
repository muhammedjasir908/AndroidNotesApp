package com.muhammedjasir.androidnotesapp.presentation.home

import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.muhammedjasir.androidnotesapp.data.local.model.Note
import com.muhammedjasir.androidnotesapp.databinding.ItemRvNotesBinding

class NoteAdapter : ListAdapter<Note,NoteAdapter.NotesViewHolder>(diffCallback) {

    private lateinit var listener: OnItemClickListener

    inner class NotesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemRvNotesBinding.bind(view)

        fun bind(note: Note){
            with(binding){
                tvTitle.text = note.noteTitle
                tvDesc.text = note.noteText
                tvDateTime.text = note.dateTime

                note.color?.let {
                    cardView.setCardBackgroundColor(Color.parseColor(note.color))
                }

                if(note.imagePath.isNullOrEmpty().not()){
                    imageNote.setImageBitmap(BitmapFactory.decodeFile(note.imagePath))
                    imageNote.visibility = View.VISIBLE
                }else{
                    imageNote.visibility = View.GONE
                }

                if(note.noteLink.isNullOrEmpty().not()){
                    tvWebLink.text = note.noteLink
                    tvWebLink.visibility = View.VISIBLE
                }else{
                    tvWebLink.visibility = View.GONE
                }

                cardView.setOnClickListener {
                    listener.onClicked(note.id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val binding = ItemRvNotesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NotesViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setOnClickListener(listener1: OnItemClickListener){
        listener = listener1
    }

    interface OnItemClickListener{
        fun onClicked(notesId: Int)
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem == newItem
            }
        }
    }
}