package uk.co.conjure.sharedprefsexplorer

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uk.co.conjure.sharedprefsexplorer.details.PreferenceFragment

class PreferenceBrowserActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, PreferenceBrowserActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference_browser)
    }

    fun showPreference(preferenceName: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, PreferenceFragment.create(preferenceName), "preference")
            .addToBackStack("preference")
            .commit()
    }
}