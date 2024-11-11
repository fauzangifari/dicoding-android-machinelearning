package com.dicoding.asclepius.presentation.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dicoding.asclepius.databinding.FragmentHomeBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.yalantis.ucrop.UCrop
import dagger.hilt.android.AndroidEntryPoint
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import java.text.NumberFormat

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var currentImageUri: Uri? = null
    private lateinit var imageClassifierHelper: ImageClassifierHelper

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            startCrop(uri)
        } else {
            showToast("Image selection failed")
        }
    }

    private val cropLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val resultUri = UCrop.getOutput(result.data!!)
            if (resultUri != null) {
                currentImageUri = resultUri
                showImage()
            } else {
                showToast("Image cropping failed")
            }
        } else if (result.resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(result.data!!)
            showToast("Crop error: ${cropError?.message}")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.galleryButton.setOnClickListener {
            startGallery()
        }

        binding.analyzeButton.setOnClickListener {
            analyzeImage()
        }

        return binding.root
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCrop(uri: Uri) {
        val destinationUri = Uri.fromFile(File(requireContext().cacheDir, "cropped_image.jpg"))
        UCrop.of(uri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(1080, 1080)
            .start(requireContext(), cropLauncher)
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.previewImageView.setImageURI(null)
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage() {
        if (currentImageUri != null) {
            imageClassifierHelper = ImageClassifierHelper(
                context = requireContext(),
                classifierListener = object : ImageClassifierHelper.ClassifierListener {
                    override fun onError(error: String) {
                        requireActivity().runOnUiThread {
                            showToast(error)
                        }
                    }

                    override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                        requireActivity().runOnUiThread {
                            results?.let {
                                if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                    val highestCategory = it[0].categories.maxByOrNull { category -> category.score }
                                    highestCategory?.let { category ->
                                        val displayResult = "${category.label} " + NumberFormat.getPercentInstance()
                                            .format(category.score).trim()
                                        moveToResult(displayResult, currentImageUri.toString())
                                    } ?: run {
                                        showToast("Failed to analyze image")
                                    }
                                } else {
                                    showToast("Failed to analyze image")
                                }
                            }
                        }
                    }
                }
            )

            imageClassifierHelper.classifyImage(currentImageUri!!)
        } else {
            showToast("Please select an image first")
        }
    }

    private fun moveToResult(displayResult: String, imageUri: String) {
        val intent = Intent(requireContext(), ResultActivity::class.java).apply {
            putExtra("ANALYSIS_RESULT", displayResult)
            putExtra("IMAGE_URI", imageUri)
        }
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}
