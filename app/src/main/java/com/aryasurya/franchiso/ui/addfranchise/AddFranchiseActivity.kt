package com.aryasurya.franchiso.ui.addfranchise

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.aryasurya.franchiso.R
import com.aryasurya.franchiso.data.dataclass.FranchiseItem
import com.aryasurya.franchiso.databinding.ActivityAddFranchiseBinding
import com.aryasurya.franchiso.ui.addfranchise.addtype.AddTypeActivity

class AddFranchiseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddFranchiseBinding
    private lateinit var adapter: TypeFranchiseAdapter
    private val ADD_TYPE_REQUEST_CODE = 100
    private val EDIT_ITEM_REQUEST_CODE = 101
    private var editedItemPosition: Int = -1 // Untuk menyimpan posisi item yang diedit

    private val REQUEST_IMAGE_PICK = 2
    private val selectedImages = mutableListOf<Uri>()
    private lateinit var imageAdapter: ImageAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFranchiseBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    private fun addImageToList(uri: Uri) {
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
                    data?.getParcelableExtra<FranchiseItem>(AddTypeActivity.EXTRA_TYPE_ITEM)?.let { newTypeItem ->
                        adapter.addItem(newTypeItem)
                    }
                }
                EDIT_ITEM_REQUEST_CODE -> {
                    data?.getParcelableExtra<FranchiseItem>(AddTypeActivity.EXTRA_TYPE_ITEM)?.let { editedItem ->
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
}
