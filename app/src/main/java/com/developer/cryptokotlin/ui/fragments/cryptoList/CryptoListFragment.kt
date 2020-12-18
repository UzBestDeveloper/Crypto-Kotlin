package com.developer.cryptokotlin.ui.fragments.cryptoList

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.developer.cryptokotlin.MyApplication
import com.developer.cryptokotlin.utils.assetListScrollListener.ProgressStatus
import com.developer.cryptokotlin.R
import com.developer.cryptokotlin.adapters.AssetClickListener
import com.developer.cryptokotlin.adapters.AssetsAdapter
import com.developer.cryptokotlin.connect.models.AssetObject
import com.developer.cryptokotlin.connect.services.ApiService
import com.developer.cryptokotlin.databinding.FragmentCryptoListBinding
import com.developer.cryptokotlin.utils.AssetContract
import javax.inject.Inject


class CryptoListFragment : Fragment() {

    @Inject
    lateinit var apiService: ApiService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentCryptoListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_crypto_list, container, false)
        initView(binding)
        return binding.root
    }

    private fun initView(binding: FragmentCryptoListBinding) {
        val cryptoListViewModel = initViewModel(binding)
        recyclerViewInit(binding, cryptoListViewModel)
    }

    private fun initViewModel(binding: FragmentCryptoListBinding): CryptoListViewModel {
        val repository = CryptoListRepository(apiService) as AssetContract.Repository
        val cryptoListViewModelFactory = CryptoListViewModelFactory(repository)
        val cryptoListViewModel = ViewModelProvider(this, cryptoListViewModelFactory).get(
            CryptoListViewModel::class.java
        )

        binding.cryptoListViewModel = cryptoListViewModel
        return cryptoListViewModel
    }

    private fun recyclerViewInit(binding: FragmentCryptoListBinding, cryptoListViewModel: CryptoListViewModel) {
        val adapter = AssetsAdapter(AssetClickListener { navigateAssetDetails(it) })
        binding.recyclerView.adapter = adapter

        cryptoListViewModel.getAssets().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                adapter.submitList(it)
            }
        })

        cryptoListViewModel.getProgressStatus().observe(viewLifecycleOwner, Observer {
            when (it) {
                is ProgressStatus.Loading -> displayLoading(binding)
                is ProgressStatus.Success -> hideLoading(binding)
                is ProgressStatus.Error -> hideLoading(binding)
            }
        })
    }

    private fun hideLoading(binding: FragmentCryptoListBinding) {
        binding.progressBar.visibility = View.GONE
    }

    private fun displayLoading(binding: FragmentCryptoListBinding) {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun navigateAssetDetails(it: AssetObject) {
        val bundle = bundleOf("asset" to it)
        findNavController(requireParentFragment()).navigate(
            R.id.action_cryptoListFragment_to_assetDetailsFragment,
            bundle
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as MyApplication).appComponent.fragmentComponent()
            .create().inject(this)
    }

}