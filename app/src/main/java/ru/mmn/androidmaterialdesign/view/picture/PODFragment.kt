package ru.mmn.androidmaterialdesign.view.picture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import ru.mmn.androidmaterialdesign.R
import ru.mmn.androidmaterialdesign.databinding.FragmentMainBinding
import ru.mmn.androidmaterialdesign.hide
import ru.mmn.androidmaterialdesign.show
import ru.mmn.androidmaterialdesign.viewmodel.PODData
import ru.mmn.androidmaterialdesign.viewmodel.PODViewModel

class PODFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    val binding: FragmentMainBinding
        get() {
            return _binding!!
        }

    private val viewModel: PODViewModel by lazy {
        ViewModelProvider(this).get(PODViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.sendServerRequest()
    }

    private fun renderData(data: PODData) {
        when (data) {
            is PODData.Error -> {
                Toast.makeText(context, data.error.message, Toast.LENGTH_LONG).show()
            }
            is PODData.Loading -> {
                binding.loadingLayout.show()
            }
            is PODData.Success -> {
                binding.loadingLayout.hide()
                binding.imageView.load(data.serverResponseData.url) {
                    error(R.drawable.ic_load_error_vector)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = PODFragment()
    }
}



