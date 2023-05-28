package com.example.firebase

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebase.databinding.ActivityAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener

class AccountActivity : AppCompatActivity() {

    private var _binding: ActivityAccountBinding? = null
    private val binding get() = _binding!!
    private var authListener: AuthStateListener? = null
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.apply {
            val user = auth?.currentUser

            authListener = AuthStateListener { firebaseAuth ->
                val userName = firebaseAuth.currentUser
                if (userName == null) {
                    startActivity(Intent(this@AccountActivity, LoginActivity::class.java))
                    finish()
                }
            }

            progressBar.visibility = View.GONE

            changeEmailButton.setOnClickListener {
                progressBar.visibility = View.VISIBLE

                if (user != null && newEmail.text.toString().trim { it <= ' ' } != "") {
                    user.updateEmail(newEmail.text.toString().trim { it <= ' ' })
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this@AccountActivity,
                                    "Email address is updated. Please sign in with new email!",
                                    Toast.LENGTH_LONG
                                ).show()

                                signOut()

                                progressBar.visibility = View.GONE
                            } else {
                                Toast.makeText(
                                    this@AccountActivity,
                                    "Failed to update email!",
                                    Toast.LENGTH_LONG
                                ).show()

                                progressBar.visibility = View.GONE
                            }
                        }
                } else if (newEmail.text.toString().trim { it <= ' ' } == "") {
                    newEmail.error = "Enter email"
                    progressBar.visibility = View.GONE
                }
            }

            removeUserButton.setOnClickListener {
                progressBar.visibility = View.VISIBLE

                user?.delete()?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@AccountActivity,
                            "Your profile is deleted:( Create a account now!",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this@AccountActivity, SignupActivity::class.java))
                        finish()

                        progressBar.visibility = View.GONE
                    } else {
                        Toast.makeText(
                            this@AccountActivity,
                            "Failed to delete your account!",
                            Toast.LENGTH_SHORT
                        ).show()
                        progressBar.visibility = View.GONE
                    }
                }
            }

            signOut.setOnClickListener { signOut() }
        }
    }

    private fun signOut() {
        auth?.signOut()
    }

    override fun onResume() {
        super.onResume()
        binding.progressBar.visibility = View.GONE
    }

    public override fun onStart() {
        super.onStart()
        auth?.addAuthStateListener(authListener!!)
    }

    public override fun onStop() {
        super.onStop()
        if (authListener != null) {
            auth?.removeAuthStateListener(authListener!!)
        }
    }
}