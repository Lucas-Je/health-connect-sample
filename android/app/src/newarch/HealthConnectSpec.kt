package com.healthconnectdemo

import com.facebook.react.bridge.ReactApplicationContext

abstract class HealthConnectSpec internal constructor(context: ReactApplicationContext) :
  NativeHealthConnectSpec(context) {
}
