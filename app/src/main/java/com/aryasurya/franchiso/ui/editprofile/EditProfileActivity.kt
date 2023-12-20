package com.aryasurya.franchiso.ui.editprofile

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.aryasurya.franchiso.ui.camera.CameraActivity
import com.aryasurya.franchiso.ui.camera.CameraActivity.Companion.CAMERAX_RESULT
import com.aryasurya.franchiso.R
import com.aryasurya.franchiso.ViewModelFactory
import com.aryasurya.franchiso.data.pref.UserModel
import com.aryasurya.franchiso.databinding.ActivityEditProfileBinding
import com.bumptech.glide.Glide

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEditProfileBinding
    private val viewModel by viewModels<EditProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }
    // CEK PERMISSION IMAGE
    private var currentImageUri: Uri? = null

    private val requestPermissLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, getString(R.string.permission_request_granted), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, getString(R.string.permission_request_denied), Toast.LENGTH_SHORT).show()
        }
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSION) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.btnChangePhotoProfile.setOnClickListener {
            selectImage()
        }

        binding.btnSave.setOnClickListener {

//            Log.d("DataGambar", "onCreate: $currentImageUri ")
        }


        viewModel.getSession().observe(this) { user ->
            Log.d("user", "onCreate: $user")
            fillUserProfileData(user)
        }

    }

    private fun fillUserProfileData(user: UserModel) {
        // Gunakan data pengguna untuk mengisi input field pada tampilan
        binding.tiNameRegister.editText?.setText(user.name)
        binding.tiEmailRegister.editText?.setText(user.email)
        binding.tiNumberTel.editText?.setText(user.phone)
//        currentImageUri = user?.photoProfileUrl?.toUri()
        if (user.avatar.isEmpty()) {
            Glide.with(this).load(user.avatar).into(binding.ivProfile)
        } else {
            binding.ivProfile.setImageResource(R.drawable.image_user_default)
        }
        val genderOptions = arrayOf(getString(R.string.male), getString(R.string.female))

        // Atur adapter untuk AutoCompleteTextView
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, genderOptions)
        binding.autoCompleteTextView.setAdapter(adapter)

        // Periksa apakah data gender pengguna ada di dalam daftar opsi
        user.gender?.let { userGender ->
            val selectedPosition = genderOptions.indexOf(userGender)
            if (selectedPosition != -1) {
                // Pilih opsi gender yang sesuai dengan data pengguna
                binding.autoCompleteTextView.setText(genderOptions[selectedPosition], false)
            }
        }
    }





    private fun selectImage() {
        val optionActions = arrayOf<CharSequence>(getString(R.string.take_photo),
            getString(R.string.gallery), getString(R.string.cancel))
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle(getString(R.string.choose_from))
        dialogBuilder.setIcon(R.mipmap.ic_launcher)
        dialogBuilder.setItems(optionActions) { dialogInterface, i ->
            when(i) {
                0 -> {
                    startCameraX()
                }
                1 -> {
                    startGallery()
                }
                2 -> {
                    dialogInterface.dismiss()
                }
            }
        }
        dialogBuilder.show()
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }
    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI" , "showImage: $it")
            binding.ivProfile.setImageURI(it)
        }
    }

    companion object {
        private  const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }


}