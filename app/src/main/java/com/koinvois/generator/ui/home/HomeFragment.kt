package com.koinvois.generator.ui.home

import android.content.Context
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.koinvois.generator.R
import com.koinvois.generator.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
   // private val viewModel: HomeViewModel by hiltNavGraphViewModels(R.id.navigation_home)

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //  val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    private fun createWebPrintJob(webView: WebView) {

        // Get a PrintManager instance
        val printManager = activity?.getSystemService(Context.PRINT_SERVICE) as PrintManager

        // Get a print adapter instance
        val printAdapter = webView.createPrintDocumentAdapter("test")

        // Create a print job with name and adapter instance
        val jobName = getString(R.string.app_name) + " Document"
        printManager.print(jobName, printAdapter, PrintAttributes.Builder().build())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}