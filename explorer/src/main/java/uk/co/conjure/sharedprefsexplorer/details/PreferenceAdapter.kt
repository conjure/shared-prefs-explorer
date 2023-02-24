package uk.co.conjure.sharedprefsexplorer.details

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import uk.co.conjure.sharedprefsexplorer.PreferenceType
import uk.co.conjure.sharedprefsexplorer.R
import uk.co.conjure.sharedprefsexplorer.details.Preference.BooleanPreference
import uk.co.conjure.sharedprefsexplorer.details.Preference.ValuePreference.*


/**
 * Adapter to list preferences in a [RecyclerView].
 */
class PreferenceAdapter(
    private val sharedPreferences: SharedPreferences
) : RecyclerView.Adapter<PreferenceAdapter.PreferenceViewHolder<out Preference>>() {

    private val preferences = mutableListOf<Preference>()

    private val _onPreferenceClick = PublishSubject.create<Preference.ValuePreference>()

    val onPreferenceClick: Observable<Preference.ValuePreference> = _onPreferenceClick.hide()

    @SuppressLint("NotifyDataSetChanged")
    fun reload() {
        this.preferences.clear()
        this.preferences.addAll(getPreferencesSorted())
        notifyDataSetChanged()
    }

    private fun getPreferencesSorted() = sharedPreferences.all
        .map { entry -> toPreference(entry.toPair()) }
        .sortedBy { it.key }

    private fun toPreference(it: Pair<String, Any?>) =
        when (val value = it.second) {
            is Boolean -> BooleanPreference(it.first, value)
            is String -> StringPreference(it.first, value)
            is Int -> IntPreference(it.first, value)
            is Long -> LongPreference(it.first, value)
            is Float -> FloatPreference(it.first, value)
            else -> throw IllegalArgumentException("Unknown type: ${value?.javaClass?.name}")
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PreferenceViewHolder<out Preference> {
        val inflater = LayoutInflater.from(parent.context)

        fun inflate(@LayoutRes id: Int): View {
            return inflater.inflate(id, parent, false)
        }

        return when (PreferenceType.fromInt(viewType)) {
            PreferenceType.BOOLEAN -> BooleanViewHolder(inflate(R.layout.item_preference_boolean))
            PreferenceType.STRING,
            PreferenceType.INT,
            PreferenceType.LONG,
            PreferenceType.FLOAT -> ValueViewHolder(inflate(R.layout.item_preference_string))
        }

    }

    override fun getItemCount(): Int = preferences.size

    override fun getItemViewType(position: Int): Int {
        return preferences[position].itemType.type
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder<out Preference>, position: Int) {
        holder.bind(preferences[position])
    }

    fun updatePreference(key: String) {
        val index = preferences.indexOfFirst { it.key == key }
        if (index >= 0) {
            preferences[index] = sharedPreferences.all[key]
                ?.let { toPreference(key to it) }
                ?: return
            notifyItemChanged(index)
        }
    }

    fun addPreference(key: String) {

        val prefs = getPreferencesSorted()
        val index = prefs.indexOfFirst { it.key == key }

        if (index >= 0) {
            preferences.add(index, prefs[index])
            notifyItemInserted(index)
        }
    }

    /**
     * Base class for all ViewHolders.
     */
    abstract inner class PreferenceViewHolder<T : Preference>(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        lateinit var preference: T
        protected val tvPreferenceName: TextView = itemView.findViewById(R.id.tv_preference_name)
        private val btnDelete: Button = itemView.findViewById(R.id.btn_delete)

        init {
            btnDelete.setOnClickListener { clearProperty() }
        }

        fun bind(preference: Preference) {
            @Suppress("UNCHECKED_CAST")
            this.preference = preference as T
            onBind(preference)
        }

        abstract fun onBind(preference: T)
        private fun clearProperty() {
            sharedPreferences.edit().remove(preference.key).apply()
            reload()
        }
    }

    /**
     * ViewHolder for Boolean preferences.
     */
    inner class BooleanViewHolder(itemView: View) :
        PreferenceViewHolder<BooleanPreference>(itemView) {

        private val switch: SwitchMaterial = itemView.findViewById(R.id.sw_switch)

        init {
            switch.setOnCheckedChangeListener { _, isChecked ->
                sharedPreferences.edit().putBoolean(preference.key, isChecked).apply()
            }
        }

        override fun onBind(preference: BooleanPreference) {
            tvPreferenceName.text = preference.key
            switch.isChecked = preference.value
        }

    }

    /**
     * ViewHolder for Int, Long, Float and String preferences.
     */
    inner class ValueViewHolder(itemView: View) :
        PreferenceViewHolder<Preference.ValuePreference>(itemView) {

        private val tvValue: TextView = itemView.findViewById(R.id.tv_preference_value)

        init {
            itemView.setOnClickListener {
                _onPreferenceClick.onNext(preference)
            }
        }

        override fun onBind(preference: Preference.ValuePreference) {
            tvPreferenceName.text = preference.key
            tvValue.text = preference.value.toString()
        }
    }
}


sealed class Preference(val key: String, val itemType: PreferenceType) {
    class BooleanPreference(key: String, val value: Boolean) :
        Preference(key, PreferenceType.BOOLEAN)

    sealed class ValuePreference(key: String, val value: Any, type: PreferenceType) :
        Preference(key, type) {
        class StringPreference(key: String, value: String) :
            ValuePreference(key, value, PreferenceType.STRING)

        class IntPreference(key: String, value: Int) :
            ValuePreference(key, value, PreferenceType.INT)

        class LongPreference(key: String, value: Long) :
            ValuePreference(key, value, PreferenceType.LONG)

        class FloatPreference(key: String, value: Float) :
            ValuePreference(key, value, PreferenceType.FLOAT)
    }
}