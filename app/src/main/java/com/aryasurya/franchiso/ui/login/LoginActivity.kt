package com.aryasurya.franchiso.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import com.aryasurya.franchiso.MainActivity
import com.aryasurya.franchiso.R
import com.aryasurya.franchiso.ViewModelFactory
import com.aryasurya.franchiso.data.Result
import com.aryasurya.franchiso.data.pref.UserModel
import com.aryasurya.franchiso.databinding.ActivityLoginBinding
import com.aryasurya.franchiso.ui.register.RegisterActivity
import com.aryasurya.franchiso.utils.isInternetAvailable

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding


    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvNotHaveAcc.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

//        binding.btnLogin.setOnClickListener {
//            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
//        }


        viewModel.getSession().observe(this) { user ->
            if (user.isLogin) {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }

        viewModel.loginResult.observe(this) { result ->
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
                    viewModel.saveSession(UserModel(result.data.data.name, result.data.data.appToken))
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                is Result.Error -> {
                    binding.overlayLoading.visibility = View.GONE
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    if (!isInternetAvailable(this)) {
                        Toast.makeText(this, getString(R.string.network_error), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this,
                            getString(R.string.username_password_is_incorrect), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            val username = binding.tlUsername.editText?.text.toString()
            val password = binding.tlPassword.editText?.text.toString()

            viewModel.login(username, password)
        }
    }
}