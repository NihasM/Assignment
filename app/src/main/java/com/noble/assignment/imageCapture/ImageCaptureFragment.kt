package com.noble.assignment.imageCapture

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.noble.assignment.MainActivity
import com.noble.assignment.MainViewModel
import com.noble.assignment.R
import com.noble.assignment.databinding.FragmentImageCaptureBinding
import com.noble.assignment.databinding.FragmentUserListBinding
import java.io.File


class ImageCaptureFragment : Fragment() {

    private var viewModel: MainViewModel? = null
    private var binding: FragmentImageCaptureBinding? = null
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var takePictureLauncher: ActivityResultLauncher<Intent>
    private var imageUri: Uri? = null


    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val cameraPermissionGranted = permissions[Manifest.permission.CAMERA] == true
            if (cameraPermissionGranted) {

                captureImage()
            } else {

                Toast.makeText(requireContext(), "Camera Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_image_capture, container, false
        )
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        (activity as MainActivity).setTitle("Image Upload")


        pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                uri?.let {
                    Glide.with(requireContext())
                        .load(uri)
                        .into(binding!!.lyimg)
                }
            }
        }


        takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && imageUri != null) {
                Glide.with(requireContext())
                    .load(imageUri)
                    .into(binding!!.lyimg)
            }
        }

        binding?.btnPickImg?.setOnClickListener {
            pickimg()
        }

        binding?.btnCamera?.setOnClickListener {

            requestPermissionLauncher.launch(
                arrayOf(Manifest.permission.CAMERA)
            )
        }

        return binding?.root
    }


    fun pickimg() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }


    fun captureImage() {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.TITLE, "New Image")
            put(MediaStore.Images.Media.DESCRIPTION, "Captured from Camera")
        }

        imageUri = requireActivity().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
        )

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, imageUri)  // Specify where to save the image
        }

        takePictureLauncher.launch(intent)
    }
}



