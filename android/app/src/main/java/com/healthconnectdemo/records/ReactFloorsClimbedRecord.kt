package com.healthconnectdemo.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.FloorsClimbedRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.ReadRecordsResponse
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import com.healthconnectdemo.utils.*
import java.time.Instant

class ReactFloorsClimbedRecord : ReactHealthRecordImpl<FloorsClimbedRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<FloorsClimbedRecord> {
    return records.toMapList().map {
      FloorsClimbedRecord(
        startTime = Instant.parse(it.getString("startTime")),
        endTime = Instant.parse(it.getString("endTime")),
        startZoneOffset = null,
        endZoneOffset = null,
        floors = it.getDouble("floors")
      )
    }
  }

  override fun parseReadResponse(response: ReadRecordsResponse<out FloorsClimbedRecord>): WritableNativeArray {
    return WritableNativeArray().apply {
      for (record in response.records) {
        val reactMap = WritableNativeMap().apply {
          putString("startTime", record.startTime.toString())
          putString("endTime", record.endTime.toString())
          putDouble("floors", record.floors)
          putMap("metadata", convertMetadataToJSMap(record.metadata))
        }
        pushMap(reactMap)
      }
    }
  }

  override fun parseReadRequest(options: ReadableMap): ReadRecordsRequest<FloorsClimbedRecord> {
    return convertReactRequestOptionsFromJS(FloorsClimbedRecord::class, options)
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    return AggregateRequest(
      metrics = setOf(FloorsClimbedRecord.FLOORS_CLIMBED_TOTAL),
      timeRangeFilter = record.getTimeRangeFilter("timeRangeFilter"),
      dataOriginFilter = convertJsToDataOriginSet(record.getArray("dataOriginFilter"))
    )
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    return WritableNativeMap().apply {
      putDouble("FLOORS_CLIMBED_TOTAL", record[FloorsClimbedRecord.FLOORS_CLIMBED_TOTAL] ?: 0.0)
      putArray("dataOrigins", convertDataOriginsToJsArray(record.dataOrigins))
    }
  }
}
