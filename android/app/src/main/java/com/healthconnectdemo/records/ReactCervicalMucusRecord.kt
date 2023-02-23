package com.healthconnectdemo.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.CervicalMucusRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.ReadRecordsResponse
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import com.healthconnectdemo.utils.*
import java.time.Instant

class ReactCervicalMucusRecord : ReactHealthRecordImpl<CervicalMucusRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<CervicalMucusRecord> {
    return records.toMapList().map {
      CervicalMucusRecord(
        time = Instant.parse(it.getString("time")),
        appearance = it.getSafeInt("appearance", CervicalMucusRecord.APPEARANCE_UNKNOWN),
        sensation = it.getSafeInt("sensation", CervicalMucusRecord.SENSATION_UNKNOWN),
        zoneOffset = null,
      )
    }
  }

  override fun parseReadResponse(response: ReadRecordsResponse<out CervicalMucusRecord>): WritableNativeArray {
    return WritableNativeArray().apply {
      for (record in response.records) {
        val reactMap = WritableNativeMap().apply {
          putString("time", record.time.toString())
          putInt("appearance", record.appearance)
          putInt("sensation", record.sensation)
          putMap("metadata", convertMetadataToJSMap(record.metadata))
        }
        pushMap(reactMap)
      }
    }
  }

  override fun parseReadRequest(options: ReadableMap): ReadRecordsRequest<CervicalMucusRecord> {
    return convertReactRequestOptionsFromJS(CervicalMucusRecord::class, options)
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    throw AggregationNotSupported()
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    throw AggregationNotSupported()
  }
}
