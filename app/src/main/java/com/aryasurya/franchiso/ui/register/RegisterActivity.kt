package com.aryasurya.franchiso.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import com.aryasurya.franchiso.R
import com.aryasurya.franchiso.ViewModelFactory
import com.aryasurya.franchiso.data.Result
import com.aryasurya.franchiso.databinding.ActivityRegisterBinding
import com.aryasurya.franchiso.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvLoginHere.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }

        viewModel.createUserResult.observe(this) { result ->
            when(result) {
                is Result.Loading -> {
                    binding.overlayLoading.visibility = View.VISIBLE
                    window.setFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    )
                }
                is Result.Success -> {
                    binding.overlayLoading.visibility = View.GONE
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    Toast.makeText(this,
                        getString(R.string.registration_successful), Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                }
                is Result.Error -> {
                    binding.overlayLoading.visibility = View.GONE
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    Toast.makeText(this,
                        getString(R.string.email_is_already_taken), Toast.LENGTH_SHORT).show()
                }
            }
        }

        validEmail()
        matchPassword()
        activeButton()
    }
    private fun activeButton() {
        val name = binding.tiNameRegister
        val email = binding.tiEmailRegister
        val password = binding.tiPasswordRegister
        val btnRegister = binding.btnRegister

        btnRegister.setOnClickListener {
            val inputName = name.editText?.text.toString()
            val inputEmail = email.editText?.text.toString()
            val inputPassword = password.editText?.text.toString()
            val confirmPassword = binding.tiConfirmPasswordRegister.editText?.text.toString()

            if ( isNameValid(inputName) && isEmailValid(inputEmail) && isPasswordValid(inputPassword) && confirmPassword.isNotEmpty()) {
                viewModel.createUser(inputEmail, inputName, inputPassword)

            }
            else {
                name.isErrorEnabled = false
                email.isErrorEnabled = false
                password.isErrorEnabled = false

                if (!isNameValid(inputName)) {
                    name.error = "Fill the name"
                }

                if (!isEmailValid(inputEmail)) {
                    email.error = getString(R.string.invalid_email_address)
                }

                if (!isPasswordValid(inputPassword)) {
                    password.error = getString(R.string.minimal_8_character)
                }

            }
        }
    }

    private fun matchPassword() {
        val passwordLayout = binding.tiPasswordRegister
        val password = passwordLayout.editText
        val confirmPasswordLayout = binding.tiConfirmPasswordRegister
        val confirmPassword = confirmPasswordLayout.editText

        password?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence? , p1: Int , p2: Int , p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence? , p1: Int , p2: Int , p3: Int) {
                if (p0 != null && p0.length <= 7) {
                    passwordLayout.apply {
                        isErrorEnabled = true
                        errorIconDrawable = null
                        error = getString(R.string.eror_text)
                    }
                } else {
                    passwordLayout.apply {
                        isErrorEnabled = false
                        error = null
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isEmpty()) {
                    confirmPasswordLayout.isErrorEnabled = true
                    confirmPasswordLayout.error = getString(R.string.input_confirm_password)
                } else {
                    confirmPasswordLayout.isErrorEnabled = false
                    confirmPasswordLayout.error = null
                }
            }

        })

        confirmPassword?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence? , p1: Int , p2: Int , p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence? , p1: Int , p2: Int , p3: Int) {
                if (p0.toString() != password?.text.toString()) {
                    confirmPasswordLayout.isErrorEnabled = true
                    confirmPasswordLayout.error = getString(R.string.confirm_password_eror_text)
                } else {
                    confirmPasswordLayout.isErrorEnabled = false
                    confirmPasswordLayout.error = null
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun validEmail() {
        val inputEmailLayout = binding.tiEmailRegister
        val inputEmail = binding.inputEmailRegister
        inputEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence? , p1: Int , p2: Int , p3: Int) {}

            override fun onTextChanged(p0: CharSequence? , p1: Int , p2: Int , p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                val email = p0.toString()
                val isValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()

                if (!isValid) {
                    inputEmailLayout.error = getString(R.string.invalid_email_address)
                } else {
                    // Hapus pesan kesalahan jika email valid
                    inputEmailLayout.isErrorEnabled = false
                    inputEmailLayout.error = null
                }
            }
        })
    }
    private fun isUsernameValid(username: String): Boolean {
        return username.isNotEmpty()
    }
    private fun isNameValid(name: String): Boolean {
        return name.isNotEmpty()
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 8
    }


}