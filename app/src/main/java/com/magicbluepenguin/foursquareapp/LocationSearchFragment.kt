package com.magicbluepenguin.foursquareapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.magicbluepenguin.foursquareapp.databinding.FragmentLocationSearchBinding
import com.magicbluepenguin.foursquareapp.extensions.setSupportActionBar


class LocationSearchFragment : Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentLocationSearchBinding? = null
    private var locquery: String? = null

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
            maxWidth = Integer.MAX_VALUE

            setIconifiedByDefault(false)
            setQuery(locquery, true)

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    clearFocus()
                    locquery = query
                    findNavController().navigate(R.id.show_detail)
                    return true
                }

                override fun onQueryTextChange(newText: String?) = true

            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}