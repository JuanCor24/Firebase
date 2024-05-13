package com.example.taller3

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.taller3.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            val intent = Intent(this, MenuPrincipal::class.java )
            startActivity(intent)
        }

        binding.enlaceRegistro.setOnClickListener{
            val intent = Intent(this, RegistrarseActivity::class.java )
            startActivity(intent)
        }

    }
}