package uk.co.conjure.sharedprefsexplorer.dialogs

import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import uk.co.conjure.rxlifecycle.RxView
import uk.co.conjure.sharedprefsexplorer.PreferenceType
import uk.co.conjure.sharedprefsexplorer.R
import uk.co.conjure.sharedprefsexplorer.databinding.DialogAddValueBinding

/**
 * View for the AddPreferenceDialog.
 * @see DialogAddValueBinding
 */
class AddPreferenceView : RxView<DialogAddValueBinding>() {
    lateinit var viewModel: AddPreferenceViewModel

    override fun onCreate() {
        super.onCreate()
        binding.spType.adapter = ArrayAdapter.createFromResource(
            context,
            R.array.preference_types,
            android.R.layout.simple_spinner_item
        )
    }

    override fun onStart() {
        super.onStart()

        binding.spType.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.setInputType.onNext(getPreferenceType(position))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        binding.etKey.bind(viewModel.setCurrentKey)
        binding.etValue.bind(viewModel.setCurrentValue)

        binding.btnSave.bindClicks(viewModel.saveClick, viewModel.isSaveEnabled)
        binding.btnCancel.bindClicks(viewModel.cancelClick)

    }

    private fun getPreferenceType(position: Int): PreferenceType {
        return when (position) {
            0 -> PreferenceType.STRING
            1 -> PreferenceType.BOOLEAN
            2 -> PreferenceType.INT
            3 -> PreferenceType.LONG
            4 -> PreferenceType.FLOAT
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }


}