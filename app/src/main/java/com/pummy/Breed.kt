package com.pummy

import android.os.Parcel
import android.os.Parcelable


data class Breed(
    val name: String,
    val lifespan: String,
    val imageResourceId: String,
    val description: String,
    val temperament: String,
    val exerciseNeeds: String,
    val groomingNeeds: String,
    val healthConsiderations: String,
    val trainability: String,
    val sizeAndWeight: String,
    val historyAndOrigin: String,
    val specialRequirements: String,
    val activityLevel: String,
    val popularity: String,
    val notableCharacteristics: String
    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(lifespan)
        parcel.writeString(imageResourceId)
        parcel.writeString(description)
        parcel.writeString(temperament)
        parcel.writeString(exerciseNeeds)
        parcel.writeString(groomingNeeds)
        parcel.writeString(healthConsiderations)
        parcel.writeString(trainability)
        parcel.writeString(sizeAndWeight)
        parcel.writeString(historyAndOrigin)
        parcel.writeString(specialRequirements)
        parcel.writeString(activityLevel)
        parcel.writeString(popularity)
        parcel.writeString(notableCharacteristics)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Breed> {
        override fun createFromParcel(parcel: Parcel): Breed {
            return Breed(parcel)
        }

        override fun newArray(size: Int): Array<Breed?> {
            return arrayOfNulls(size)
        }
    }
}