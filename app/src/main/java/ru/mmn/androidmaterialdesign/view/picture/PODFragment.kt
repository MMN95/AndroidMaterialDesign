package ru.mmn.androidmaterialdesign.view.picture

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import ru.mmn.androidmaterialdesign.R
import ru.mmn.androidmaterialdesign.databinding.FragmentMainBinding
import ru.mmn.androidmaterialdesign.view.MainActivity
import ru.mmn.androidmaterialdesign.view.chips.ChipsFragment
import ru.mmn.androidmaterialdesign.viewmodel.PODData
import ru.mmn.androidmaterialdesign.viewmodel.PODViewModel

class PODFragment : Fragment() {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() {
            return _binding!!
        }

    private val viewModel: PODViewModel by lazy {
        ViewModelProvider(this).get(PODViewModel::class.java)
    }
    private var isMain = true
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomAppBar(view)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.sendServerRequest()
        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }
        binding.scrollView.setOnScrollChangeListener { it, q, w, e, r ->
            binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
        }
        bottomSheetBehavior =
            BottomSheetBehavior.from(binding.bottomSheetLayoutInclude.bottomSheetContainer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(
                bottomSheet: View,
                newState: Int
            ) { //FIXME переделать, чтобы описание выводилось под фото
                when (newState) {
                    BottomSheetBehavior.STATE_DRAGGING -> Toast.makeText(
                        context,
                        "Dragging",
                        Toast.LENGTH_SHORT
                    ).show()
                    BottomSheetBehavior.STATE_COLLAPSED -> Toast.makeText(
                        context,
                        "Collapsed",
                        Toast.LENGTH_SHORT
                    ).show()
                    BottomSheetBehavior.STATE_EXPANDED -> binding.bottomSheetLayoutInclude.bottomSheetDescriptionHeader
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> Toast.makeText(
                        context,
                        "Half-expanded",
                        Toast.LENGTH_SHORT
                    ).show()
                    BottomSheetBehavior.STATE_HIDDEN -> Toast.makeText(
                        context,
                        "Hidden",
                        Toast.LENGTH_SHORT
                    ).show()
                    BottomSheetBehavior.STATE_SETTLING -> Toast.makeText(
                        context,
                        "Settling",
                        Toast.LENGTH_SHORT
                    ).show()
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
                binding.imageView.load(R.drawable.progress_animation){
                    error(R.drawable.ic_load_error_vector)
                }
            }
            is PODData.Success -> {
                binding.imageView.load(data.serverResponseData.url) {
                    placeholder(R.drawable.progress_animation)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.appBarFav -> Toast.makeText(context, "Favourite", Toast.LENGTH_SHORT).show()
            R.id.appBarSearch -> Toast.makeText(context, "Search", Toast.LENGTH_SHORT).show()
            R.id.appBarSettings -> {
                activity?.supportFragmentManager?.beginTransaction()?.add(
                    R.id.container,
                    ChipsFragment.newInstance()
                )?.addToBackStack(null)?.commit()
            }
            android.R.id.home -> {
                activity?.let {
                    BottomNavigationDrawerFragment.newInstance()
                        .show(it.supportFragmentManager, "tag")
                }
            }


        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBottomAppBar(view: View) {
        val context = activity as MainActivity
        context.setSupportActionBar(view.findViewById(R.id.bottomAppBar))
        setHasOptionsMenu(true)
        binding.fab.setOnClickListener {
            if (isMain) {
                isMain = false
                binding.bottomAppBar.navigationIcon = null
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                binding.fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_back_fab
                    )
                )
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar_other_screen)
            } else {
                isMain = true
                binding.bottomAppBar.navigationIcon =
                    ContextCompat.getDrawable(context, R.drawable.ic_hamburger_menu_bottom_bar)
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                binding.fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_plus_fab
                    )
                )
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)
            }
        }


    }
}



