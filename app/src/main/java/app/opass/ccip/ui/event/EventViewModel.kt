package app.opass.ccip.ui.event

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.opass.ccip.extension.asyncExecute
import app.opass.ccip.model.Event
import app.opass.ccip.model.EventConfig
import app.opass.ccip.network.PortalClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EventViewModel : ViewModel() {

    private val TAG = EventViewModel::class.java.simpleName

    private val _events = MutableStateFlow<List<Event>?>(emptyList())
    val events = _events.asStateFlow()

    private val _eventConfig = MutableStateFlow<EventConfig?>(null)
    val eventConfig = _eventConfig.asStateFlow()

    init {
        getEvents()
    }

    fun getEvents() {
        viewModelScope.launch {
            try {
                _events.value = PortalClient.get().getEvents().asyncExecute().body()
            } catch (exception: Exception) {
                Log.e(TAG, "Failed to get event list", exception)
                _events.value = null
            }
        }
    }

    fun fetchEventConfig(eventId: String) {
        viewModelScope.launch {
            try {
                _eventConfig.value =
                    PortalClient.get().getEventConfig(eventId).asyncExecute().body()
            } catch (exception: Exception) {
                Log.e(TAG, "Failed to fetch event config for $eventId", exception)
            }
        }
    }
}
