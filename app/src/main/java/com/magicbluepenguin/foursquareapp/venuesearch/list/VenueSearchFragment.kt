package com.magicbluepenguin.foursquareapp.venuesearch.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.magicbluepenguin.foursquareapp.R
import com.magicbluepenguin.foursquareapp.databinding.FragmentVenueSearchBinding
import com.magicbluepenguin.repository.model.VenueListItem
import com.magicbluepenguin.repository.repositories.ErrorResponse
import com.magicbluepenguin.repository.repositories.SuccessResponse
import com.magicbluepenguin.utils.extensions.doOnNetworkAvailable
import com.magicbluepenguin.utils.extensions.isNetworkAvailable
import com.magicbluepenguin.utils.extensions.setSupportActionBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class VenueSearchFragment : Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentVenueSearchBinding? = null

    private val viewModel by viewModels<VenueSearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVenueSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        setSupportActionBar(binding.appBar.toolbar).apply {
            setDisplayShowTitleEnabled(false)
        }

        val venueListAdapter = VenueListAdapter {
            VenueSearchFragmentDirections.showDetail(venueId = it).let {
                findNavController().navigate(it)
            }
        }

        binding.searchResultsRecyclerView.apply {
            adapter = venueListAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL).apply {
                ContextCompat.getDrawable(requireActivity(), R.drawable.recyclervirew_divider)
                    ?.let {
                        setDrawable(it)
                    }
            })
        }
        viewModel.searchInProgressLiveData.observe(viewLifecycleOwner) {
            binding.searchProgress.visibility = if (it) {
                hideErrors()
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        viewModel.venuesLiveData.observe(viewLifecycleOwner) {

            hideErrors()

            when (it) {
                is SuccessResponse -> {
                    handleSuccessResponse(it)
                    it.data
                }
                is ErrorResponse -> {
                    handleErrorResponse(it)
                    it.data
                }
            }?.let {
                venueListAdapter.updateData(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.location_search_menu, menu)
        (menu.findItem(R.id.search).actionView as SearchView).apply {

            // Known hack to ensure that the SearchView takes up the entire width of the toolbar
            maxWidth = Integer.MAX_VALUE

            setIconifiedByDefault(false)
            setQuery(viewModel.searchQuery, true)

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    clearFocus()
                    viewModel.submitSearch()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.searchQuery = query?.toString()
                    (binding.searchResultsRecyclerView.adapter as VenueListAdapter).updateData(emptyList())
                    return true
                }
            })
        }
    }

    private fun handleSuccessResponse(successResponse: SuccessResponse<List<VenueListItem>>) {
        if (successResponse.data.isNullOrEmpty()) {
            showNoResultsError()
        }
    }

    private fun handleErrorResponse(errorResponse: ErrorResponse<List<VenueListItem>>) {
        if (!requireContext().isNetworkAvailable()) {
            if (errorResponse.data.isNullOrEmpty()) {
                binding.networkErrorText.setText(R.string.venue_search_internet_connection_error)
            } else {
                binding.networkErrorText.setText(R.string.venue_search_internet_connection_error_with_results)
            }
            showNetworkError()
            doOnNetworkAvailable {
                viewModel.submitSearch()
            }
        } else {
            if (errorResponse.data.isNullOrEmpty()) {
                showNoResultsError()
            }
        }
    }

    private fun hideErrors() {
        binding.networkErrorText.visibility = View.GONE
        binding.noResultsText.visibility = View.GONE
    }

    private fun showNetworkError() {
        binding.networkErrorText.visibility = View.VISIBLE
        binding.noResultsText.visibility = View.GONE
    }

    private fun showNoResultsError() {
        binding.networkErrorText.visibility = View.GONE
        binding.noResultsText.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}