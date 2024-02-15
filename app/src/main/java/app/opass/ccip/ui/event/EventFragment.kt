package app.opass.ccip.ui.event

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import app.opass.ccip.R
import app.opass.ccip.databinding.FragmentEventBinding
import app.opass.ccip.util.PreferenceUtil
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class EventFragment : Fragment(R.layout.fragment_event), EventAdapter.EventClickListener {

    private val TAG = EventFragment::class.java.simpleName

    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EventViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEventBinding.bind(view)

        binding.retry.setOnClickListener { viewModel.getEvents() }
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.refresh -> {
                    viewModel.getEvents()
                    Toast.makeText(requireContext(), R.string.refreshing, Toast.LENGTH_SHORT).show()
                }
                else -> Log.i(TAG, "No action for ${it.itemId}")
            }
            true
        }

        val adapter = EventAdapter(this)
        binding.events.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.events.collect {
                if (it != null) {
                    binding.events.visibility = View.VISIBLE
                    binding.noNetwork.visibility = View.GONE
                    adapter.submitList(it)
                } else {
                    binding.events.visibility = View.GONE
                    binding.noNetwork.visibility = View.VISIBLE
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.eventConfig.filterNotNull().collect {
                PreferenceUtil.setCurrentEvent(requireContext(), it)
                findNavController().navigate(
                    if (findNavController().currentDestination?.id == R.id.eventSheetFragment) {
                        EventSheetFragmentDirections.actionEventSheetFragmentToMainFragment()
                    } else {
                        EventFragmentDirections.actionEventFragmentToMainFragment()
                    }
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onEventClicked(eventId: String) {
        viewModel.fetchEventConfig(eventId)
    }
}
