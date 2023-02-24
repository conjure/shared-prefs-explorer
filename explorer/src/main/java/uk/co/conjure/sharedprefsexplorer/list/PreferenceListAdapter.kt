package uk.co.conjure.sharedprefsexplorer.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import uk.co.conjure.sharedprefsexplorer.R

/**
 * Adapter for the list of preference files.
 */
class PreferenceListAdapter : RecyclerView.Adapter<PreferenceListAdapter.ViewHolder>() {

    private val preferences = mutableListOf<SharedPrefFolder>()
    private val _onPreferenceSelected = PublishSubject.create<String>()

    val onPreferenceSelected: Observable<String> = _onPreferenceSelected.hide()

    @SuppressLint("NotifyDataSetChanged")
    fun setPreferences(preferences: List<SharedPrefFolder>) {
        this.preferences.clear()
        this.preferences.addAll(preferences)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.preference_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(preferences[position])
    }

    override fun getItemCount(): Int = preferences.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var preference: SharedPrefFolder
        private val preferenceName = itemView.findViewById<TextView>(R.id.tv_title)

        init {
            itemView.setOnClickListener {
                _onPreferenceSelected.onNext(preference.preferenceName)
            }
        }

        fun bind(preference: SharedPrefFolder) {
            this.preference = preference
            preferenceName.text = preference.preferenceName
        }
    }
}