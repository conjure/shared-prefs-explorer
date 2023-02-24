package uk.co.conjure.demo.sharedprefsexplorer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


private const val SHARED_PREFS_NAME = "uk.co.conjure.demo.shared_prefs_explorer-test1"
private const val SHARED_PREFS_NAME_2 = "uk.co.conjure.demo.shared_prefs_explorer-test2"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createSharedPrefs(SHARED_PREFS_NAME)
        createSharedPrefs(SHARED_PREFS_NAME_2)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_explore).setOnClickListener {
            SharedPrefsExplorerProxy.start(this)
        }
    }

    private fun createSharedPrefs(name: String) {
        val sharedPrefs = getSharedPreferences(name, MODE_PRIVATE)
        if (sharedPrefs.getBoolean("set", false)) return
        val editor = sharedPrefs.edit()
        editor.putBoolean("set", true)
        editor.putString("test", "test")
        editor.putString(
            "test a very long key that should either wrap or whatever but not mess with the layout",
            "test a very long value next to the key that should either wrap or whatever but not mess with the layout"
        )
        editor.putBoolean("boolean1", true)
        editor.putBoolean("boolean2", true)
        editor.putInt("int1", 1)
        for (i in 0..20) {
            editor.putString("test$i", "test$i")
        }
        editor.apply()
    }
}