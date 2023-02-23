package com.healthconnectdemo

import com.facebook.react.bridge.*

abstract class HealthConnectSpec internal constructor(context: ReactApplicationContext) :
  ReactContextBaseJavaModule(context) {

  @ReactMethod
  abstract fun isAvailable(providerPackageNames: ReadableArray, promise: Promise)

  @ReactMethod
  abstract fun initialize(providerPackageNames: ReadableArray, promise: Promise);

  @ReactMethod
  abstract fun requestPermission(permissions: ReadableArray, providerPackageName: String, promise: Promise);

  @ReactMethod
  abstract fun getGrantedPermissions(promise: Promise);

  @ReactMethod
  abstract fun revokeAllPermissions(promise: Promise);

  @ReactMethod
  abstract fun insertRecords(records: ReadableArray, promise: Promise);

  @ReactMethod
  abstract fun readRecords(recordType: String, options: ReadableMap, promise: Promise);

  @ReactMethod
  abstract fun aggregateRecord(record: ReadableMap, promise: Promise);
  
  @ReactMethod
  abstract fun deleteRecordsByUuids(recordType: String, recordIdsList: ReadableArray, clientRecordIdsList: ReadableArray, promise: Promise);

  @ReactMethod
  abstract fun deleteRecordsByTimeRange(recordType: String, timeRangeFilter: ReadableMap, promise: Promise);
}