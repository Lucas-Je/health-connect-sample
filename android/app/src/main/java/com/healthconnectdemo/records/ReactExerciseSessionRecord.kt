package com.healthconnectdemo.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.ReadRecordsResponse
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import com.healthconnectdemo.utils.*
import java.time.Instant

class ReactExerciseSessionRecord : ReactHealthRecordImpl<ExerciseSessionRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<ExerciseSessionRecord> {
    return records.toMapList().map {
      ExerciseSessionRecord(
        startTime = Instant.parse(it.getString("startTime")),
        endTime = Instant.parse(it.getString("endTime")),
        startZoneOffset = null,
        endZoneOffset = null,
        exerciseType = it.getSafeInt(
          "exerciseType", ExerciseSessionRecord.EXERCISE_TYPE_OTHER_WORKOUT
        ),
        notes = it.getString("notes"),
        title = it.getString("title"),
      )
    }
  }

  override fun parseReadResponse(response: ReadRecordsResponse<out ExerciseSessionRecord>): WritableNativeArray {
    return WritableNativeArray().apply {
      for (record in response.records) {
        val reactMap = WritableNativeMap().apply {
          putString("startTime", record.startTime.toString())
          putString("endTime", record.endTime.toString())
          putString("notes", record.notes)
          putString("title", record.title)
          putInt("exerciseType", record.exerciseType)
          putMap("metadata", convertMetadataToJSMap(record.metadata))
        }
        pushMap(reactMap)
      }
    }
  }

  override fun parseReadRequest(options: ReadableMap): ReadRecordsRequest<ExerciseSessionRecord> {
    return convertReactRequestOptionsFromJS(ExerciseSessionRecord::class, options)
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    return AggregateRequest(
      metrics = setOf(ExerciseSessionRecord.EXERCISE_DURATION_TOTAL),
      timeRangeFilter = record.getTimeRangeFilter("timeRangeFilter"),
      dataOriginFilter = convertJsToDataOriginSet(record.getArray("dataOriginFilter"))
    )
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    return WritableNativeMap().apply {
      val map = WritableNativeMap().apply {
        putDouble(
          "inSeconds",
          record[ExerciseSessionRecord.EXERCISE_DURATION_TOTAL]?.seconds?.toDouble() ?: 0.0
        )
      }
      putMap("EXERCISE_DURATION_TOTAL", map)
      putArray("dataOrigins", convertDataOriginsToJsArray(record.dataOrigins))
    }
  }
}
