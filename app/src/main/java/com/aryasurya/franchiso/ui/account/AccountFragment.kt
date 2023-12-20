package com.aryasurya.franchiso.ui.account

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import com.aryasurya.franchiso.R
import com.aryasurya.franchiso.ViewModelFactory
import com.aryasurya.franchiso.databinding.FragmentAccountBinding
import com.bumptech.glide.Glide

class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var photoProfile: ImageView
    private val viewModel by viewModels<AccountViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Settings Fragment
        val preferenceFragment = MyPreferenceFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.settings, preferenceFragment)
            .commit()

        progressDialog = ProgressDialog(requireContext())
        photoProfile = binding.ivProfile

        viewModel.getSession().observe(requireActivity()) { user ->
            Log.d(TAG, "onViewCreated: $user")
            binding.tvNameProfile.text = user.role
        }

    }

    override fun onResume() {
        super.onResume()
    }


    companion object {
        private const val TAG = "AccountFragment"
    }
}

