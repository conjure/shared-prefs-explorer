package uk.co.conjure.sharedprefsexplorer.dialogs

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer


interface EditPreferenceViewModel {
    // input
    val cancelClick: Observer<Unit>
    val saveClick: Observer<Unit>
    val setCurrentValue: Observer<String>

    // output
    val inputType: Observable<Int>
    val currentValue: Observable<String>
    val currentKey: Observable<String>
    val isSaveEnabled: Observable<Boolean>

    val dismiss: Observable<Unit>

}