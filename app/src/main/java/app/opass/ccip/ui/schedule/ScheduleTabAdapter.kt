package app.opass.ccip.ui.schedule

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ScheduleTabAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val dates: List<String>
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return dates.size
    }

    override fun createFragment(position: Int): Fragment {
        return ScheduleFragment(dates[position])
    }

}
