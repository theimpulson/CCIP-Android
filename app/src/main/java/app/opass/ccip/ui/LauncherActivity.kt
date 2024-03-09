package app.opass.ccip.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.opass.ccip.ui.auth.AuthActivity
import app.opass.ccip.ui.event.EventActivity
import app.opass.ccip.util.PreferenceUtil

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentEventId = PreferenceUtil.getCurrentEvent(this).eventId
        val isLaunchedFromHistory = intent.flags and Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY != 0

        if (intent.action == Intent.ACTION_VIEW && !isLaunchedFromHistory) {
            val eventId = intent.data!!.getQueryParameter("event_id") ?: ""
            val token = intent.data!!.getQueryParameter("token") ?: ""

            if (eventId.isNotEmpty()) {
                if (token.isNotEmpty()) {
                    startActivity(AuthActivity.createIntent(this, eventId, token))
                } else {
                    startActivity(AuthActivity.createIntent(this, eventId, null))
                }
                return finish()
            }
        }
        if (currentEventId.isNotEmpty()) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            startActivity(Intent(this, EventActivity::class.java))
        }
        finish()
    }
}
