package com.r.cohen.radiobrowserandroid.services

import com.r.cohen.radiobrowserandroid.models.RadioBrowserClickResult
import com.r.cohen.radiobrowserandroid.models.RadioBrowserCountry
import com.r.cohen.radiobrowserandroid.models.RadioBrowserOrder
import com.r.cohen.radiobrowserandroid.models.RadioBrowserState
import com.r.cohen.radiobrowserandroid.models.RadioBrowserStation
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface RadioBrowserApiService {
    @GET("json/countries")
    suspend fun getCountries(
        @Header("User-Agent") userAgent: String,
        @Query("order") order: String = RadioBrowserOrder.BY_NAME.getApiValue()
    ): List<RadioBrowserCountry>

    @GET("json/states/{countryName}/")
    suspend fun getStatesByCountryName(
        @Header("User-Agent") userAgent: String,
        @Path("countryName") countryName: String
    ): List<RadioBrowserState>

    @GET("json/stations/bycountrycodeexact/{countryCode}")
    suspend fun getStationsByCountry(
        @Header("User-Agent") userAgent: String,
        @Path("countryCode") countryCode: String,
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 1000,
        @Query("order") order: String = RadioBrowserOrder.BY_NAME.getApiValue(),
        @Query("reverse") reverse: Boolean = false
    ): List<RadioBrowserStation>

    @GET("json/stations/byname/{search}")
    suspend fun getStationsBySearch(
        @Header("User-Agent") userAgent: String,
        @Path("search") search: String,
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 1000
    ): List<RadioBrowserStation>

    @GET("json/stations/bystateexact/{stateName}")
    suspend fun getStationsByState(
        @Header("User-Agent") userAgent: String,
        @Path("stateName") stateName: String,
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 1000
    ): List<RadioBrowserStation>

    @GET("json/url/{stationUuid}")
    suspend fun stationClick(
        @Header("User-Agent") userAgent: String,
        @Path("stationUuid") stationUuid: String
    ): RadioBrowserClickResult

    @GET("json/stations/byuuid/{stationUuid}")
    suspend fun getStationsById(
        @Header("User-Agent") userAgent: String,
        @Path("stationUuid") stationUuid: String
    ): List<RadioBrowserStation>

}