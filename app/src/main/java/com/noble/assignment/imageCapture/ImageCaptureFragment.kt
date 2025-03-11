package com.noble.assignment.imageCapture

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.noble.assignment.MainViewModel
import com.noble.assignment.R
import com.noble.assignment.databinding.FragmentImageCaptureBinding
import com.noble.assignment.databinding.FragmentUserListBinding
import java.io.File


class ImageCaptureFragment : Fragment() {

    private var viewModel: MainViewModel? = null
    private var binding: FragmentImageCaptureBinding? = null
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_image_capture, container, false
        )
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]


        pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                uri?.let {
                    val filePath = getPathFromUri(uri)
                    Glide.with(requireContext())
                        .load(uri)
                        .into(binding!!.lyimg)


                }
            }
        }

        binding?.btnPickImg?.setOnClickListener {
            pickimg()
        }

        return binding?.root
    }

    fun pickimg() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }


    fun getPathFromUri(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireActivity().contentResolver.query(uri, projection, null, null, null)
        return if (cursor != null) {
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val filePath = cursor.getString(columnIndex)
            cursor.close()
            filePath
        } else {
            null
        }
    }
}
