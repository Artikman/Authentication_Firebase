package com.example.firebase

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebase.databinding.ActivityResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordActivity : AppCompatActivity() {

    private var _binding: ActivityResetPasswordBinding? = null
    private val binding get() = _binding!!
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.apply {
            btnBack.setOnClickListener { finish() }

            btnResetPassword.setOnClickListener {
                val email = email.text.toString().trim { it <= ' ' }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(
                        application,
                        "Enter your registered email id",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                progressBar.visibility = View.VISIBLE

                auth?.sendPasswordResetEmail(email)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this@ResetPasswordActivity,
                                "We have sent you instructions to reset your password!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this@ResetPasswordActivity,
                                "Failed to send reset email!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        progressBar.visibility = View.GONE
                    }
            }
        }
    }
}