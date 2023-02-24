package uk.co.conjure.sharedprefsexplorer.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import uk.co.conjure.rxlifecycle.whileStarted
import uk.co.conjure.sharedprefsexplorer.PreferenceBrowserActivity
import uk.co.conjure.sharedprefsexplorer.R

/**
 * A Fragment that displays a list of preference files.
 */
class PreferenceListFragment : Fragment() {

    private val preferencesAdapter = PreferenceListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_preferences_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<RecyclerView>(R.id.rv_preferences).adapter = preferencesAdapter
    }

    override fun onStart() {
        super.onStart()
        updatePreferenceFiles()
        preferencesAdapter.onPreferenceSelected.whileStarted(this,
            { name -> (requireActivity() as PreferenceBrowserActivity).showPreference(name) })
    }

    private fun updatePreferenceFiles() {
        preferencesAdapter.setPreferences(
            requireContext().dataDir.listFiles()?.firstOrNull { it.name == "shared_prefs" }
                ?.listFiles()
                ?.map { SharedPrefFolder(it.name) } ?: emptyList()
        )
    }
}

data class SharedPrefFolder(
    val fileName: String,
    val preferenceName: String = fileName.removeSuffix(".xml")
)