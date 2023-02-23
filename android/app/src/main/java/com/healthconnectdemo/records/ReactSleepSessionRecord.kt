package com.healthconnectdemo.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.SleepSessionRecord
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
import java.time.Instant


class ReactSleepSessionRecord : ReactHealthRecordImpl<SleepSessionRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<SleepSessionRecord> {
    TODO("Not yet implemented")
  }


  override fun parseReadResponse(response: ReadRecordsResponse<out SleepSessionRecord>): WritableNativeArray {
    Log.d("ReactSleepSessionRecord", "parseReadResponse")
    return WritableNativeArray().apply {
      for (record in response.records) {
        val reactMap = WritableNativeMap().apply {
          putString("startTime", record.startTime.toString())
          putString("endTime", record.endTime.toString())
          putString("title", record.title)
          putString("notes", record.notes)
//          val array = WritableNativeArray().apply {
//            record.stages.map {
//              val map = WritableNativeMap()
//              map.putString("startTime", it.startTime.toString())
//              map.putString("endTime", it.endTime.toString())
//              map.putString("stage", it.stage.toString())
//              map.putMap("metadata", convertMetadataToJSMap(it.metadata))
//              this.pushMap(map)
//            }
//          }
          putMap("metadata", convertMetadataToJSMap(record.metadata))
        }
        pushMap(reactMap)
      }
    }
  }

  override fun parseReadRequest(options: ReadableMap): ReadRecordsRequest<SleepSessionRecord> {
    return convertReactRequestOptionsFromJS(SleepSessionRecord::class, options)
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    TODO("Not yet implemented")
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    TODO("Not yet implemented")
  }
}
