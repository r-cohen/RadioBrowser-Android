package com.r.cohen.radiobrowserandroid

import com.r.cohen.radiobrowserandroid.models.RadioBrowserClickResult
import com.r.cohen.radiobrowserandroid.models.RadioBrowserCountry
import com.r.cohen.radiobrowserandroid.models.RadioBrowserState
import com.r.cohen.radiobrowserandroid.models.RadioBrowserStation
import com.r.cohen.radiobrowserandroid.services.RadioBrowserApiService
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class RadioBrowserApiTest {
    private val mockRadioBrowserApiService = object : RadioBrowserApiService {
        override suspend fun getCountries(
            userAgent: String,
            order: String
        ): List<RadioBrowserCountry> {
            return listOf(
                RadioBrowserCountry("Israel", "IL", 1)
            )
        }

        override suspend fun getStatesByCountryName(
            userAgent: String,
            countryName: String
        ): List<RadioBrowserState> {
            return emptyList()
        }

        override suspend fun getStationsByCountry(
            userAgent: String,
            countryCode: String,
            offset: Int,
            limit: Int
        ): List<RadioBrowserStation> {
            if (countryCode != "IL") return emptyList()
            return listOf(
                RadioBrowserStation(
                    stationuuid = "29b28a1a-e60d-4b1a-b57d-e7d46b95f36c",
                    name = "88FM",
                    url = "https://kan88.media.kan.org.il/hls/live/2024812/2024812/kan88_mp3/chunklist.m3u8",
                    url_resolved = "https://kan88.media.kan.org.il/hls/live/2024812/2024812/kan88_mp3/chunklist.m3u8",
                    homepage = "https://www.kan.org.il/",
                    favicon = ""
                )
            )
        }

        override suspend fun getStationsBySearch(
            userAgent: String,
            search: String,
            offset: Int,
            limit: Int
        ): List<RadioBrowserStation> {
            return emptyList()
        }

        override suspend fun getStationsByState(
            userAgent: String,
            stateName: String
        ): List<RadioBrowserStation> {
            return emptyList()
        }

        override suspend fun stationClick(
            userAgent: String,
            stationUuid: String
        ): RadioBrowserClickResult {
            return RadioBrowserClickResult("true", "retrieved station url", stationUuid, "namd", "url")
        }
    }

    private val radioBrowserApi = RadioBrowserApi()

    @Before
    fun setMockService() {
        radioBrowserApi.radioBrowserService = mockRadioBrowserApiService
    }

    @Test
    fun searchEmpty_notAllowed() {
        val lock = CountDownLatch(1)
        var failError = ""
        radioBrowserApi.searchStationsByName(
            search = "",
            offset = 0,
            limit = 500,
            onSuccess = {

            }, { error ->
                error?.let { failError = it }
                lock.countDown()
            }
        )
        lock.await(1, TimeUnit.SECONDS)
        assertEquals("search cannot be empty", failError)
    }

    @Test
    fun search_noResults() {
        val lock = CountDownLatch(1)
        var results: List<RadioBrowserStation>? = null
        radioBrowserApi.searchStationsByName(
            search = "no results",
            offset = 0,
            limit = 500,
            onSuccess = {
                results = it
                lock.countDown()
            }, {
            }
        )
        lock.await(1, TimeUnit.SECONDS)
        assertTrue(results != null && results?.isEmpty() ?: false)
    }
}