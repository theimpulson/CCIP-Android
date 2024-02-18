package app.opass.ccip.ui.schedule

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.opass.ccip.extension.asyncExecute
import app.opass.ccip.model.ConfSchedule
import app.opass.ccip.model.FeatureType
import app.opass.ccip.model.Session
import app.opass.ccip.util.JsonUtil
import app.opass.ccip.util.PreferenceUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.text.SimpleDateFormat
import java.util.Locale

class ScheduleViewModel : ViewModel() {

    private val TAG = ScheduleViewModel::class.java.simpleName

    private val _confSchedule = MutableStateFlow<ConfSchedule?>(null)
    val confSchedule = _confSchedule.asStateFlow()

    private val _sessions = MutableStateFlow<Map<String, List<Session>>>(emptyMap())
    val sessions = _sessions.asStateFlow()

    fun loadConfSchedule(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val schedule = PreferenceUtil.loadSchedule(context)
            if (schedule != null) {
                _confSchedule.value = schedule
                loadSessions(schedule.sessions.sortedBy { it.startLong })
            } else {
                fetchConfSchedule(context)
            }
        }
    }

    private fun loadSessions(sessions: List<Session>) {
        val formattedSDF = SimpleDateFormat("E dd", Locale.getDefault())
        _sessions.value = sessions.groupBy { formattedSDF.format(it.startLong) }
    }

    private suspend fun fetchConfSchedule(context: Context) {
        withContext(Dispatchers.IO) {
            try {
                val feature = PreferenceUtil.getCurrentEvent(context).features
                    .find { it.feature == FeatureType.SCHEDULE }
                val request = Request.Builder().url(feature?.url!!).build()
                val schedule = OkHttpClient().newCall(request).asyncExecute().body!!.string().trim()

                val confSchedule = JsonUtil.GSON.fromJson(schedule, ConfSchedule::class.java)
                PreferenceUtil.saveSchedule(context, schedule)

                _confSchedule.value = confSchedule
                loadSessions(confSchedule.sessions.sortedBy { it.startLong })
            } catch (exception: Exception) {
                Log.e(TAG, "Failed to fetch schedules", exception)
            }
        }
    }
}
