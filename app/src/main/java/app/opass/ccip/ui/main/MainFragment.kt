package app.opass.ccip.ui.main

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import app.opass.ccip.R
import app.opass.ccip.databinding.FragmentMainBinding
import app.opass.ccip.model.EventConfig
import app.opass.ccip.model.Feature
import app.opass.ccip.model.FeatureType
import app.opass.ccip.util.PreferenceUtil
import coil.load

class MainFragment : Fragment(R.layout.fragment_main), FeatureAdapter.FeatureClickListener {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val currentEvent: EventConfig
        get() = PreferenceUtil.getCurrentEvent(requireContext())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainBinding.bind(view)

        // Ask user to select an event
        if (currentEvent.eventId.isBlank()) {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToEventFragment())
        } else {
            binding.toolbar.title = currentEvent.displayName.findBestMatch(requireContext())
            binding.logo.load(currentEvent.logoUrl)

            binding.recyclerView.apply {
                val featureAdapter = FeatureAdapter(this@MainFragment)
                layoutManager = GridLayoutManager(requireContext(), 4)
                adapter = featureAdapter
                featureAdapter.submitList(currentEvent.features)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onFeatureClicked(feature: Feature) {
        val customTabsIntent = CustomTabsIntent.Builder()
            .build()

        when (feature.feature) {

            FeatureType.PUZZLE -> {}

            FeatureType.SCHEDULE -> {}

            FeatureType.TICKET -> {}

            FeatureType.WIFI -> {}

            else -> customTabsIntent.launchUrl(requireContext(), Uri.parse(feature.url))
        }
    }
}
