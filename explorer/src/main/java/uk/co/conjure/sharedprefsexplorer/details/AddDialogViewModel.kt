package uk.co.conjure.sharedprefsexplorer.details

import android.content.SharedPreferences
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import uk.co.conjure.rxlifecycle.RxViewModel
import uk.co.conjure.sharedprefsexplorer.PreferenceType
import uk.co.conjure.sharedprefsexplorer.dialogs.AddPreferenceViewModel

/**
 * ViewModel for the AddPreferenceDialog.
 *
 * It allows the Dialog to communicate with the hosting Fragment.
 */
class AddDialogViewModel : RxViewModel(), AddPreferenceViewModel {

    private lateinit var sharedPreferences: SharedPreferences

    override val setCurrentKey: PublishSubject<String> = PublishSubject.create()
    override val setCurrentValue: PublishSubject<String> = PublishSubject.create()
    override val setInputType: PublishSubject<PreferenceType> = PublishSubject.create()
    override val cancelClick: PublishSubject<Unit> = PublishSubject.create()

    override val saveClick: PublishSubject<Unit> = PublishSubject.create()
    override val valueInputType: Observable<Int> = setInputType.map { it.inputType }

    override val isSaveEnabled: Observable<Boolean> = Observable.combineLatest(
        setCurrentKey.map { it.isNotBlank() },
        setCurrentValue.map { it.isNotBlank() }
    ) { key, value -> key && value }


    override val dismiss: Observable<Unit> = Observable.merge(saveClick, cancelClick)

    val onPreferenceAdded: Observable<out AddPreferenceEvent> = saveClick.withLatestFrom(
        setCurrentKey,
        setCurrentValue,
        setInputType
    ) { _, key, value, type ->
        sharedPreferences.edit().apply {
            try {
                when (type) {
                    PreferenceType.BOOLEAN -> putBoolean(key, value.toBoolean())
                    PreferenceType.STRING -> putString(key, value)
                    PreferenceType.INT -> putInt(key, value.toInt())
                    PreferenceType.LONG -> putLong(key, value.toLong())
                    PreferenceType.FLOAT -> putFloat(key, value.toFloat())
                    null -> {}
                }
                apply()

            } catch (ex: NumberFormatException) {
                return@withLatestFrom AddPreferenceEvent.ShowError
            }
        }
        AddPreferenceEvent.PreferenceAdded(key)
    }.hot()

    fun setSharedPreferences(sharedPreferences: SharedPreferences) {
        this.sharedPreferences = sharedPreferences
    }

    sealed class AddPreferenceEvent {
        object ShowError : AddPreferenceEvent()
        class PreferenceAdded(val key: String) : AddPreferenceEvent()
    }
}