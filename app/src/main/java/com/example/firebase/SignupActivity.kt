package com.example.firebase

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebase.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    private var _binding: ActivitySignupBinding? = null
    private val binding get() = _binding!!
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.apply {
            btnResetPassword.setOnClickListener {
                startActivity(
                    Intent(
                        this@SignupActivity,
                        ResetPasswordActivity::class.java
                    )
                )
            }

            signInButton.setOnClickListener { finish() }

            signUpButton.setOnClickListener {
                val email = email.text.toString().trim { it <= ' ' }
                val password = password.text.toString().trim { it <= ' ' }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (password.length < 6) {
                    Toast.makeText(
                        applicationContext,
                        "Password too short, enter minimum 6 characters!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                binding.progressBar.visibility = View.VISIBLE

                auth?.createUserWithEmailAndPassword(email, password)
                    ?.addOnCompleteListener(
                        this@SignupActivity
                    ) { task ->
                        Toast.makeText(
                            this@SignupActivity,
                            "createUserWithEmail:onComplete:" + task.isSuccessful,
                            Toast.LENGTH_SHORT
                        ).show()

                        binding.progressBar.visibility = View.GONE

                        if (!task.isSuccessful) {
                            Toast.makeText(
                                this@SignupActivity, "Authentication failed." + task.exception,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            startActivity(Intent(this@SignupActivity, MainActivity::class.java))
                            finish()
                        }
                    }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.progressBar.visibility = View.GONE
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}