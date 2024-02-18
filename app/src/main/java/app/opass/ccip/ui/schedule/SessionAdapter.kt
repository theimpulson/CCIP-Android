package app.opass.ccip.ui.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.opass.ccip.R
import app.opass.ccip.databinding.ItemSessionBinding
import app.opass.ccip.model.Session
import java.text.SimpleDateFormat
import java.util.*

class SessionAdapter(private val listener: SessionClickListener) :
    ListAdapter<Session, SessionAdapter.ViewHolder>(SessionDiffUtil()) {

    interface SessionClickListener {
        fun onSessionClicked(session: Session)
    }

    class SessionDiffUtil : DiffUtil.ItemCallback<Session>() {

        override fun areItemsTheSame(oldItem: Session, newItem: Session): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Session, newItem: Session): Boolean {
            return when {
                oldItem.id != newItem.id -> false
                oldItem.start != newItem.start -> false
                oldItem.end != newItem.end -> false
                oldItem.room != newItem.room -> false
                oldItem.tags != newItem.tags -> false
                oldItem.speakers != newItem.speakers -> false
                oldItem.type != newItem.type -> false
                else -> true
            }
        }
    }

    inner class ViewHolder(val binding: ItemSessionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSessionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val session = getItem(position)
        val context = holder.binding.root.context
        val formattedSDF = SimpleDateFormat("HH:mm", Locale.getDefault())

        holder.binding.apply {
            root.setOnClickListener { listener.onSessionClicked(session) }

            room.text = session.room.getDetails(context).name
            title.text = session.getSessionDetail(context).title
            time.text = context.getString(
                R.string.formatted_time,
                formattedSDF.format(session.startLong),
                formattedSDF.format(session.endLong)
            )
        }
    }
}
