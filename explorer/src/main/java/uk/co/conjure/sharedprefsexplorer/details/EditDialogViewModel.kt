package uk.co.conjure.sharedprefsexplorer.details

import android.content.SharedPreferences
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import uk.co.conjure.rxlifecycle.RxViewModel
import uk.co.conjure.sharedprefsexplorer.dialogs.EditPreferenceViewModel

interface DialogHostViewModel {
    fun onOpenEditDialog(key: String, value: String, inputType: Int)
    val onPreferenceUpdated: Observable<String>
}

/**
 * ViewModel for the EditPreferenceDialog.
 *
 * It allows the Dialog to communicate with the hosting Fragment.
 */
class EditDialogViewModel : RxViewModel(), EditPreferenceViewModel, DialogHostViewModel {

    private lateinit var sharedPreferences: SharedPreferences

    private val _currentKey = BehaviorSubject.create<String>()
    private val _currentValue = BehaviorSubject.create<String>()
    private val _inputType = BehaviorSubject.create<Int>()

    private val _saveClick = PublishSubject.create<Unit>()
    private val _cancelClick = PublishSubject.create<Unit>()

    override val onPreferenceUpdated: Observable<String> =
        _saveClick.withLatestFrom(_currentKey, _currentValue) { _, key, value ->
            sharedPreferences.edit().putString(key, value).apply()
            key
        }.hot()

    override val cancelClick: Observer<Unit> = _cancelClick
    override val setCurrentValue: Observer<String> = _currentValue
    override val saveClick: Observer<Unit> = _saveClick

    override val inputType: Observable<Int> = _inputType.hide()
    override val currentValue: Observable<String> = _currentValue.hide()
    override val currentKey: Observable<String> = _currentKey.hide()

    override val isSaveEnabled: Observable<Boolean>
        get() = _currentValue.map { it.isNotBlank() }

    override val dismiss: Observable<Unit>
        get() = Observable.merge(_cancelClick, _saveClick)

    /**
     * To be called before using this ViewModel.
     * Could be done in the constructor, but this would require to set up DI or use a factory.
     */
    fun setSharedPreferences(sharedPreferences: SharedPreferences) {
        this.sharedPreferences = sharedPreferences
    }

    /**
     * To be called when opening the edit dialog.
     */
    override fun onOpenEditDialog(key: String, value: String, inputType: Int) {
        _currentKey.onNext(key)
        _currentValue.onNext(value)
        _inputType.onNext(inputType)
    }
}