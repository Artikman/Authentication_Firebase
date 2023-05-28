package com.example.firebase

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebase.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        if (auth?.currentUser != null) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }

        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnSignup.setOnClickListener {
                startActivity(
                    Intent(
                        this@LoginActivity,
                        SignupActivity::class.java
                    )
                )
            }

            btnResetPassword.setOnClickListener {
                startActivity(
                    Intent(
                        this@LoginActivity,
                        ResetPasswordActivity::class.java
                    )
                )
            }

            btnLogin.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    val email = email.text.toString()
                    val password = password.text.toString()

                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(
                            applicationContext,
                            "Enter email address!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        return
                    }

                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT)
                            .show()
                        return
                    }

                    progressBar.visibility = View.VISIBLE

                    auth?.signInWithEmailAndPassword(email, password)
                        ?.addOnCompleteListener(
                            this@LoginActivity
                        ) { task ->
                            progressBar.visibility = View.GONE

                            if (!task.isSuccessful) {
                                if (password.length < 6) {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        getString(R.string.minimum_password),
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        getString(R.string.auth_failed),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } else {
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                }
            })
        }
    }
}