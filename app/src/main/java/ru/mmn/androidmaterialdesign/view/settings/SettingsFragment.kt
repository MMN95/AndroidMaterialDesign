package ru.mmn.androidmaterialdesign.view.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import ru.mmn.androidmaterialdesign.R
import ru.mmn.androidmaterialdesign.databinding.FragmentSettingsBinding
import ru.mmn.androidmaterialdesign.view.MainActivity
import ru.mmn.androidmaterialdesign.view.themeDark
import ru.mmn.androidmaterialdesign.view.themeLight

class SettingsFragment : Fragment(), View.OnClickListener {

    private lateinit var parentActivity: MainActivity

    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding
        get() {
            return _binding!!
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentActivity = requireActivity() as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.chipGroup.setOnCheckedChangeListener { chipGroup, position ->
            chipGroup.findViewById<Chip>(position)?.let {
                Toast.makeText(context, "Выбран ${it.text}", Toast.LENGTH_SHORT).show()
            }
        }
        binding.darkThemeChip.setOnClickListener(this)
        binding.lightThemeChip.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.darkThemeChip -> {
                parentActivity.setCurrentTheme(themeDark)
                parentActivity.recreate()
            }
            R.id.lightThemeChip -> {
                parentActivity.setCurrentTheme(themeLight)
                parentActivity.recreate()
            }
        }

    }
}