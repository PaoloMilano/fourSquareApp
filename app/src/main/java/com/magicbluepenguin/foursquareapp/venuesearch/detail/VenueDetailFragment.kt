package com.magicbluepenguin.foursquareapp.venuesearch.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.magicbluepenguin.foursquareapp.R
import com.magicbluepenguin.foursquareapp.databinding.FragmentVenueDetailBinding
import com.magicbluepenguin.repository.model.VenueDetail
import com.magicbluepenguin.repository.repositories.ErrorResponse
import com.magicbluepenguin.repository.repositories.SuccessResponse
import com.magicbluepenguin.utils.extensions.doOnNetworkAvailable
import com.magicbluepenguin.utils.extensions.setSupportActionBar
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

        with(binding.appBar.toolbar) {
            setNavigationOnClickListener { requireActivity().onBackPressed() }
            setSupportActionBar(this).apply {
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowTitleEnabled(false)
            }
        }

        fun showVenueDetails(venueDetail: VenueDetail) {
            binding.appBar.toolbar.title = venueDetail.name
            binding.venuePhoneNumber.text = venueDetail.formattedPhoneNumber
            binding.venueAddress.text = venueDetail.address
            binding.venueRating.text = requireActivity().resources?.getString(R.string.venue_rating_sf, venueDetail.rating)
            binding.venuePhotoPager.adapter = VenuePhotosAdapter(venueDetail.photos)
        }

        viewModel.venuesLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is SuccessResponse -> it.data
                is ErrorResponse -> {
                    doOnNetworkAvailable {
                        viewModel.fetchDetailsForVenue(args.venueId)
                    }
                    it.data
                }
            }?.let {
                showVenueDetails(it)
            }
        }
        viewModel.fetchDetailsForVenue(args.venueId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}