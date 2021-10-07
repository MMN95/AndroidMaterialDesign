package ru.mmn.androidmaterialdesign.view.picture

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.mmn.androidmaterialdesign.R
import ru.mmn.androidmaterialdesign.api.ApiActivity
import ru.mmn.androidmaterialdesign.databinding.BottomNavigationLayoutBinding

class BottomNavigationDrawerFragment : BottomSheetDialogFragment() {

    private var _binding: BottomNavigationLayoutBinding? = null
    private val binding: BottomNavigationLayoutBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomNavigationLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigationOne -> startActivity(Intent(context, ApiActivity::class.java))
                R.id.navigationTwo -> Toast.makeText(context, "2", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = BottomNavigationDrawerFragment()
    }
}