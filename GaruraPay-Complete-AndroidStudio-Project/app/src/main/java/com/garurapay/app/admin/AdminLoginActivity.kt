package com.garurapay.app.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.garurapay.app.R

class AdminLoginActivity : AppCompatActivity() {

    private lateinit var etAdminPassword: EditText
    private lateinit var btnAdminLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        etAdminPassword = findViewById(R.id.etAdminPassword)
        btnAdminLogin = findViewById(R.id.btnAdminLoginSubmit)

        // Ensure field is empty by default
        etAdminPassword.text.clear()

        btnAdminLogin.setOnClickListener {
            val password = etAdminPassword.text.toString().trim()

            // Verification is checked internally in Kotlin logic only - never shown on UI
            if (password == "Ji@9835659964") {
                Toast.makeText(this, "Admin access granted", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, AdminDashboardActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Invalid Admin Password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
