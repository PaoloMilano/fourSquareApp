package com.magicbluepenguin.foursquareapp.venuesearch.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.magicbluepenguin.foursquareapp.databinding.FragmentVenueDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class VenueDetailFragment : Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentVenueDetailBinding? = null

    val args: VenueDetailFragmentArgs by navArgs()
    val viewModel: VenueDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVenueDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.venuesLiveData.observe(viewLifecycleOwner) {
            System.currentTimeMillis()
        }
        viewModel.fetchDetailsForVenue(args.venueId)
    }
}