package com.aryasurya.franchiso.ui.addfranchise

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aryasurya.franchiso.data.remote.request.FranchiseTypeRequest
import com.aryasurya.franchiso.databinding.ItemTypeFranchiseInputBinding


class TypeFranchiseAdapter(
    private val items: MutableList<FranchiseTypeRequest>,
    private val onItemClick: (Int) -> Unit,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<TypeFranchiseAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemTypeFranchiseInputBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FranchiseTypeRequest) {
            binding.inputTypeFranchise.text = item.franchise_type
            binding.inputFacilityFranchise.text = item.facility
            binding.inputPriceFranchise.text = item.price

            binding.root.setOnClickListener {
                onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTypeFranchiseInputBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int = items.size

    fun addItem(item: FranchiseTypeRequest) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun updateItem(position: Int, item: FranchiseTypeRequest) {
        if (position != RecyclerView.NO_POSITION) {
            items[position] = item
            notifyItemChanged(position)
        }
    }

    fun removeItem(position: Int) {
        if (position != RecyclerView.NO_POSITION && position < items.size) {
            items.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, items.size)
        }
    }

    fun getItem(position: Int): FranchiseTypeRequest {
        return items[position]
    }
    fun getItems(): List<FranchiseTypeRequest> {
        return items.toList()
    }
}
