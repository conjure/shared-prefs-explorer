package uk.co.conjure.sharedprefsexplorer.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import uk.co.conjure.rxlifecycle.whileStarted
import uk.co.conjure.sharedprefsexplorer.databinding.DialogAddValueBinding
import uk.co.conjure.viewmodelscope.scopedInterface

/**
 * A DialogFragment that allows the user to add a new preference.
 */
class AddPreferenceDialog : DialogFragment() {

    private val viewModel: AddPreferenceViewModel by scopedInterface()
    private val view: AddPreferenceView = AddPreferenceView()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DialogAddValueBinding.inflate(inflater, container, false)
        view.viewModel = viewModel
        view.registerBinding(binding, this)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        fixDialogSize()
        viewModel.dismiss.whileStarted(this, { dismiss() })
    }

    private fun fixDialogSize() {
        dialog?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.window?.setLayout(width, height)
        }
    }
}