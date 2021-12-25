package id.nuryono.adnan.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Items(
    val author: String,
    val desc: String,
    val key: String,
    val tag: String,
    val thumb: String,
    val time: String,
    val title: String
) : Parcelable