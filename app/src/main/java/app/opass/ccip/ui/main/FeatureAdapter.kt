package app.opass.ccip.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.opass.ccip.R
import app.opass.ccip.databinding.ItemFeatureBinding
import app.opass.ccip.model.Feature
import app.opass.ccip.model.FeatureType
import coil.load

class FeatureAdapter(private val listener: FeatureClickListener) :
    ListAdapter<Feature, FeatureAdapter.ViewHolder>(FeatureDiffUtil()) {

    interface FeatureClickListener {
        fun onFeatureClicked(feature: Feature)
    }

    class FeatureDiffUtil : DiffUtil.ItemCallback<Feature>() {
        override fun areItemsTheSame(oldItem: Feature, newItem: Feature): Boolean {
            return oldItem.feature == newItem.feature
        }

        override fun areContentsTheSame(oldItem: Feature, newItem: Feature): Boolean {
            return when {
                oldItem.url != newItem.url -> false
                oldItem.feature != newItem.feature -> false
                oldItem.icon != newItem.icon -> false
                oldItem.displayText != newItem.displayText -> false
                oldItem.visibleRoles != newItem.visibleRoles -> false
                oldItem.wifiNetworks != newItem.wifiNetworks -> false
                else -> true
            }
        }
    }

    inner class ViewHolder(val binding: ItemFeatureBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureAdapter.ViewHolder {
        return ViewHolder(
            ItemFeatureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val feature = getItem(position)
        val context = holder.binding.root.context

        holder.binding.apply {
            root.setOnClickListener { listener.onFeatureClicked(feature) }

            when (feature.feature) {
                FeatureType.ANNOUNCEMENT -> {
                    logo.setImageResource(R.drawable.ic_announcement)
                    title.text = context.getString(R.string.announcement)
                }

                FeatureType.FAST_PASS -> {
                    logo.setImageResource(R.mipmap.ic_launcher_foreground)
                    title.text = context.getString(R.string.fast_pass)
                }

                FeatureType.IM -> {
                    logo.setImageResource(R.drawable.ic_im)
                    title.text = feature.displayText.findBestMatch(context)
                }

                FeatureType.PUZZLE -> {
                    logo.setImageResource(R.drawable.ic_puzzle)
                    title.text = context.getString(R.string.puzzle)
                }

                FeatureType.SCHEDULE -> {
                    logo.setImageResource(R.drawable.ic_schedule)
                    title.text = context.getString(R.string.schedule)
                }

                FeatureType.SPONSORS -> {
                    logo.setImageResource(R.drawable.ic_sponsor)
                    title.text = context.getString(R.string.sponsors)
                }

                FeatureType.STAFFS -> {
                    logo.setImageResource(R.drawable.ic_staff)
                    title.text = context.getString(R.string.staffs)
                }

                FeatureType.TELEGRAM -> {
                    logo.setImageResource(R.drawable.telegram_logo)
                    title.text = context.getString(R.string.telegram)
                }

                FeatureType.TICKET -> {
                    logo.setImageResource(R.drawable.ic_ticket)
                    title.text = context.getString(R.string.my_ticket)
                }

                FeatureType.VENUE -> {
                    logo.setImageResource(R.drawable.ic_venue)
                    title.text = context.getString(R.string.venue)
                }

                FeatureType.WEBVIEW -> {
                    logo.load(feature.icon)
                    title.text = feature.displayText.findBestMatch(context)
                }

                FeatureType.WIFI -> {
                    logo.setImageResource(R.drawable.ic_wifi)
                    title.text = context.getString(R.string.wifi)
                }
            }
        }
    }
}
