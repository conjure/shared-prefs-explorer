package uk.co.conjure.sharedprefsexplorer.dialogs

import uk.co.conjure.rxlifecycle.RxView
import uk.co.conjure.rxlifecycle.whileStarted
import uk.co.conjure.sharedprefsexplorer.databinding.DialogEditValueBinding

/**
 * View for the EditPreferenceDialog.
 * @see DialogEditValueBinding
 */
class EditPreferenceView : RxView<DialogEditValueBinding>() {
    lateinit var viewModel: EditPreferenceViewModel

    override fun onStart() {
        super.onStart()
        viewModel.inputType.whileStarted(this, binding.etValue::setInputType)
        binding.etValue.bind(viewModel.setCurrentValue, viewModel.currentValue)
        binding.btnSave.bindClicks(viewModel.saveClick, viewModel.isSaveEnabled)
        binding.btnCancel.bindClicks(viewModel.cancelClick)
        binding.tvTitle.bind(viewModel.currentKey)
    }
}