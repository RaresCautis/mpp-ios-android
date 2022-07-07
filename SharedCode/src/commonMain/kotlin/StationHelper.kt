package com.jetbrains.handson.mpp.mobile

import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import kotlinx.serialization.json.Json

class StationHelper {
    companion object {

        private val baseURL = "https://mobile-api-softwire2.lner.co.uk/"
        private val client = { HttpClient {
            install(JsonFeature) {
                serializer = KotlinxSerializer(Json.nonstrict)
            }}
        }

        suspend fun getStations(): List<StationDetails> {
            val url = "${baseURL}v1/stations"
            try {
                val stationList = client().get<Stations> { url(url) }
                return stationList.stations.filter { it.crs != null }
            }
            catch (ex:Exception){
                return emptyList()
            }
        }
    }
}