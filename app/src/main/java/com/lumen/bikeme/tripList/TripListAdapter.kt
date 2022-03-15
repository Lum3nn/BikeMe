package com.lumen.bikeme.tripList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lumen.bikeme.commons.model.TripItem
import com.lumen.bikeme.commons.model.TripItemDate
import com.lumen.bikeme.commons.model.TripItemList
import com.lumen.bikeme.commons.toFormattedString
import com.lumen.bikeme.databinding.TripDateItemBinding
import com.lumen.bikeme.databinding.TripItemBinding
import com.lumen.bikeme.databinding.TripLoadingItemBinding
import com.lumen.bikeme.commons.repository.DateNamesRepository
import java.util.*

class TripListAdapter(
    private val onTripSwipeToDelete: OnTripDelete,
    private val onTripUndoAdd: OnTrippAdd,
    ) : ListAdapter<TripItemList, TripListAdapter.TripsListItemViewHolder>(DIFF) {

    companion object {

        const val TYPE_DATE = 1
        const val TYPE_TRIP = 2
        const val TYPE_LOADING = 3

        private val DIFF = object : DiffUtil.ItemCallback<TripItemList>() {

            override fun areItemsTheSame(
                oldItem: TripItemList, newItem: TripItemList
            ): Boolean {
                if (oldItem is TripItem && newItem is TripItem) {
                    return oldItem == newItem
                } else if (oldItem is TripItemDate && newItem is TripItemDate) {
                    return oldItem.date == newItem.date
                }
                return false
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: TripItemList, newItem: TripItemList
            ): Boolean = oldItem == newItem
        }
    }

    fun deleteTrip(id: String) {
        onTripSwipeToDelete.onTripSwipeToDelete(id)
    }


    fun onTripAdd(tripName: String, tripDistance: String, tripDate: String, tripId: String) {
        onTripUndoAdd.onTripAdd(tripName, tripDistance, tripDate, tripId)
    }

    interface OnTrippAdd {
        fun onTripAdd(tripName: String, tripDistance: String, tripDate: String, tripId: String)
    }

    interface OnTripDelete {
        fun onTripSwipeToDelete(tripId: String)
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is TripItemDate -> TYPE_DATE
            is TripItem -> TYPE_TRIP
            else -> TYPE_LOADING
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripsListItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_DATE -> {
                val itemView = TripDateItemBinding.inflate(layoutInflater, parent, false)
                TripDateViewHolder(itemView)
            }
            TYPE_TRIP -> {
                val itemView = TripItemBinding.inflate(layoutInflater, parent, false)
                TripItemViewHolder(itemView)
            }
            else -> {
                val itemView = TripLoadingItemBinding.inflate(layoutInflater, parent, false)
                LoadingViewHolder(itemView)
            }
        }
    }

    override fun onBindViewHolder(holder: TripsListItemViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    abstract class TripsListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(data: TripItemList)
    }

    class LoadingViewHolder(
        binding: TripLoadingItemBinding
    ) : TripsListItemViewHolder(binding.root) {
        override fun bind(data: TripItemList) {}
    }

    class TripDateViewHolder(
        private val binding: TripDateItemBinding
    ) : TripsListItemViewHolder(binding.root) {

        override fun bind(data: TripItemList) {
            val tripDate = data as TripItemDate

            val calendar = Calendar.getInstance()
            calendar.time = tripDate.date
            val month = DateNamesRepository.getMonthName(calendar.get(Calendar.MONTH) + 1)
            val year = (calendar.get(Calendar.YEAR)).toString()

            val fullHeader = StringBuilder()
            fullHeader.append(month).append(" ").append(year)

            binding.tripDate.text = fullHeader.toString()
        }
    }

    class TripItemViewHolder(
        private val binding: TripItemBinding,
    ) : TripsListItemViewHolder(binding.root) {

        var tripId: String = ""
        var tripName: String = ""
        var tripDistance: String = ""
        var tripDate: String = ""

        override fun bind(data: TripItemList) {
            val tripItem = data as TripItem
            tripId = tripItem.id
            tripName = tripItem.name
            tripDistance = tripItem.distance
            tripDate = tripItem.date.toFormattedString()

            val fullDistance = StringBuilder()
            fullDistance.append(tripDistance).append(" km")

            binding.tripItemName.text = tripName
            binding.tripItemDistance.text = fullDistance
            binding.tripItemFullDate.text = tripDate
        }
    }
}
