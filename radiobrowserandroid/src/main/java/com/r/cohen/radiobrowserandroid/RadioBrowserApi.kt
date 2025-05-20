package com.r.cohen.radiobrowserandroid

import android.util.Log
import com.r.cohen.radiobrowserandroid.models.RadioBrowserClickResult
import com.r.cohen.radiobrowserandroid.models.RadioBrowserCountry
import com.r.cohen.radiobrowserandroid.models.RadioBrowserOrder
import com.r.cohen.radiobrowserandroid.models.RadioBrowserState
import com.r.cohen.radiobrowserandroid.models.RadioBrowserStation
import com.r.cohen.radiobrowserandroid.services.RadioBrowserApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.InetAddress

private const val allServersEndpoint = "all.api.radio-browser.info"
private const val logTag = "RBA"
private const val initFailMsg = "RadioBrowser initialize failed to find available server"

class RadioBrowserApi(private val userAgent: String = "RC.RadioBrowserAndroid") {
    var radioBrowserService: RadioBrowserApiService? = null

    private suspend fun initialize() {
        if (radioBrowserService == null) {
            val serverEndpoint = getRadioBrowserServer()
            radioBrowserService = Retrofit.Builder()
                .baseUrl("https://$serverEndpoint")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RadioBrowserApiService::class.java)
        }
    }

    private suspend fun getRadioBrowserServer(): String = withContext(Dispatchers.IO) {
        val servers = InetAddress.getAllByName(allServersEndpoint)
        if (servers.isEmpty()) { return@withContext "" }
        servers.shuffle()
        return@withContext servers.first().canonicalHostName
    }

    private fun handleApiException(e: Exception, onFail: (String?) -> Unit) {
        e.message?.let { Log.e(logTag, it) }
        onFail.invoke(e.message)
    }

    fun getCountries(
        order: RadioBrowserOrder = RadioBrowserOrder.BY_NAME,
        onSuccess: (List<RadioBrowserCountry>) -> Unit,
        onFail: (String?) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        try {
            initialize()
            radioBrowserService?.getCountries(
                userAgent = userAgent,
                order = order.getApiValue()
            )?.let(onSuccess) ?: onFail.invoke(initFailMsg)
        } catch (e: Exception) {
            handleApiException(e, onFail)
        }
    }

    fun getStationsByCountry(
        countryCode: String,
        offset: Int = 0,
        limit: Int = 1000,
        order: RadioBrowserOrder = RadioBrowserOrder.BY_NAME,
        reverse: Boolean = false,
        onSuccess: (List<RadioBrowserStation>) -> Unit,
        onFail: (String?) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        try {
            initialize()
            radioBrowserService?.getStationsByCountry(
                userAgent = userAgent,
                countryCode = countryCode,
                offset = offset,
                limit = limit,
                order = order.getApiValue(),
                reverse = reverse
            )?.let(onSuccess) ?: onFail.invoke(initFailMsg)
        } catch (e: Exception) {
            handleApiException(e, onFail)
        }
    }

    fun searchStationsByName(
        search: String,
        offset: Int = 0,
        limit: Int = 1000,
        onSuccess: (List<RadioBrowserStation>) -> Unit,
        onFail: (String?) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        if (search.isEmpty()) {
            onFail.invoke("search cannot be empty")
            return@launch
        }
        try {
            initialize()
            radioBrowserService?.getStationsBySearch(
                userAgent = userAgent,
                search = search,
                offset = offset,
                limit = limit
            )?.let(onSuccess) ?: onFail.invoke(initFailMsg)
        } catch (e: Exception) {
            handleApiException(e, onFail)
        }
    }

    fun getStatesByCountry(
        countryName: String,
        onSuccess: (List<RadioBrowserState>) -> Unit,
        onFail: (String?) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        if (countryName.isEmpty()) {
            onFail.invoke("countryName cannot be empty")
            return@launch
        }
        try {
            initialize()
            radioBrowserService?.getStatesByCountryName(
                userAgent = userAgent,
                countryName = countryName
            )?.let(onSuccess) ?: onFail.invoke(initFailMsg)
        } catch (e: Exception) {
            handleApiException(e, onFail)
        }
    }

    fun getStationsByState(
        stateName: String,
        offset: Int = 0,
        limit: Int = 1000,
        onSuccess: (List<RadioBrowserStation>) -> Unit,
        onFail: (String?) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        if (stateName.isEmpty()) {
            onFail.invoke("stateName cannot be empty")
            return@launch
        }
        try {
            initialize()
            radioBrowserService?.getStationsByState(
                userAgent = userAgent,
                stateName = stateName,
                offset = offset,
                limit = limit
            )?.let(onSuccess) ?: onFail.invoke(initFailMsg)
        } catch (e: Exception) {
            handleApiException(e, onFail)
        }
    }

    fun getStationByUuid(
        stationUuid: String,
        onSuccess: (List<RadioBrowserStation>) -> Unit,
        onFail: (String?) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        if (stationUuid.isEmpty()) {
            onFail.invoke("stationUuid cannot be empty")
            return@launch
        }
        try {
            initialize()
            radioBrowserService?.getStationsById(
                userAgent = userAgent,
                stationUuid = stationUuid
            )?.let(onSuccess) ?: onFail.invoke(initFailMsg)
        } catch (e: Exception) {
            handleApiException(e, onFail)
        }
    }

    fun stationClick(
        stationUuid: String,
        onSuccess: (RadioBrowserClickResult) -> Unit,
        onFail: (String?) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        if (stationUuid.isEmpty()) {
            onFail.invoke("stationUuid cannot be empty")
            return@launch
        }
        try {
            initialize()
            radioBrowserService?.stationClick(
                userAgent = userAgent,
                stationUuid = stationUuid
            )?.let(onSuccess) ?: onFail.invoke(initFailMsg)
        } catch (e: Exception) {
            handleApiException(e, onFail)
        }
    }
}