package com.aryasurya.franchiso.ui.addfranchise

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aryasurya.franchiso.R
import com.aryasurya.franchiso.data.entity.FranchiseItem
import com.aryasurya.franchiso.databinding.ActivityAddFranchiseBinding
import com.aryasurya.franchiso.ui.addfranchise.addtype.AddTypeActivity

class AddFranchiseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddFranchiseBinding
    private lateinit var adapter: TypeFranchiseAdapter
    private val ADD_TYPE_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFranchiseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi RecyclerView dan adapter
        val layoutManager = LinearLayoutManager(this)
        binding.rvType.layoutManager = layoutManager

        adapter = TypeFranchiseAdapter(mutableListOf(),
            onItemClick = { position ->
                // Lakukan logika saat item diklik di sini
                Toast.makeText(this, "Item clicked at position $position", Toast.LENGTH_SHORT).show()
            },
            onDeleteClick = { position ->
                // Lakukan logika saat tombol delete diklik di sini
                Toast.makeText(this, "Delete button clicked at position $position", Toast.LENGTH_SHORT).show()
            }
        )
        binding.rvType.adapter = adapter // Atur adapter ke RecyclerView

        val items = listOf("Food", "Drink")
        val autoCompleteAdapter = ArrayAdapter(this@AddFranchiseActivity, R.layout.text_type_franchise, items)
        binding.autoCompleteTextView.setAdapter(autoCompleteAdapter)

        binding.btnAddType.setOnClickListener {
            val intent = Intent(this, AddTypeActivity::class.java)
            startActivityForResult(intent, ADD_TYPE_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_TYPE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.getParcelableExtra<FranchiseItem>(AddTypeActivity.EXTRA_TYPE_ITEM)?.let { newTypeItem ->
                // Add the new item to the list and update RecyclerView
                adapter.addItem(newTypeItem)
            }
        }
    }


}