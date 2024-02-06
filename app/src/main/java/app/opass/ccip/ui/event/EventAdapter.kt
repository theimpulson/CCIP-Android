package app.opass.ccip.ui.event

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.opass.ccip.databinding.ItemEventBinding
import app.opass.ccip.model.Event
import coil.load

class EventAdapter(private val listener: EventClickListener) :
    ListAdapter<Event, EventAdapter.ViewHolder>(EventDiffUtil()) {

    interface EventClickListener {
        fun onEventClicked(eventId: String)
    }

    class EventDiffUtil : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.eventId == newItem.eventId
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return when {
                oldItem.displayName != newItem.displayName -> false
                oldItem.eventId != newItem.eventId -> false
                oldItem.logoUrl != newItem.logoUrl -> false
                else -> true
            }
        }
    }

    inner class ViewHolder(val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: EventAdapter.ViewHolder, position: Int) {
        val event = getItem(position)

        holder.binding.apply {
            eventName.text = event.displayName.findBestMatch(root.context)
            eventLogo.load(event.logoUrl)
            root.setOnClickListener { listener.onEventClicked(event.eventId) }
        }
    }
}
