package com.healthconnectdemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.health.connect.client.HealthConnectClient
import com.facebook.react.bridge.*
import com.healthconnectdemo.permissions.HCPermissionManager
import com.healthconnectdemo.records.ReactHealthRecord
import com.healthconnectdemo.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HealthConnectManager(private val context: ReactApplicationContext) : ActivityEventListener {
  private lateinit var healthConnectClient: HealthConnectClient
  private val coroutineScope = CoroutineScope(Dispatchers.IO)
  private var pendingPromise: Promise? = null
  private var latestPermissions: Set<String>? = null

  private val isInitialized get() = this::healthConnectClient.isInitialized

  private inline fun throwUnlessClientIsAvailable(promise: Promise, block: () -> Unit) {
    if (!isInitialized) {
      return promise.rejectWithException(ClientNotInitialized())
    }
    block()
  }

  override fun onActivityResult(
    activity: Activity?, requestCode: Int, resultCode: Int, intent: Intent?
  ) {
    if (requestCode == REQUEST_CODE) {
      HCPermissionManager.parseOnActivityResult(resultCode, intent, pendingPromise)
    }
  }

  override fun onNewIntent(intent: Intent?) {}

  fun checkHealthConnectSupported(providerPackageNames: ReadableArray, promise: Promise) {

    var result = WritableNativeMap().apply {
      putBoolean("isApiSupported", HealthConnectClient.isApiSupported())
      putBoolean("healthPlatformInstalled", HealthConnectClient.isProviderAvailable(
        context, convertProviderPackageNamesFromJS(providerPackageNames)
      ))
    }
    return promise.resolve(result)
  }

  fun isAvailable(providerPackageNames: ReadableArray, promise: Promise) {

    val available = HealthConnectClient.isProviderAvailable(
      context, convertProviderPackageNamesFromJS(providerPackageNames)
    )
    return promise.resolve(available)
  }


  fun initialize(providerPackageNames: ReadableArray, promise: Promise) {
    try {
      healthConnectClient = HealthConnectClient.getOrCreate(
        context, convertProviderPackageNamesFromJS(providerPackageNames)
      )
      promise.resolve(true)
    } catch (e: Exception) {
      promise.rejectWithException(e)
    }
  }

  fun requestPermission(
    reactPermissions: ReadableArray, providerPackageName: String, promise: Promise
  ) {
    throwUnlessClientIsAvailable(promise) {
      this.pendingPromise = promise
      this.latestPermissions = HCPermissionManager.parsePermissions(reactPermissions)

      val bundle = Bundle().apply {
        putString("providerPackageName", providerPackageName)
      }

      val intent = HCPermissionManager(providerPackageName).healthPermissionContract.createIntent(
        context, latestPermissions!!
      )
      context.startActivityForResult(intent, HealthConnectManager.REQUEST_CODE, bundle)
    }
  }

  fun revokeAllPermissions(promise: Promise) {
    throwUnlessClientIsAvailable(promise) {
      coroutineScope.launch {
        healthConnectClient.permissionController.revokeAllPermissions()
      }
    }
  }

  fun getGrantedPermissions(promise: Promise) {
    throwUnlessClientIsAvailable(promise) {
      coroutineScope.launch {
        promise.resolve(HCPermissionManager.getGrantedPermissions(healthConnectClient.permissionController))
      }
    }
  }

  fun insertRecords(reactRecords: ReadableArray, promise: Promise) {
    throwUnlessClientIsAvailable(promise) {
      coroutineScope.launch {
        try {
          val records = ReactHealthRecord.parseWriteRecords(reactRecords)
          val response = healthConnectClient.insertRecords(records)
          promise.resolve(ReactHealthRecord.parseWriteResponse(response))
        } catch (e: Exception) {
          promise.rejectWithException(e)
        }
      }
    }
  }

  fun readRecords(recordType: String, options: ReadableMap, promise: Promise) {
    throwUnlessClientIsAvailable(promise) {
      coroutineScope.launch {
        try {
          val request = ReactHealthRecord.parseReadRequest(recordType, options)
          val response = healthConnectClient.readRecords(request)
          promise.resolve(ReactHealthRecord.parseReadResponse(recordType, response))
        } catch (e: Exception) {
          promise.rejectWithException(e)
        }
      }
    }
  }

  fun aggregateRecord(record: ReadableMap, promise: Promise) {
    throwUnlessClientIsAvailable(promise) {
      coroutineScope.launch {
        try {
          val recordType = record.getString("recordType") ?: ""
          val response = healthConnectClient.aggregate(
            ReactHealthRecord.getAggregateRequest(
              recordType, record
            )
          )
          promise.resolve(ReactHealthRecord.parseAggregationResult(recordType, response))
        } catch (e: Exception) {
          promise.rejectWithException(e)
        }
      }
    }
  }

  fun deleteRecordsByUuids(
    recordType: String,
    recordIdsList: ReadableArray,
    clientRecordIdsList: ReadableArray,
    promise: Promise
  ) {
    throwUnlessClientIsAvailable(promise) {
      coroutineScope.launch {
        val record = reactRecordTypeToClassMap[recordType]
        if (record != null) {
          healthConnectClient.deleteRecords(
            recordType = record,
            recordIdsList = recordIdsList.toArrayList().mapNotNull { it.toString() }.toList(),
            clientRecordIdsList = if (clientRecordIdsList.size() > 0) clientRecordIdsList.toArrayList()
              .mapNotNull { it.toString() }.toList() else emptyList()
          )
        }
      }
    }
  }

  fun deleteRecordsByTimeRange(
    recordType: String, timeRangeFilter: ReadableMap, promise: Promise
  ) {
    throwUnlessClientIsAvailable(promise) {
      coroutineScope.launch {
        val record = reactRecordTypeToClassMap[recordType]
        if (record != null) {
          healthConnectClient.deleteRecords(
            recordType = record, timeRangeFilter = timeRangeFilter.getTimeRangeFilter()
          )
        }
      }
    }
  }

  companion object {
    const val REQUEST_CODE = 150
  }

  init {
    context.addActivityEventListener(this)
  }
}

