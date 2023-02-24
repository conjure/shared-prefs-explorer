package uk.co.conjure.sharedprefsexplorer.details

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import uk.co.conjure.rxlifecycle.whileStarted
import uk.co.conjure.sharedprefsexplorer.R
import uk.co.conjure.sharedprefsexplorer.dialogs.AddPreferenceDialog
import uk.co.conjure.sharedprefsexplorer.dialogs.EditPreferenceDialog
import uk.co.conjure.viewmodelscope.createViewModelScope

private const val ARG_PREFERENCE_NAME = "preferenceName"

/**
 * A Fragment that displays the preferences for a given preference file and allows to add, edit
 * and remove values.
 * Use the [PreferenceFragment.create] factory method to create an instance of this fragment.
 */
class PreferenceFragment : Fragment(R.layout.fragment_preferences) {

    companion object {
        fun create(preferenceName: String) = PreferenceFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PREFERENCE_NAME, preferenceName)
            }
        }
    }

    private val editDialogViewModel: EditDialogViewModel by createViewModelScope()
    private val addDialogViewModel: AddDialogViewModel by createViewModelScope()

    private val adapter: PreferenceAdapter by lazy {
        // Lazy because we need the context to create the adapter
        PreferenceAdapter(getSharedPreferences(requireContext()))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val prefs = getSharedPreferences(context)
        editDialogViewModel.setSharedPreferences(prefs)
        addDialogViewModel.setSharedPreferences(prefs)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(view)
        setupRecyclerView(view)
        setupFab(view)
    }

    override fun onStart() {
        super.onStart()
        adapter.reload()

        adapter.onPreferenceClick.whileStarted(this, { selectedPreference ->
            showEditPreferenceDialog(selectedPreference)
        })

        editDialogViewModel.onPreferenceUpdated.whileStarted(this, { key ->
            adapter.updatePreference(key)
        })

        addDialogViewModel.onPreferenceAdded.whileStarted(this, { event ->
            when (event) {
                is AddDialogViewModel.AddPreferenceEvent.PreferenceAdded ->
                    adapter.addPreference(event.key)
                is AddDialogViewModel.AddPreferenceEvent.ShowError ->
                    showError(requireContext())
            }
        })
    }

    private fun showEditPreferenceDialog(selectedPreference: Preference.ValuePreference) {
        editDialogViewModel.onOpenEditDialog(
            selectedPreference.key,
            selectedPreference.value.toString(),
            selectedPreference.itemType.inputType
        )
        EditPreferenceDialog().show(childFragmentManager, "editPreference")
    }


    private fun showAddPreferenceDialog() {
        AddPreferenceDialog().show(childFragmentManager, "addPreference")
    }

    private fun setupFab(view: View) {
        view.findViewById<View>(R.id.fab_create).setOnClickListener {
            showAddPreferenceDialog()
        }
    }

    private fun setupRecyclerView(view: View) {
        view.findViewById<RecyclerView>(R.id.rv_preferences).adapter = adapter
    }

    private fun setTitle(view: View) {
        view.findViewById<TextView>(R.id.tv_preference_name).text =
            arguments?.getString(ARG_PREFERENCE_NAME)
    }


    private fun showError(context: Context) {
        Toast.makeText(context, getString(R.string.invalid_value), Toast.LENGTH_SHORT).show()
    }

    private fun getSharedPreferences(context: Context) = context.getSharedPreferences(
        arguments?.getString(ARG_PREFERENCE_NAME) ?: "",
        Context.MODE_PRIVATE
    )


}


