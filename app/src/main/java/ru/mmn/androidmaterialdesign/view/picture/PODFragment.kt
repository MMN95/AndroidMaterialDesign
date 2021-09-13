package ru.mmn.androidmaterialdesign.view.picture

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import ru.mmn.androidmaterialdesign.R
import ru.mmn.androidmaterialdesign.databinding.FragmentMainBinding
import ru.mmn.androidmaterialdesign.hide
import ru.mmn.androidmaterialdesign.repository.PODServerResponseData
import ru.mmn.androidmaterialdesign.show
import ru.mmn.androidmaterialdesign.viewmodel.PODData
import ru.mmn.androidmaterialdesign.viewmodel.PODViewModel

class PODFragment : Fragment() {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
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
        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetLayoutInclude.bottomSheetContainer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) { //FIXME
                when (newState) {
                    BottomSheetBehavior.STATE_DRAGGING -> Toast.makeText(context, "Dragging", Toast.LENGTH_SHORT).show()
                    BottomSheetBehavior.STATE_COLLAPSED -> Toast.makeText(context, "Collapsed", Toast.LENGTH_SHORT).show()
                    BottomSheetBehavior.STATE_EXPANDED -> binding.bottomSheetLayoutInclude.bottomSheetDescriptionHeader
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> Toast.makeText(context, "Half-expanded", Toast.LENGTH_SHORT).show()
                    BottomSheetBehavior.STATE_HIDDEN -> Toast.makeText(context, "Hidden", Toast.LENGTH_SHORT).show()
                    BottomSheetBehavior.STATE_SETTLING -> Toast.makeText(context, "Settling", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) { //FIXME
            }
        })


    }

    private fun renderData(data: PODData) {
        when (data) {
            is PODData.Error -> {
                Toast.makeText(context, data.error.message, Toast.LENGTH_LONG).show()
            }
            is PODData.Loading -> {
                binding.loadingLayoutInclude.loadingLayout.show()
            }
            is PODData.Success -> {
                binding.loadingLayoutInclude.loadingLayout.hide()
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



