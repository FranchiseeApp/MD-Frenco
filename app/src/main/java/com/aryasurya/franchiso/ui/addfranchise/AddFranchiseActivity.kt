package com.aryasurya.franchiso.ui.addfranchise

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aryasurya.franchiso.R
import com.aryasurya.franchiso.ViewModelFactory
import com.aryasurya.franchiso.data.Result
import com.aryasurya.franchiso.data.remote.request.FranchiseRequest
import com.aryasurya.franchiso.data.remote.request.FranchiseTypeRequest
import com.aryasurya.franchiso.databinding.ActivityAddFranchiseBinding
import com.aryasurya.franchiso.ui.addfranchise.addtype.AddTypeActivity
import com.aryasurya.franchiso.ui.home.HomeFragment
import com.aryasurya.franchiso.utils.createCustomTempFile
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream

class AddFranchiseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddFranchiseBinding
    private lateinit var adapter: TypeFranchiseAdapter
    private val ADD_TYPE_REQUEST_CODE = 100
    private val EDIT_ITEM_REQUEST_CODE = 101
    private var editedItemPosition: Int = -1 // Untuk menyimpan posisi item yang diedit

    private val REQUEST_IMAGE_PICK = 2
    private val selectedImages = mutableListOf<Uri>()
    private val imageParts = mutableListOf<MultipartBody.Part>()
    private lateinit var imageAdapter: ImageAdapter

    private var franchiseId: String? = null


    private val viewModel by viewModels<AddFranchiseViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFranchiseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        franchiseId = intent.getStringExtra("franchiseId")

        franchiseId?.let { id ->
            // Jika franchiseId tidak null, akses data dari Firebase
//            fetchDataFromFirebase(id)

        }

        Log.d("AddFranchiseActivity", "fId:  $franchiseId")

        val items = listOf("Food", "Drink")
        val adapterCategory = ArrayAdapter(this@AddFranchiseActivity, R.layout.text_type_franchise, items)
        binding.autoCompleteTextView.setAdapter(adapterCategory)

        // Inisialisasi RecyclerView dan adapter
        val layoutManager = LinearLayoutManager(this)
        binding.rvType.layoutManager = layoutManager

        adapter = TypeFranchiseAdapter(mutableListOf(),
            onItemClick = { position ->
                showPopupMenu(position, binding.rvType.getChildAt(position))
            },
            onDeleteClick = { position ->
                deleteItem(position)
            }
        )
        binding.rvType.adapter = adapter // Atur adapter ke RecyclerView

        binding.btnAddType.setOnClickListener {
            val intent = Intent(this, AddTypeActivity::class.java)
            startActivityForResult(intent, ADD_TYPE_REQUEST_CODE)
        }

        imageAdapter = ImageAdapter(selectedImages)
        binding.rvImageFranchise.adapter = imageAdapter

        val imageLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvImageFranchise.layoutManager = imageLayoutManager

        binding.btnAddImg.setOnClickListener {
            showImagePickerDialog()
        }

        binding.btnUploadDataFranchise.setOnClickListener {
//            binding.overlayLoading.visibility = View.VISIBLE

            // Panggil fungsi untuk mengumpulkan data dari inputan
            val franchiseName = binding.tlName.editText?.text.toString()
            val franchiseAddress = binding.tlAddress.editText?.text.toString()
            val franchiseDescription = binding.tlDesc.editText?.text.toString()
            val franchiseCategory = binding.autoCompleteTextView.text.toString()
            val franchisePhoneNumber = binding.tlWa.editText?.text.toString()
            // Dapatkan daftar tipe franchisenya dari adapter
            val franchiseTypes = adapter.getItems()

            if (franchiseTypes.isNotEmpty()) {
                val textColorPrimary = getColorFromAttribute(android.R.attr.textColorPrimary)
                binding.tvSecType.setTextColor(textColorPrimary)

                val newFranchise = FranchiseRequest(
                    franchiseName,
                    franchiseAddress,
                    franchiseDescription,
                    franchiseCategory,
                    franchisePhoneNumber,
                    franchiseTypes
                )

                viewModel.createFranchise(newFranchise)

            } else {
                binding.overlayLoading.visibility = View.GONE
                binding.tvSecType.setTextColor(Color.RED)
                Toast.makeText(this, "Please add at least one Franchise Type", Toast.LENGTH_SHORT).show()
            }
        }

        // ADD FRANCHISE OBS
        viewModel.franchiseResult.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.overlayLoading.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    val result = result.data.data
                    val id = result.id.toString()
                    franchiseId = id
                    viewModel.uploadImages(id, imageParts)

                }

                is Result.Error -> {
                    binding.overlayLoading.visibility = View.GONE
                    Toast.makeText(this, "Upload data franchise eror", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.uploadResult.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.overlayLoading.visibility = View.GONE
                }

                is Result.Success -> {
                    binding.overlayLoading.visibility = View.GONE
                    Toast.makeText(this, "Upload data Gambar successful", Toast.LENGTH_SHORT).show()
                    finish()

                }

                is Result.Error -> {
                    binding.overlayLoading.visibility = View.GONE
                }
            }
        }

    }




    private fun showImagePickerDialog() {
        val options = arrayOf("Gallery", "Cancel")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose image from")

        builder.setItems(options) { dialog, which ->
            when (options[which]) {
                "Gallery" -> pickImageFromGallery()
                "Cancel" -> dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun pickImageFromGallery() {
        val pickPhotoIntent = Intent(Intent.ACTION_GET_CONTENT)
        pickPhotoIntent.type = "image/*"
        pickPhotoIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) // Memungkinkan pemilihan multiple image
        startActivityForResult(Intent.createChooser(pickPhotoIntent, "Select Picture"), REQUEST_IMAGE_PICK)
    }

    private fun convertImageUriToFile(uri: Uri, context: Context): File {
        val contentResolver = context.contentResolver
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, filePathColumn, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
        val filePath = cursor?.getString(columnIndex ?: -1)
        cursor?.close()
        return File(filePath ?: "")
    }
    private fun convertFileToMultipart(file: File): MultipartBody.Part {
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData("gallery", file.name, requestFile)
    }


    private fun addImageToList(uri: Uri) {
        val imageFile = convertImageUriToFile(uri, this)
        val imagePart = convertFileToMultipart(imageFile)
        imageParts.add(imagePart)
        selectedImages.add(uri)
        val imageAdapter = ImageAdapter(selectedImages)
        binding.rvImageFranchise.adapter = imageAdapter
        imageAdapter.notifyDataSetChanged()
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                ADD_TYPE_REQUEST_CODE -> {
                    data?.getParcelableExtra<FranchiseTypeRequest>(AddTypeActivity.EXTRA_TYPE_ITEM)?.let { newTypeItem ->
                        adapter.addItem(newTypeItem)
                    }
                }
                EDIT_ITEM_REQUEST_CODE -> {
                    data?.getParcelableExtra<FranchiseTypeRequest>(AddTypeActivity.EXTRA_TYPE_ITEM)?.let { editedItem ->
                        adapter.updateItem(editedItemPosition, editedItem)
                    }
                }
                REQUEST_IMAGE_PICK -> {
                    if (data?.clipData != null) {
                        val clipData = data.clipData!!
                        for (i in 0 until clipData.itemCount) {
                            val imageUri = clipData.getItemAt(i).uri
                            addImageToList(imageUri)
                        }
                    } else if (data?.data != null) {
                        val imageUri = data.data
                        imageUri?.let {
                            addImageToList(it)
                        }
                    }
                }
            }
        }
    }

    private fun deleteItem(position: Int) {
        adapter.removeItem(position) // Hapus item dari adapter
    }

    private fun showPopupMenu(position: Int, view: View) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.menuInflater.inflate(R.menu.menu_item_options, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_edit -> {
                    editItem(position)
                    true
                }
                R.id.menu_delete -> {
                    deleteItem(position)
                    true
                }
                R.id.menu_cancel -> {
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun editItem(position: Int) {
        editedItemPosition = position // Simpan posisi item yang akan diedit
        val editedItem = adapter.getItem(position) // Ambil item yang ingin di-edit dari adapter
        val intent = Intent(this, AddTypeActivity::class.java)
        intent.putExtra(AddTypeActivity.EXTRA_TYPE_ITEM, editedItem)
        startActivityForResult(intent, EDIT_ITEM_REQUEST_CODE)
    }

    private fun Context.getColorFromAttribute(attr: Int): Int {
        val typedValue = TypedValue()
        val theme = theme
        theme.resolveAttribute(attr, typedValue, true)
        return typedValue.data
    }
}

