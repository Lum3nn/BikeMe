package com.lumen.bikeme

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("ConstantLocale")
private val sdfLong = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
@SuppressLint("ConstantLocale")
private val sdfShort = SimpleDateFormat("MM-yyyy", Locale.getDefault())

fun String.toDate(): Date {
    return sdfLong.parse(this)!!
}

fun Date.toFormattedString() : String {
    return sdfLong.format(this)
}

fun String.toShortDate() : Date {
    return sdfShort.parse(this)!!
}

fun Date.toFormattedShortString() : String {
    return sdfShort.format(this)
}