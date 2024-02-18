package app.opass.ccip.ui.schedule

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import app.opass.ccip.R
import app.opass.ccip.databinding.FragmentScheduleTabBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class ScheduleTabFragment : Fragment(R.layout.fragment_schedule_tab) {

    private var _binding: FragmentScheduleTabBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ScheduleViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentScheduleTabBinding.bind(view)

        binding.searchBar.apply {
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

        // Fetch schedule
        viewModel.loadConfSchedule(view.context)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.sessions.filter { it.isNotEmpty() }.collect {
                binding.viewPager.adapter = ScheduleTabAdapter(
                    childFragmentManager,
                    viewLifecycleOwner.lifecycle,
                    it.keys.toList()
                )

                TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                    tab.text = it.keys.toList()[position]
                }.attach()

                if (it.size != 1) {
                    binding.tabLayout.visibility = View.VISIBLE
                } else {
                    binding.viewPager.isUserInputEnabled = false
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
