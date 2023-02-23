package com.healthconnectdemo.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.BasalBodyTemperatureRecord
import androidx.health.connect.client.records.BodyTemperatureMeasurementLocation
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.ReadRecordsResponse
import androidx.health.connect.client.units.Temperature
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import com.healthconnectdemo.utils.*
import java.time.Instant

class ReactBasalBodyTemperatureRecord : ReactHealthRecordImpl<BasalBodyTemperatureRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<BasalBodyTemperatureRecord> {
    return records.toMapList().map {
      BasalBodyTemperatureRecord(
        time = Instant.parse(it.getString("time")),
        zoneOffset = null,
        temperature = getTemperatureFromJsMap(it.getMap("temperature")),
        measurementLocation = it.getSafeInt(
          "measurementLocation",
          BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_UNKNOWN
        )
      )
    }
  }

  override fun parseReadResponse(response: ReadRecordsResponse<out BasalBodyTemperatureRecord>): WritableNativeArray {
    return WritableNativeArray().apply {
      for (record in response.records) {
        val reactMap = WritableNativeMap().apply {
          putString("time", record.time.toString())
          putInt("measurementLocation", record.measurementLocation)
          putMap("temperature", temperatureToJsMap(record.temperature))
          putMap("metadata", convertMetadataToJSMap(record.metadata))
        }
        pushMap(reactMap)
      }
    }
  }

  override fun parseReadRequest(options: ReadableMap): ReadRecordsRequest<BasalBodyTemperatureRecord> {
    return convertReactRequestOptionsFromJS(BasalBodyTemperatureRecord::class, options)
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    throw AggregationNotSupported()
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    throw AggregationNotSupported()
  }

  private fun getTemperatureFromJsMap(temperatureMap: ReadableMap?): Temperature {
    if (temperatureMap == null) {
      throw InvalidTemperature()
    }

    val value = temperatureMap.getDouble("value")
    return when (temperatureMap.getString("unit")) {
      "fahrenheit" -> Temperature.fahrenheit(value)
      "celsius" -> Temperature.celsius(value)
      else -> Temperature.celsius(value)
    }
  }

  private fun temperatureToJsMap(temperature: Temperature): WritableNativeMap {
    return WritableNativeMap().apply {
      putDouble("inFahrenheit", temperature.inFahrenheit)
      putDouble("inCelsius", temperature.inCelsius)
    }
  }
}