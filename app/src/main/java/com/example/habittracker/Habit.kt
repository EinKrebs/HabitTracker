package com.example.habittracker

import android.os.Parcel
import android.os.Parcelable

enum class HabitType(val value: String) {
    HEALTHY("Healthy"), NEUTRAL("Neutral"), UNHEALTHY("Unhealthy")
}

class Habit(
    val name: String,
    val description: String,
    val priority: Int,
    val period: String,
    val type: HabitType,
    val color: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        HabitType.valueOf(parcel.readString()!!.toUpperCase()),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeInt(priority)
        parcel.writeString(period)
        parcel.writeString(type.value);
        parcel.writeInt(color)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Habit> {
        override fun createFromParcel(parcel: Parcel): Habit {
            return Habit(parcel)
        }

        override fun newArray(size: Int): Array<Habit?> {
            return arrayOfNulls(size)
        }
    }
}