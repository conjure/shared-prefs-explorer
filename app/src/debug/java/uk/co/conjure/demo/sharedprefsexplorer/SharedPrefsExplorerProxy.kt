package uk.co.conjure.demo.sharedprefsexplorer

import android.content.Intent
import uk.co.conjure.sharedprefsexplorer.PreferenceBrowserActivity

class SharedPrefsExplorerProxy {
    companion object {
        fun start(context: android.content.Context) {
            context.startActivity(Intent(context, PreferenceBrowserActivity::class.java))
        }
    }
}