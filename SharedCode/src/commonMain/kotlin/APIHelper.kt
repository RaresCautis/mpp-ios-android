package com.jetbrains.handson.mpp.mobile

import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import io.ktor.client.features.*

class APIHelper {
    companion object {

        private val baseURL = "https://mobile-api-softwire2.lner.co.uk/v1/"
        private val client = { HttpClient {
            install(JsonFeature) {
                serializer = KotlinxSerializer(Json.nonstrict)
            }}
        }

        suspend fun getStations(): List<StationDetails> {
            val url = "${baseURL}stations"
            try {
                val stationList = client().get<Stations> { url(url) }
                return stationList.stations.filter { it.crs != null }
            }
            catch (e:Exception){
                return emptyList()
            }
        }

        suspend fun getAPIData(
            originCrs: String,
            destinationCrs: String,
            dateTime: String,
            adultCount: String,
            childCount: String
        ): DepartureDetails {
            val url = URLBuilder("${baseURL}fares").apply {
                parameters["originStation"] = originCrs
                parameters["destinationStation"] = destinationCrs
                parameters["outboundDateTime"] = dateTime
                parameters["numberOfChildren"] = childCount
                parameters["numberOfAdults"] = adultCount
            }.build()

            return client().get {
                url(url)
            }
        }

        suspend fun getAPIError(e: ResponseException): String{
            val responseText = e.response.readText()
            val json = Json(JsonConfiguration.Stable)
            val description = json.parse(ErrorResponse.serializer(), responseText)
            return description.error_description
        }
    }
}