package com.healthconnectdemo.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.SleepStageRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.ReadRecordsResponse
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import android.util.Log
import com.healthconnectdemo.utils.*
class ReactSleepStageRecord: ReactHealthRecordImpl<SleepStageRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<SleepStageRecord> {
    TODO("Not yet implemented")
  }

  override fun parseReadResponse(response: ReadRecordsResponse<out SleepStageRecord>): WritableNativeArray {
    Log.d("ReactSleepStageRecord", "parseReadResponse")
    return WritableNativeArray().apply {
      for (record in response.records) {
        val reactMap = WritableNativeMap().apply {
          putString("startTime", record.startTime.toString())
          putString("endTime", record.endTime.toString())
          putInt("stage", record.stage)
          putMap("metadata", convertMetadataToJSMap(record.metadata))
        }
        pushMap(reactMap)
      }
    }

  }

  override fun parseReadRequest(options: ReadableMap): ReadRecordsRequest<SleepStageRecord> {
    return convertReactRequestOptionsFromJS(SleepStageRecord::class, options)

  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    TODO("Not yet implemented")
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    TODO("Not yet implemented")
  }
}
