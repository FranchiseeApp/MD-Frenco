package com.aryasurya.franchiso.ui.addfranchise

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aryasurya.franchiso.R
import com.aryasurya.franchiso.data.entity.FranchiseItem
import com.aryasurya.franchiso.databinding.ItemTypeFranchiseInputBinding

class TypeFranchiseAdapter(
    private val items: MutableList<FranchiseItem>,
    private val onItemClick: (Int) -> Unit,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<TypeFranchiseAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemTypeFranchiseInputBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick(adapterPosition)
            }

            // Jika ada tombol delete di dalam tampilan item, atur listener-nya di sini
            // binding.btnDelete.setOnClickListener { onDeleteClick(adapterPosition) }
        }

        fun bind(item: FranchiseItem) {
            binding.apply {
                inputTypeFranchise.text = item.type
                inputFacilityFranchise.text = item.facility
                inputPriceFranchise.text = item.price
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(item: FranchiseItem) {
        items.add(item)
        notifyDataSetChanged() // Inform adapter that dataset changed
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTypeFranchiseInputBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
