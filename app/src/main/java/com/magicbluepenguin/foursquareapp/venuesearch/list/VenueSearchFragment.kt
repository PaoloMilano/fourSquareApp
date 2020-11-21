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

        viewModel.venuesLiveData.observe(viewLifecycleOwner) {
            venueListAdapter.updateData(it)
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
                    return true
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}