package com.example.securicamapplication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.load.data.DataRewinder
import com.example.securicamapplication.api.ApiConfig
import com.example.securicamapplication.customview.BtnRegister
import com.example.securicamapplication.customview.EdtEmail
import com.example.securicamapplication.customview.EdtPassword
import com.example.securicamapplication.customview.EdtUsername
import com.example.securicamapplication.data.DataResponse
import com.example.securicamapplication.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerBtn: BtnRegister
    private lateinit var emailEdt: EdtEmail
    private lateinit var passwordEdt: EdtPassword
    private lateinit var nameEdt: EdtUsername
    private var correctEmail: Boolean = false
    private var correctPassword: Boolean = false
    private var correctName: Boolean = false
    private val emailRegex: Regex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+\$")

    companion object {
        private const val DURATION = 200L
        private const val ALPHA = 1f
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        playAnimation()

        registerBtn = binding.registerButton
        emailEdt = binding.etEmail
        passwordEdt = binding.etPassword
        nameEdt = binding.etName

        nameEdt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.length >= 2) {
                    correctName = true
                } else if (s.isNullOrBlank()) {
                    correctName = false
                } else if (!s.isNullOrBlank() && s.length < 2) {
                    correctName = false
                    nameEdt.error = getString(R.string.min_name)
                }
                setLoginButtonEnable()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        emailEdt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty() && emailRegex.matches(s.toString())) {
                    correctEmail = true
                } else if (!s.isNullOrEmpty() && !emailRegex.matches(s.toString())) {
                    emailEdt.error = getString(R.string.invalid_email)
                    correctEmail = false
                } else {
                    correctEmail = false
                }
                setLoginButtonEnable()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        passwordEdt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                correctPassword = if (!s.isNullOrEmpty() && s.length < 6) {
                    passwordEdt.error = getString(R.string.min_password)
                    false
                } else !s.isNullOrEmpty()
                setLoginButtonEnable()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        registerBtn.setOnClickListener {
            register()
        }

        binding.tvAccount.setOnClickListener {
            val i = Intent(this, LogInActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    private fun setLoginButtonEnable() {
        registerBtn.isEnabled = correctName && correctEmail && correctPassword
    }

    private fun register() {
        val client = ApiConfig.getApiService().register(
            nameEdt.text.toString(),
            emailEdt.text.toString(),
            passwordEdt.text.toString()
        )
        client.enqueue(object : Callback<DataResponse> {
            override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    if (responseBody.error == true) {
                        Toast.makeText(
                            this@RegisterActivity,
                            responseBody.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        goLogin()
                        Toast.makeText(
                            this@RegisterActivity,
                            responseBody.message,
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    Toast.makeText(this@RegisterActivity, response.message(), Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                Toast.makeText(this@RegisterActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun playAnimation() {
        val registerApp = ObjectAnimator.ofFloat(binding.tvLogIn, View.ALPHA, ALPHA).setDuration(DURATION)
        val tvName = ObjectAnimator.ofFloat(binding.tvName, View.ALPHA, ALPHA).setDuration(DURATION)
        val edtName = ObjectAnimator.ofFloat(binding.etName, View.ALPHA, ALPHA).setDuration(DURATION)
        val tvEmail = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, ALPHA).setDuration(DURATION)
        val edtEmail = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, ALPHA).setDuration(DURATION)
        val tvPassword = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, ALPHA).setDuration(DURATION)
        val edtPassword = ObjectAnimator.ofFloat(binding.etPassword, View.ALPHA, ALPHA).setDuration(DURATION)
        val registerBtn =
            ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, ALPHA).setDuration(DURATION)
        val tvAccount = ObjectAnimator.ofFloat(binding.tvAccount, View.ALPHA, ALPHA).setDuration(DURATION)

        AnimatorSet().apply {
            playSequentially(
                registerApp,
                tvName,
                edtName,
                tvEmail,
                edtEmail,
                tvPassword,
                edtPassword,
                registerBtn,
                tvAccount
            )
            start()
        }
    }



    private fun goLogin() {
        val i = Intent(this, LogInActivity::class.java)
        i.putExtra("email", emailEdt.text.toString())
        i.putExtra("password", passwordEdt.text.toString())
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(i)
        finish()
    }
}