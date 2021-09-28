package ru.mmn.androidmaterialdesign.api

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import ru.mmn.androidmaterialdesign.R
import ru.mmn.androidmaterialdesign.databinding.ActivityApiBinding

class ApiActivity: AppCompatActivity() {

    lateinit var binding: ActivityApiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewPager.adapter = ViewPagerAdapter(supportFragmentManager)
    }
}