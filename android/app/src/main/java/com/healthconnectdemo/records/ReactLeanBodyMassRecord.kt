package com.healthconnectdemo.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.LeanBodyMassRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.ReadRecordsResponse
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import com.healthconnectdemo.records.ReactHealthRecordImpl

class ReactLeanBodyMassRecord: ReactHealthRecordImpl<LeanBodyMassRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<LeanBodyMassRecord> {
    TODO("Not yet implemented")
  }

  override fun parseReadResponse(response: ReadRecordsResponse<out LeanBodyMassRecord>): WritableNativeArray {
    TODO("Not yet implemented")
  }

  override fun parseReadRequest(options: ReadableMap): ReadRecordsRequest<LeanBodyMassRecord> {
    TODO("Not yet implemented")
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    TODO("Not yet implemented")
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    TODO("Not yet implemented")
  }

}
