package com.garurapay.app.auth

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.garurapay.app.MainActivity
import com.garurapay.app.R
import com.garurapay.app.data.AppDatabase
import com.garurapay.app.data.SessionManager
import com.garurapay.app.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignupActivity : AppCompatActivity() {

    private lateinit var etFullName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignup: Button
    private lateinit var tvBackToLogin: TextView
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        sessionManager = SessionManager(this)

        etFullName = findViewById(R.id.etSignupFullName)
        etEmail = findViewById(R.id.etSignupEmail)
        etPassword = findViewById(R.id.etSignupPassword)
        btnSignup = findViewById(R.id.btnSignupSubmit)
        tvBackToLogin = findViewById(R.id.tvBackToLogin)

        btnSignup.setOnClickListener {
            val name = etFullName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Requirement 1: Generate unique 9-digit User ID
            val generated9DigitId = sessionManager.generate9DigitUserId()

            val newUser = User(
                userId = generated9DigitId,
                email = email,
                passwordHash = password,
                fullName = name,
                balance = 0.00
            )

            lifecycleScope.launch {
                val db = AppDatabase.getInstance(this@SignupActivity)
                withContext(Dispatchers.IO) {
                    db.userDao().insertUser(newUser)
                }

                sessionManager.saveUserSession(
                    userId = generated9DigitId,
                    email = email,
                    name = name,
                    balance = 0.00
                )

                // Requirement 3: Highly visible warning note right below the ID:
                // Exact text: "Must take screen shot of this ID for future to forget password"
                AlertDialog.Builder(this@SignupActivity)
                    .setTitle("Registration Successful!")
                    .setMessage(
                        "Your Unique 9-Digit User ID is:

" +
                        "★ ${generated9DigitId} ★

" +
                        "⚠️ Must take screen shot of this ID for future to forget password"
                    )
                    .setPositiveButton("I have taken a screenshot") { dialog, _ ->
                        dialog.dismiss()
                        startActivity(Intent(this@SignupActivity, MainActivity::class.java))
                        finish()
                    }
                    .setCancelable(false)
                    .show()
            }
        }

        tvBackToLogin.setOnClickListener {
            finish()
        }
    }
}
