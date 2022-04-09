package com.ineedyourcode.decomposeme.ui.mainscreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ineedyourcode.decomposeme.databinding.ActivityMainScreenBinding
import com.ineedyourcode.decomposeme.presenter.login.LoginActivityPresenter

class MainScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainScreenBinding
    private lateinit var mainScreenPresenter: LoginActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}