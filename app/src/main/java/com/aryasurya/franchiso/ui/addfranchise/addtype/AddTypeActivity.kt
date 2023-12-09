package com.aryasurya.franchiso.ui.addfranchise.addtype

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.aryasurya.franchiso.R
import com.aryasurya.franchiso.data.entity.FranchiseItem
import com.aryasurya.franchiso.databinding.ActivityAddTypeBinding

class AddTypeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTypeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val items = listOf("Store", "Stand", "Mini market")
        val adapter = ArrayAdapter(this@AddTypeActivity, R.layout.text_type_franchise, items)
        binding.autoCompleteTextView.setAdapter(adapter)


        binding.btnSaveType.setOnClickListener {
            saveType()
        }
    }

    private fun saveType() {
        val type = binding.autoCompleteTextView.text.toString().trim()
        val facility = binding.tlFacility.editText?.text.toString().trim()
        val price = binding.tlPrice.editText?.text.toString().trim()

        if (type.isNotEmpty() && facility.isNotEmpty() && price.isNotEmpty()) {
            val newTypeItem = FranchiseItem(type, facility, price)
            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_TYPE_ITEM, newTypeItem)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        } else {
            // Handle when fields are empty
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val EXTRA_TYPE_ITEM = "extra_type_item"
    }
}