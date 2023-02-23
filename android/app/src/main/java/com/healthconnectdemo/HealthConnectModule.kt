package com.healthconnectdemo

import com.facebook.react.bridge.*


class HealthConnectModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    private val manager = HealthConnectManager(reactContext)

  override fun getName(): String {
    return "HealthConnectModule"
  }

  @ReactMethod
  fun checkHealthConnectSupported(providerPackageNames: ReadableArray, promise: Promise) {
    return manager.checkHealthConnectSupported(providerPackageNames, promise)
  }

  @ReactMethod
  fun initialize(providerPackageNames: ReadableArray, promise: Promise) {
    return manager.initialize(providerPackageNames, promise)
  }

  @ReactMethod
   fun requestPermissions(
    permissions: ReadableArray,
    providerPackageName: String,
    promise: Promise
  ) {
    return manager.requestPermission(permissions, providerPackageName, promise)
  }

  @ReactMethod
   fun getGrantedPermissions(promise: Promise) {
    return manager.getGrantedPermissions(promise)
  }

  @ReactMethod
   fun revokeAllPermissions(promise: Promise) {
    return manager.revokeAllPermissions(promise)
  }

  @ReactMethod
   fun insertRecords(records: ReadableArray, promise: Promise) {
    return manager.insertRecords(records, promise)
  }


    @ReactMethod
    fun getHeartRates(options: ReadableMap, promise: Promise) {
        return manager.readRecords(recordType, options, promise)
    }
  @ReactMethod
   fun readRecords(recordType: String, options: ReadableMap, promise: Promise) {
    return manager.readRecords(recordType, options, promise)
  }

  @ReactMethod
   fun aggregateRecord(record: ReadableMap, promise: Promise) {
    return manager.aggregateRecord(record, promise)
  }

   fun deleteRecordsByUuids(
    recordType: String,
    recordIdsList: ReadableArray,
    clientRecordIdsList: ReadableArray,
    promise: Promise
  ) {
    return manager.deleteRecordsByUuids(recordType, recordIdsList, clientRecordIdsList, promise)
  }

   fun deleteRecordsByTimeRange(
    recordType: String,
    timeRangeFilter: ReadableMap,
    promise: Promise
  ) {
    return manager.deleteRecordsByTimeRange(recordType, timeRangeFilter, promise)
  }

  companion object {
    const val NAME = "HealthConnect"
  }
}

