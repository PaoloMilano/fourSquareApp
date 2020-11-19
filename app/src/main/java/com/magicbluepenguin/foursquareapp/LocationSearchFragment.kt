package com.magicbluepenguin.foursquareapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.magicbluepenguin.foursquareapp.databinding.FragmentLocationSearchBinding
import com.magicbluepenguin.utils.extensions.setSupportActionBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class LocationSearchFragment : Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentLocationSearchBinding? = null

    private val viewModel by viewModels<LocationSearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocationSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.location_search_menu, menu)
        (menu.findItem(R.id.search).actionView as SearchView).apply {

            // Slight hack to ensure that the SearchView takes up the entire width of the toolbar
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