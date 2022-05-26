package com.example.securicamapplication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.securicamapplication.api.ApiConfig
import com.example.securicamapplication.customview.BtnLogin
import com.example.securicamapplication.customview.EdtEmail
import com.example.securicamapplication.customview.EdtPassword
import com.example.securicamapplication.data.LogIn
import com.example.securicamapplication.data.LoginResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.securicamapplication.databinding.ActivityLogInBinding
import com.example.securicamapplication.model.LoginViewModel
import com.example.securicamapplication.model.ViewModelFactory

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LogInActivity : AppCompatActivity() {

    companion object {
        private const val DURATION = 200L
        private const val ALPHA = 1f
    }

    private lateinit var binding: ActivityLogInBinding
    private lateinit var loginBtn: BtnLogin
    private lateinit var emailEdt: EdtEmail
    private lateinit var passwordEdt: EdtPassword

    private lateinit var loginViewModel: LoginViewModel

    private var correctEmail = false
    private var correctPassword = false

    private val emailRegex: Regex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+\$")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        playAnimation()

        loginBtn = binding.btnLogin
        emailEdt = binding.edtEmail
        passwordEdt = binding.edtPassword

        val pref = UserManager.getInstance(dataStore)

        if (!intent.getStringExtra("email").isNullOrEmpty()) {
            emailEdt.setText(intent.getStringExtra("email"))
            correctEmail = true
        }
        if (!intent.getStringExtra("password").isNullOrEmpty()) {
            passwordEdt.setText(intent.getStringExtra("password"))
            correctPassword = true
        }

        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(pref)
        )[LoginViewModel::class.java]

        setLoginButtonEnable()

        loginViewModel.getToken().observe(
            this
        ) { token: String ->
            if (token.isNotEmpty()) {
                val i = Intent(this, ChoosenDeviceActivity::class.java)
                startActivity(i)
            }
        }

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
                } else {
                    true
                }
                setLoginButtonEnable()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        loginBtn.setOnClickListener {
            login()
        }

        binding.tvAccount.setOnClickListener {
            val i = Intent(this, RegisterActivity::class.java)
            startActivity(i)
        }
    }

    private fun setLoginButtonEnable() {
        loginBtn.isEnabled = correctEmail && correctPassword
    }

    private fun playAnimation() {
        val logoApp =ObjectAnimator.ofFloat(binding.tvLogIn, View.ALPHA, ALPHA).setDuration(DURATION)
        val welcome = ObjectAnimator.ofFloat(binding.tvWelcome, View.ALPHA, ALPHA).setDuration(DURATION)
        val tvEmail = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, ALPHA).setDuration(DURATION)
        val edtEmail = ObjectAnimator.ofFloat(binding.edtEmail, View.ALPHA, ALPHA).setDuration(DURATION)
        val tvPassword = ObjectAnimator.ofFloat(binding.password, View.ALPHA, ALPHA).setDuration(DURATION)
        val edtPassword = ObjectAnimator.ofFloat(binding.edtPassword, View.ALPHA, ALPHA).setDuration(DURATION)
        val loginBtn = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, ALPHA).setDuration(DURATION)
        val tvAccount = ObjectAnimator.ofFloat(binding.tvAccount, View.ALPHA, ALPHA).setDuration(DURATION)

        AnimatorSet().apply {
            playSequentially(
                logoApp,
                welcome,
                tvEmail,
                edtEmail,
                tvPassword,
                edtPassword,
                loginBtn,
                tvAccount
            )
            start()
        }
    }

    private fun login() {
        val client = ApiConfig.getApiService()
            .login(emailEdt.text.toString(), passwordEdt.text.toString())
        client.enqueue(object : Callback<LogIn> {
            override fun onResponse(call: Call<LogIn>, response: Response<LogIn>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    if (responseBody.error == true) {
                        Toast.makeText(this@LogInActivity, responseBody.message, Toast.LENGTH_LONG)
                            .show()
                    } else {
                        saveSession(responseBody.loginResult as LoginResult)
                        Toast.makeText(
                            this@LogInActivity,
                            getString(R.string.login_sucess),
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    Toast.makeText(this@LogInActivity, response.message(), Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<LogIn>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                Toast.makeText(this@LogInActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }


    private fun saveSession(loginResult: LoginResult) {
        loginViewModel.saveToken(loginResult.token as String)
        val i = Intent(this, MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(i)
        finish()
    }


}