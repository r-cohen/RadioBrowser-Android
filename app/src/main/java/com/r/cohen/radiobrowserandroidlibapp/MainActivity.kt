package com.r.cohen.radiobrowserandroidlibapp

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.r.cohen.radiobrowserandroid.RadioBrowserApi

class MainActivity : AppCompatActivity() {
    private val api = RadioBrowserApi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getCountries()
    }

    private fun outputText(text: String) = runOnUiThread {
        val view = TextView(this)
        view.text = text
        findViewById<LinearLayout>(R.id.linearlayoutOut).addView(view)
    }

    private fun getCountries() {
        api.getCountries(
            onSuccess = { countries ->
                outputText("getCountries result (${countries.size}):")
                countries.forEach { country -> outputText("country: $country") }

                getStationsByCountry()
            },
            onFail = { error -> outputText("error: $error") }
        )
    }

    private fun getStationsByCountry() {
        api.getStationsByCountry(
            countryCode = "US",
            offset = 0,
            limit = 500,
            onSuccess = { stations ->
                outputText("\ngetStationsByCountry result (${stations.size}):")
                stations.forEach { station -> outputText("station $station") }

                searchStationsByName()
            },
            onFail = { error -> outputText("error: $error") }
        )
    }

    private fun searchStationsByName() {
        api.searchStationsByName(
            search = "kol chai",
            offset = 0,
            limit = 500,
            onSuccess = { stations ->
                outputText("\nsearchStationsByName result:")
                stations.forEach { station -> outputText("station $station") }

                getStatesByCountry()
            },
            onFail = { error -> outputText("error: $error") }
        )
    }

    private fun getStatesByCountry() {
        api.getStatesByCountry(
            countryName = "Israel",
            onSuccess = { states ->
                outputText("\ngetStatesByCountry result: ${states.count()}")
                states.forEach { state -> outputText("state $state") }

                getStationsByState()
            },
            onFail = { error -> outputText("error: $error") }
        )
    }

    private fun getStationsByState() {
        api.getStationsByState(
            stateName = "Haifa",
            offset = 0,
            limit = 500,
            onSuccess = { stations ->
                outputText("\ngetStationsByState result:")
                stations.forEach { station ->
                    outputText("station $station")

                    clickStation(station.stationuuid)
                }
            },
            onFail = { error -> outputText("error: $error") }
        )
    }

    private fun clickStation(stationUuid: String) {
        api.stationClick(
            stationUuid = stationUuid,
            onSuccess = { clickResult ->
                outputText("\n station click result:")
                outputText("$clickResult")
            },
            onFail = { error -> outputText("error: $error") }
        )
    }
}