package com.r.cohen.radiobrowserandroid.models

enum class RadioBrowserOrder {
    BY_NAME, BY_STATIONCOUNT, BY_CLICKCOUNT;

    fun getApiValue(): String = when(this) {
        BY_NAME -> "name"
        BY_STATIONCOUNT -> "stationcount"
        BY_CLICKCOUNT -> "clickcount"
    }
}