package com.healthconnectdemo.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.InsertRecordsResponse
import androidx.health.connect.client.response.ReadRecordsResponse
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import com.healthconnectdemo.utils.InvalidRecordType
import com.healthconnectdemo.utils.reactRecordTypeToReactClassMap

class ReactHealthRecord {
  companion object {
    private fun <T : Record> createReactHealthRecordInstance(recordType: String?): ReactHealthRecordImpl<T> {
      if (!reactRecordTypeToReactClassMap.containsKey(recordType)) {
        throw InvalidRecordType()
      }

      val reactClass = reactRecordTypeToReactClassMap[recordType]
      return reactClass?.newInstance() as ReactHealthRecordImpl<T>
    }

    fun parseWriteRecords(reactRecords: ReadableArray): List<Record> {
      val recordType = reactRecords.getMap(0).getString("recordType")

      val recordClass = createReactHealthRecordInstance<Record>(recordType)

      return recordClass.parseWriteRecord(reactRecords)
    }

    fun parseWriteResponse(response: InsertRecordsResponse?): WritableNativeArray {
      val ids = WritableNativeArray()
      response?.recordIdsList?.forEach { ids.pushString(it) }
      return ids
    }

    fun parseReadRequest(recordType: String, reactRequest: ReadableMap): ReadRecordsRequest<*> {
      val recordClass = createReactHealthRecordInstance<Record>(recordType)

      return recordClass.parseReadRequest(reactRequest)
    }

    fun getAggregateRequest(recordType: String, reactRequest: ReadableMap): AggregateRequest {
      val recordClass = createReactHealthRecordInstance<Record>(recordType)

      return recordClass.getAggregateRequest(reactRequest)
    }

    fun parseAggregationResult(recordType: String, result: AggregationResult): WritableNativeMap {
      val recordClass = createReactHealthRecordInstance<Record>(recordType)

      return recordClass.parseAggregationResult(result)
    }

    fun parseReadResponse(
      recordType: String,
      response: ReadRecordsResponse<out Record>
    ): WritableNativeArray {
      val recordClass = createReactHealthRecordInstance<Record>(recordType)
      return recordClass.parseReadResponse(response)
    }
  }
}
