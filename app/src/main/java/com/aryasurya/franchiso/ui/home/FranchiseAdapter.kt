package com.aryasurya.franchiso.ui.home

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aryasurya.franchiso.data.remote.response.DataItem3
import com.aryasurya.franchiso.databinding.StoryItemBinding
import com.aryasurya.franchiso.ui.detail.DetailActivity
import com.bumptech.glide.Glide

class FranchiseAdapter(private val franchiseList: List<DataItem3>) :
    RecyclerView.Adapter<FranchiseAdapter.FranchiseViewHolder>() {

    inner class FranchiseViewHolder(val binding: StoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(franchiseData: DataItem3) {
            // Bind data ke elemen UI dalam item franschise_card menggunakan ViewBinding
            binding.tvNameFranchises.text = franchiseData.franchiseName
            binding.tvCategoryFranchise.text = franchiseData.category
            binding.tvDescStory.text = franchiseData.description

            Log.d("Gambarran", "bind: ${franchiseData.gallery.firstOrNull()?.image} ")
            // Tambahkan logika untuk menampilkan gambar jika ada
            // franchiseData.images berisi URI gambar yang diunggah ke Firebase Storage
            // Misalnya:
             Glide.with(binding.root.context).load(franchiseData.gallery.first().image).into(binding.ivFranchise)
//            Glide.with(binding.ivFranchise.context)
//                .load(franchiseData.gallery.firstOrNull()) // Ganti dengan properti yang sesuai dari galeri
//                .into(binding.ivFranchise)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FranchiseViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FranchiseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FranchiseViewHolder, position: Int) {
        val franchiseData = franchiseList[position]
//        val firstImageUri = franchiseData.images.firstOrNull()
        holder.bind(franchiseData)
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            // Mengirim ID dokumen yang dipilih ke halaman detail menggunakan Intent
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("franchiseId", franchiseData.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return franchiseList.size
    }
}
