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
import com.magicbluepenguin.utils.extensions.isNetworkAvailable
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
    ): View {
        _binding = FragmentVenueDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.appBar.toolbar) {
            setSupportActionBar(this).apply {
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowTitleEnabled(false)
                setNavigationOnClickListener { requireActivity().onBackPressed() }
            }
        }

        fun showVenueDetails(venueDetail: VenueDetail) {
            binding.appBar.toolbar.title = venueDetail.name
            binding.venuePhoneNumber.text = venueDetail.formattedPhoneNumber
            binding.venueAddress.text = venueDetail.address
            binding.venueRating.text = requireActivity().resources?.getString(R.string.venue_rating_sf, venueDetail.rating)
            binding.venuePhotoPager.adapter = VenuePhotosAdapter(venueDetail.photos)
        }

        viewModel.searchInProgressLiveData.observe(viewLifecycleOwner) {
            binding.detailProgress.visibility = if (it) {
                hideErrors()
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        viewModel.venuesLiveData.observe(viewLifecycleOwner) {
            hideErrors()
            when (it) {
                is SuccessResponse -> it.data
                is ErrorResponse -> {
                    handleErrorResponse(it)
                    it.data
                }
            }?.let {
                showVenueDetails(it)
            }
        }
        viewModel.fetchDetailsForVenue(args.venueId)
    }

    private fun handleErrorResponse(errorResponse: ErrorResponse<VenueDetail?>) {
        if (!requireContext().isNetworkAvailable()) {
            if (errorResponse.data != null) {
                binding.networkErrorText.setText(R.string.venue_detail_internet_connection_error_with_result)
            } else {
                binding.networkErrorText.setText(R.string.venue_detail_internet_connection_error)
            }
            showNetworkError()
            doOnNetworkAvailable {
                viewModel.fetchDetailsForVenue(args.venueId)
            }
        } else {
            if (errorResponse.data == null) {
                showGenericError()
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

    private fun showGenericError() {
        binding.networkErrorText.visibility = View.GONE
        binding.noResultsText.visibility = View.VISIBLE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}