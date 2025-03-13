package com.noble.assignment.pdf

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.noble.assignment.MainActivity
import com.noble.assignment.R
import com.noble.assignment.databinding.FragmentPdfViewerBinding
import com.noble.assignment.databinding.FragmentUserListBinding


class PdfViewerFragment : Fragment() {
    private var binding: FragmentPdfViewerBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pdf_viewer, container, false
        )
        (activity as MainActivity).setTitle("Pdf Viewer")
        binding?.pdfViewer?.fromAsset("BalanceSheet.pdf")
            ?.enableSwipe(true)
            ?.swipeHorizontal(false)
            ?.enableDoubletap(true)
            ?.defaultPage(0)
            ?.enableAnnotationRendering(false)
            ?.password(null)
            ?.scrollHandle(null)
            ?.enableAntialiasing(true)
            ?.spacing(0)
            ?.load()

        return binding?.root
    }

}