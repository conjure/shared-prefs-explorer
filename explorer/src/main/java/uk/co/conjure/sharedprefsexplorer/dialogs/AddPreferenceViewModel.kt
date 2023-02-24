package uk.co.conjure.sharedprefsexplorer.dialogs

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import uk.co.conjure.sharedprefsexplorer.PreferenceType


interface AddPreferenceViewModel {
    // input

    val setCurrentKey: Observer<String>
    val setCurrentValue: Observer<String>
    val setInputType: Observer<PreferenceType>

    val cancelClick: Observer<Unit>
    val saveClick: Observer<Unit>

    // output

    val valueInputType: Observable<Int>
    val isSaveEnabled: Observable<Boolean>
    val dismiss: Observable<Unit>

}