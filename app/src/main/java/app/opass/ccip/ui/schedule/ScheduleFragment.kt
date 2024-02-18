package app.opass.ccip.ui.schedule

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import app.opass.ccip.R
import app.opass.ccip.databinding.FragmentScheduleBinding
import app.opass.ccip.model.Session
import app.opass.ccip.ui.sessiondetail.SessionDetailActivity
import kotlinx.coroutines.launch

class ScheduleFragment(private val date: String) : Fragment(R.layout.fragment_schedule),
    SessionAdapter.SessionClickListener {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ScheduleViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentScheduleBinding.bind(view)

        val adapter = SessionAdapter(this)
        binding.recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.sessions.collect {
                adapter.submitList(it[date])
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSessionClicked(session: Session) {
        val intent = Intent(requireContext(), SessionDetailActivity::class.java).apply {
            putExtra(SessionDetailActivity.INTENT_EXTRA_SESSION_ID, session.id)
        }
        startActivity(intent)
    }
}
