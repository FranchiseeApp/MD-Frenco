package com.aryasurya.franchiso.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aryasurya.franchiso.ViewModelFactory
import com.aryasurya.franchiso.data.Result
import com.aryasurya.franchiso.data.remote.response.GetMyFranchiseResponse
import com.aryasurya.franchiso.databinding.FragmentHomeBinding
import com.aryasurya.franchiso.ui.login.LoginActivity
import com.aryasurya.franchiso.ui.login.LoginViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private val userViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var adapter: FranchiseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvListStory.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


        userViewModel.getSession().observe(viewLifecycleOwner) {
            Log.d("userIdLogin", "login: ${it.token}")
        }

        // Memuat data pertama kali ketika fragment dimuat
        loadData()

    }
    override fun onResume() {
        super.onResume()
        // Memuat data setiap kali fragment di-resume
        loadData()
    }

    private fun loadData() {
        viewModel.getMyFranchise().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    // Handle loading state
                    binding.pbListFranchise.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.pbListFranchise.visibility = View.GONE

                    if (result.data.data.isNotEmpty()) {
                        // Tampilkan data di RecyclerView
                        val allFranchises = result.data.data
                        Log.d("TAG", "loadData: $allFranchises")
                        adapter = FranchiseAdapter(allFranchises)
                        binding.rvListStory.adapter = adapter
                    } else {
                        // Tampilkan pesan jika tidak ada data
//                        binding.tvNoData.visibility = View.VISIBLE
                    }
                }
                is Result.Error -> {
                    // Handle error state
                    binding.pbListFranchise.visibility = View.GONE
                }
                else -> {}
            }
        }
    }


}