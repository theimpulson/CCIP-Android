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
            binding.logo.load(currentEvent.logoUrl)
            binding.toolbar.apply {
                title = currentEvent.displayName.findBestMatch(requireContext())
                setNavigationOnClickListener {
                    findNavController().navigate(R.id.eventSheetFragment)
                }
            }

            binding.recyclerView.apply {
                val featureAdapter = FeatureAdapter(this@MainFragment)
                layoutManager = object : GridLayoutManager(requireContext(), 4) {
                    override fun canScrollVertically(): Boolean {
                        return false
                    }
                }
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

            FeatureType.WIFI -> {
                if (!feature.wifiNetworks.isNullOrEmpty()) {
                    findNavController().navigate(
                        MainFragmentDirections.actionMainFragmentToWiFiNetworkFragment(
                            feature.wifiNetworks.toTypedArray()
                        )
                    )
                }
            }

            else -> customTabsIntent.launchUrl(requireContext(), Uri.parse(feature.url))
        }
    }
}
