package uk.co.conjure.sharedprefsexplorer

import android.text.InputType

/**
 * Enum class for the different types of preferences.
 */
enum class PreferenceType(
    /**
     * An ID for the Type
     */
    val type: Int,
    /**
     * The [InputType] for the value input field (EditText).
     */
    val inputType: Int
) {

    BOOLEAN(1, 0),
    STRING(2, InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL),
    INT(3, InputType.TYPE_CLASS_NUMBER),
    LONG(4, InputType.TYPE_CLASS_NUMBER),
    FLOAT(5, InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL);

    companion object {
        fun fromInt(type: Int): PreferenceType {
            return values().first { it.type == type }
        }
    }
}