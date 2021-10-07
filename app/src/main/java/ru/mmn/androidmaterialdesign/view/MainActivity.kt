package ru.mmn.androidmaterialdesign.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ru.mmn.androidmaterialdesign.R
import ru.mmn.androidmaterialdesign.databinding.ActivityMainBinding
import ru.mmn.androidmaterialdesign.view.picture.PODFragment

const val themeDark = 1
const val themeLight = 2

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val KEY_SP = "sp"
    private val KEY_CURRENT_THEME = "current_theme"

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(getRealStyle(getCurrentTheme()))
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PODFragment.newInstance())
                .commit()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.darkThemeChip -> setCurrentTheme(themeDark)
            R.id.lightThemeChip -> setCurrentTheme(themeLight)
        }
        recreate()
    }

    fun setCurrentTheme(currentTheme: Int) {
        val sharedPreferences = getSharedPreferences(KEY_SP, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_CURRENT_THEME, currentTheme)
        editor.apply()
    }

    fun getCurrentTheme(): Int {
        val sharedPreferences = getSharedPreferences(KEY_SP, MODE_PRIVATE)
        return sharedPreferences.getInt(KEY_CURRENT_THEME, -1)
    }

    private fun getRealStyle(currentTheme: Int): Int {
        return when (currentTheme) {
            themeDark -> R.style.Theme_AndroidMaterialDesign_Dark
            themeLight -> R.style.Theme_AndroidMaterialDesign_Light
            else -> 0
        }
    }

}