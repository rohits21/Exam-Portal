package com.example.examportal.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.examportal.R
import com.example.examportal.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    lateinit var firebaseAuth: FirebaseAuth
    private var binding : ActivityProfileBinding ?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        firebaseAuth = FirebaseAuth.getInstance()
        binding?.txtEmail!!.text = firebaseAuth.currentUser?.email

        binding?.btnLogout!!.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginIntro::class.java)
            startActivity(intent)
            finish()

        }
    }
}