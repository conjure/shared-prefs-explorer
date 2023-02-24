package uk.co.conjure.sharedprefsexplorer.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import uk.co.conjure.rxlifecycle.whileStarted
import uk.co.conjure.sharedprefsexplorer.R
import uk.co.conjure.sharedprefsexplorer.databinding.DialogEditValueBinding
import uk.co.conjure.viewmodelscope.scopedInterface

/**
 * A DialogFragment that allows the user to edit a preference.
 */
class EditPreferenceDialog : DialogFragment() {

    private val viewModel: EditPreferenceViewModel by scopedInterface()
    private val view: EditPreferenceView = EditPreferenceView()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DialogEditValueBinding.inflate(inflater, container, false)
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