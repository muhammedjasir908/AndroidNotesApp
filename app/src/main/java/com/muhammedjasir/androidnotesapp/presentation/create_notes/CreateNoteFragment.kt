package com.muhammedjasir.androidnotesapp.presentation.create_notes

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.snackbar.Snackbar
import com.muhammedjasir.androidnotesapp.R
import com.muhammedjasir.androidnotesapp.data.local.model.Note
import com.muhammedjasir.androidnotesapp.databinding.FragmentCreateNoteBinding
import com.muhammedjasir.androidnotesapp.util.extensions.EMPTY_STRING
import com.muhammedjasir.androidnotesapp.util.extensions.makeGone
import com.muhammedjasir.androidnotesapp.util.extensions.makeVisible
import com.muhammedjasir.androidnotesapp.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class CreateNoteFragment :
    Fragment(R.layout.fragment_create_note)
{

        private val binding by viewBinding(FragmentCreateNoteBinding::bind)
        private val viewModel by viewModels<CreateNotesViewModel>()

    var selectedColor = "#3e434e"
    private var currentTime: String? = null

    private var READ_STORAGE_PERM = 123

    private var selectedImagePath = EMPTY_STRING
    private var selectedWebLink = EMPTY_STRING
    private var selectedAudioPath = EMPTY_STRING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireArguments().getInt(getString(R.string.noteId), -1).also {
            if(it != -1) viewModel.setNoteId(it)
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            requireActivity().requestPermissions(arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),PackageManager.PERMISSION_GRANTED)
        }else{
            requireActivity().requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),PackageManager.PERMISSION_GRANTED)
        }
    }

    companion object {
        const val NOTE_BOTTOM_SHEET_TAG = "Note Bottom Sheet Fragment"
        const val SELECTED_COLOR = "selectedColor"

        @JvmStatic
        fun newInstance() =
            CreateNoteFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view,savedInstanceState)
        initViews()
        collectNotes()
    }

    private fun collectNotes() = viewLifecycleOwner.lifecycleScope.launch {
        viewModel.note.collectLatest {
            it?.let (this@CreateNoteFragment::setNoteDataInUI)
        }
    }

    private fun setNoteDataInUI(note: Note) = binding.apply{
        viewSubtitleIndicator.setBackgroundColor(Color.parseColor(note.color))
        inputNoteTitle.setText(note.noteTitle)
        inputNoteSubtitle.setText(note.noteSubtitle)
        inputNote.setText(note.noteText)

        if(note.imagePath != EMPTY_STRING){
            selectedImagePath = note.imagePath.orEmpty()
            imageNote.setImageBitmap(BitmapFactory.decodeFile(note.imagePath))
            makeVisible(layoutImage,binding.imageNote,binding.imageNoteDelete)
        }else{
            makeGone(layoutImage,binding.imageNote,binding.imageNoteDelete)
        }

        if(note.noteLink != EMPTY_STRING){
            selectedWebLink = note.noteLink.orEmpty()
            tvWebLink.text = note.noteLink
            makeVisible(layoutWebUrl,noteWebLinkDelete)
            noteWebLink.setText(note.noteLink)
        }else{
            makeGone(layoutWebUrl,noteWebLinkDelete)
        }
    }

    private fun initViews() = binding.apply{

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            broadcastReceiver, IntentFilter("bottom_sheet_action")
        )

        viewSubtitleIndicator.setBackgroundColor(Color.parseColor(selectedColor))

        val sdf = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        currentTime = sdf.format(Date())

        textDateTime.text = currentTime

        imageSave.setOnClickListener{
            viewModel.note.value?.let { updateNote(it) } ?: saveNote()
        }

        imageBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        imageMore.setOnClickListener {
            val noteBottomSheetFragment = NoteBottomSheetFragment.newInstance(viewModel.noteId.value)
            noteBottomSheetFragment.show(
                requireActivity().supportFragmentManager,
                NOTE_BOTTOM_SHEET_TAG
            )
        }

        imageNoteDelete.setOnClickListener {
            selectedImagePath = EMPTY_STRING
            layoutImage.visibility = View.GONE
        }

        btnOk.setOnClickListener {
            if(noteWebLink.text.toString().trim().isEmpty()){
                checkWebUrl()
            }else{
                Toast.makeText(requireContext(),getString(R.string.url_require),Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener {
            if(viewModel.noteId.value != null){
                tvWebLink.makeVisible()
                layoutWebUrl.makeGone()
            }else{
                layoutWebUrl.makeGone()
            }
        }

        noteWebLinkDelete.setOnClickListener {
            selectedWebLink = EMPTY_STRING
            makeGone(tvWebLink,noteWebLinkDelete,layoutWebUrl)
        }

        tvWebLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(noteWebLink.text.toString()))
            startActivity(intent)
        }

    }

    private fun updateNote(note: Note) = viewLifecycleOwner.lifecycleScope.launch {
        note.apply {
            noteTitle = binding.inputNoteTitle.text.toString()
            noteSubtitle = binding.inputNoteSubtitle.text.toString()
            noteText = binding.inputNote.text.toString()
            dateTime = binding.textDateTime.text.toString()
            imagePath = selectedImagePath
            noteLink = selectedWebLink
        }.also {
            viewModel.updateNote(it)
        }
        binding.inputNoteTitle.setText(EMPTY_STRING)
        binding.inputNoteSubtitle.setText(EMPTY_STRING)
        binding.inputNote.setText(EMPTY_STRING)
        makeGone(
            with(binding){
                layoutImage
                imageNote
                tvWebLink
            }
        )
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun saveNote() {
       val etNoteTitle = view?.findViewById<EditText>(R.id.inputNoteTitle)
        val etNoteSubtitle = view?.findViewById<EditText>(R.id.inputNoteSubtitle)
        val etNoteDesc = view?.findViewById<EditText>(R.id.inputNote)

        when{
            etNoteTitle?.text.isNullOrEmpty() -> {
                Snackbar.make(requireView(),getString(R.string.title_require), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.ok)){ }.show()
            }

            etNoteDesc?.text.isNullOrEmpty() -> {
                Snackbar.make(requireView(),getString(R.string.empty_note_description_warning), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.ok)){ }.show()
            }
            else -> {
                viewLifecycleOwner.lifecycleScope.launch {
                    Note().apply{
                        noteTitle = etNoteTitle?.text.toString()
                        noteSubtitle = etNoteSubtitle?.text.toString()
                        noteText = etNoteDesc?.text.toString()
                        dateTime = currentTime
                        color = selectedColor
                        imagePath = selectedImagePath
                        noteLink = selectedWebLink
                    }.also {
                        viewModel.saveNote(it)
                    }
                    etNoteTitle?.setText(EMPTY_STRING)
                    etNoteSubtitle?.setText(EMPTY_STRING)
                    etNoteDesc?.setText(EMPTY_STRING)
                    makeGone(with(binding){
                        layoutImage
                        imageNote
                        tvWebLink
                    })
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
        }
    }

    private fun deleteNote() = viewLifecycleOwner.lifecycleScope.launch {
        viewModel.deleteNote()
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun checkWebUrl() {
        if(Patterns.WEB_URL.matcher(binding.noteWebLink.text.toString()).matches()){
            binding.layoutWebUrl.makeGone()
            binding.noteWebLink.isEnabled = false
            selectedWebLink = binding.noteWebLink.text.toString()
            binding.tvWebLink.makeVisible()
            binding.tvWebLink.text = binding.noteWebLink.text.toString()
        }else{
            Toast.makeText(requireContext(), getString(R.string.url_validation),Toast.LENGTH_SHORT).show()
        }
    }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if(intent == null || view == null)
                return

            val actionColor = intent.getStringExtra(getString(R.string.action))

            view?.let { fragmantView ->
                val binding = FragmentCreateNoteBinding.bind(fragmantView)

                when(actionColor){
                    getString(R.string.blue) -> {
                        selectedColor = intent.getStringExtra(SELECTED_COLOR) ?: ""
                        binding.viewSubtitleIndicator.setBackgroundColor(Color.parseColor(selectedColor))
                    }

                    getString(R.string.cyan) -> {
                        selectedColor = intent.getStringExtra(SELECTED_COLOR) ?: ""
                        binding.viewSubtitleIndicator.setBackgroundColor(Color.parseColor(selectedColor))
                    }

                    getString(R.string.green) -> {
                        selectedColor = intent.getStringExtra(SELECTED_COLOR) ?: ""
                        binding.viewSubtitleIndicator.setBackgroundColor(Color.parseColor(selectedColor))
                    }

                    getString(R.string.orange) -> {
                        selectedColor = intent.getStringExtra(SELECTED_COLOR) ?: ""
                        binding.viewSubtitleIndicator.setBackgroundColor(Color.parseColor(selectedColor))
                    }

                    getString(R.string.purple) -> {
                        selectedColor = intent.getStringExtra(SELECTED_COLOR) ?: ""
                        binding.viewSubtitleIndicator.setBackgroundColor(Color.parseColor(selectedColor))
                    }

                    getString(R.string.red) -> {
                        selectedColor = intent.getStringExtra(SELECTED_COLOR) ?: ""
                        binding.viewSubtitleIndicator.setBackgroundColor(Color.parseColor(selectedColor))
                    }

                    getString(R.string.yellow) -> {
                        selectedColor = intent.getStringExtra(SELECTED_COLOR) ?: ""
                        binding.viewSubtitleIndicator.setBackgroundColor(Color.parseColor(selectedColor))
                    }

                    getString(R.string.brown) -> {
                        selectedColor = intent.getStringExtra(SELECTED_COLOR) ?: ""
                        binding.viewSubtitleIndicator.setBackgroundColor(Color.parseColor(selectedColor))
                    }

                    getString(R.string.indigo) -> {
                        selectedColor = intent.getStringExtra(SELECTED_COLOR) ?: ""
                        binding.viewSubtitleIndicator.setBackgroundColor(Color.parseColor(selectedColor))
                    }

                    getString(R.string.image) -> {
                        readStorageTask()
                        binding.layoutWebUrl.makeGone()
                    }

                    getString(R.string.webUrl) -> {
                        binding.layoutWebUrl.visibility = View.VISIBLE
                    }

                    getString(R.string.deleteNote) -> {
                        deleteNote()
                    }

                    else -> {
                        binding.layoutImage.visibility = View.GONE
                        binding.imageNote.visibility = View.GONE
                        binding.layoutWebUrl.visibility = View.GONE
                        selectedColor = intent.getStringExtra(SELECTED_COLOR) ?: ""
                        makeGone(with(binding) {
                            layoutImage
                            imageNote
                            layoutWebUrl
                        })
                        selectedColor = intent.getStringExtra(SELECTED_COLOR).orEmpty()
                        binding.viewSubtitleIndicator.setBackgroundColor(Color.parseColor(selectedColor))
                    }
                }
            }
        }
    }

    private fun hasReadStoragePerm(): Int {
        return requireActivity().checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun hasReadImagePerm(): Int {
        return requireActivity().checkSelfPermission(android.Manifest.permission.READ_MEDIA_IMAGES)
    }

    private fun readStorageTask(){
         if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
             if(hasReadImagePerm() == 1){
                 pickImageFromGallery()
             }else{
                 requireActivity().requestPermissions(arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),PackageManager.PERMISSION_GRANTED)
             }
         }else{
             if(hasReadStoragePerm() == 1) {
                 pickImageFromGallery()
             }else{
                 requireActivity().requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),PackageManager.PERMISSION_GRANTED)
             }
         }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            getResultForImage.launch(intent)
        }else{
            if(intent.resolveActivity(requireActivity().packageManager) != null){
                getResultForImage.launch(intent)
            }
        }
    }

    private fun getPathFromUri(contentUri: Uri): String? {
        val filePath: String?
        val cursor = requireActivity().contentResolver.query(contentUri,null,null,null,null)
        if(cursor == null){
            filePath = contentUri.path
        }else{
            cursor.moveToFirst()
            val index = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        }
        return filePath
    }

    private val getResultForImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            if (it.data != null) {
                val selectedImageUrl = it.data!!.data

                if (selectedImageUrl != null) {
                    try {
                        val inputStream =
                            requireActivity().contentResolver.openInputStream(selectedImageUrl)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        binding.imageNote.setImageBitmap(bitmap)
                        binding.imageNote.makeVisible()
                        binding.layoutImage.makeVisible()
                        selectedImagePath = getPathFromUri(selectedImageUrl).orEmpty()
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}