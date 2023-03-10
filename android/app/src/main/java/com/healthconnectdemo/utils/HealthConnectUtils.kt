package com.healthconnectdemo.utils

import androidx.health.connect.client.records.*
import androidx.health.connect.client.records.metadata.DataOrigin
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.health.connect.client.units.Length
import androidx.health.connect.client.units.Mass
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import com.healthconnectdemo.records.*
import java.time.Instant
import kotlin.reflect.KClass

fun <T : Record> convertReactRequestOptionsFromJS(
  recordType: KClass<T>, options: ReadableMap
): ReadRecordsRequest<T> {
  return ReadRecordsRequest(
    recordType,
    timeRangeFilter = options.getTimeRangeFilter("timeRangeFilter"),
    dataOriginFilter = convertJsToDataOriginSet(options.getArray("dataOriginFilter")),
    ascendingOrder = options.getSafeBoolean("ascendingOrder", true),
    pageSize = options.getSafeInt("pageSize", 1000),
    pageToken = if (options.hasKey("pageToken")) options.getString("pageToken") else null,
  )
}

fun convertDataOriginsToJsArray(dataOrigin: Set<DataOrigin>): WritableNativeArray {
  return WritableNativeArray().apply {
    dataOrigin.forEach {
      pushString(it.packageName)
    }
  }
}

fun convertJsToDataOriginSet(readableArray: ReadableArray?): Set<DataOrigin> {
  if (readableArray == null) {
    return emptySet()
  }

  return readableArray.toArrayList().mapNotNull { DataOrigin(it.toString()) }.toSet()
}

fun convertProviderPackageNamesFromJS(providerPackageNames: ReadableArray): List<String> {
  return providerPackageNames.toArrayList().map { it.toString() }.toList()
}

fun ReadableArray.toMapList(): List<ReadableMap> {
  val list = mutableListOf<ReadableMap>()
  for (i in 0 until size()) {
    list.add(getMap(i))
  }
  return list
}

fun ReadableMap.getSafeInt(key: String, default: Int): Int {
  return if (this.hasKey(key)) this.getInt(key) else default
}

fun ReadableMap.getSafeBoolean(key: String, default: Boolean): Boolean {
  return if (this.hasKey(key)) this.getBoolean(key) else default
}

fun ReadableMap.getSafeString(key: String, default: String): String {
  return if (this.hasKey(key)) this.getString(key) ?: default else default
}

fun ReadableMap.getTimeRangeFilter(key: String? = null): TimeRangeFilter {
  val timeRangeFilter = if (key != null) this.getMap(key)
    ?: throw Exception("Time range filter should be provided") else this

  val operator = timeRangeFilter.getString("operator")

  val startTime =
    if (timeRangeFilter.hasKey("startTime")) Instant.parse(timeRangeFilter.getString("startTime")) else null

  val endTime =
    if (timeRangeFilter.hasKey("endTime")) Instant.parse(timeRangeFilter.getString("endTime")) else null

  when (operator) {
    "between" -> {
      if (startTime == null || endTime == null) {
        throw Exception("Start time and end time should be provided")
      }

      return TimeRangeFilter.between(startTime, endTime)
    }
    "after" -> {
      if (startTime == null) {
        throw Exception("Start time should be provided")
      }

      return TimeRangeFilter.after(startTime)
    }
    "before" -> {
      if (endTime == null) {
        throw Exception("End time should be provided")
      }

      return TimeRangeFilter.before(endTime)
    }
    else -> {
      if (startTime == null || endTime == null) {
        throw Exception("Start time and end time should be provided")
      }

      return TimeRangeFilter.between(startTime, endTime)
    }
  }
}

fun convertMetadataToJSMap(meta: Metadata): WritableNativeMap {
  return WritableNativeMap().apply {
    putString("id", meta.id)
    putString("clientRecordId", meta.clientRecordId)
    putDouble("clientRecordVersion", meta.clientRecordVersion.toDouble())
    putString("dataOrigin", meta.dataOrigin.packageName)
    putString("lastModifiedTime", meta.lastModifiedTime.toString())
    putInt("device", meta.device?.type ?: 0)
  }
}

val reactRecordTypeToClassMap: Map<String, KClass<out Record>> = mapOf(
  "ActiveCaloriesBurned" to ActiveCaloriesBurnedRecord::class,
  "BasalBodyTemperature" to BasalBodyTemperatureRecord::class,
  "BasalMetabolicRate" to BasalMetabolicRateRecord::class,
  "BloodGlucose" to BloodGlucoseRecord::class,
  "BloodPressure" to BloodPressureRecord::class,
  "BodyFat" to BodyFatRecord::class,
  "BodyTemperature" to BodyTemperatureRecord::class,
  "BodyWaterMass" to BodyWaterMassRecord::class,
  "BoneMass" to BoneMassRecord::class,
  "CervicalMucus" to CervicalMucusRecord::class,
  "CyclingPedalingCadence" to CyclingPedalingCadenceRecord::class,
  "Distance" to DistanceRecord::class,
  "ElevationGained" to ElevationGainedRecord::class,
  "ExerciseSession" to ExerciseSessionRecord::class,
  "FloorsClimbed" to FloorsClimbedRecord::class,
  "HeartRate" to HeartRateRecord::class,
  "HeartRateVariabilityRmssd" to HeartRateVariabilityRmssdRecord::class,
  "Height" to HeightRecord::class,
  "Hydration" to HydrationRecord::class,
  "LeanBodyMass" to LeanBodyMassRecord::class,
  "MenstruationFlow" to MenstruationFlowRecord::class,
  "Nutrition" to NutritionRecord::class,
  "OvulationTest" to OvulationTestRecord::class,
  "OxygenSaturation" to OxygenSaturationRecord::class,
  "Power" to PowerRecord::class,
  "RespiratoryRate" to RespiratoryRateRecord::class,
  "RestingHeartRate" to RestingHeartRateRecord::class,
  "SexualActivity" to SexualActivityRecord::class,
  "SleepSession" to SleepSessionRecord::class,
  "SleepStage" to SleepStageRecord::class,
  "Speed" to SpeedRecord::class,
  "StepsCadence" to StepsCadenceRecord::class,
  "Steps" to StepsRecord::class,
  "TotalCaloriesBurned" to TotalCaloriesBurnedRecord::class,
  "Vo2Max" to Vo2MaxRecord::class,
  "Weight" to WeightRecord::class,
  "WheelchairPushes" to WheelchairPushesRecord::class,
  "IntermenstrualBleeding" to IntermenstrualBleedingRecord::class,
  "MenstruationPeriod" to MenstruationPeriodRecord::class
)

val reactRecordTypeToReactClassMap: Map<String, Class<out ReactHealthRecordImpl<*>>> =
  mapOf(
    "ActiveCaloriesBurned" to ReactActiveCaloriesBurnedRecord::class.java,
    "BasalBodyTemperature" to ReactBasalBodyTemperatureRecord::class.java,
    "BasalMetabolicRate" to ReactBasalMetabolicRateRecord::class.java,
    "BloodGlucose" to ReactBloodGlucoseRecord::class.java,
    "BloodPressure" to ReactBloodPressureRecord::class.java,
    "BodyFat" to ReactBodyFatRecord::class.java,
    "BodyTemperature" to ReactBodyTemperatureRecord::class.java,
    "BodyWaterMass" to ReactBodyWaterMassRecord::class.java,
    "BoneMass" to ReactBoneMassRecord::class.java,
    "CervicalMucus" to ReactCervicalMucusRecord::class.java,
    "CyclingPedalingCadence" to ReactCyclingPedalingCadenceRecord::class.java,
    "Distance" to ReactDistanceRecord::class.java,
    "ElevationGained" to ReactElevationGainedRecord::class.java,
    "ExerciseSession" to ReactExerciseSessionRecord::class.java,
    "FloorsClimbed" to ReactFloorsClimbedRecord::class.java,
    "HeartRate" to ReactHeartRateRecord::class.java,
    "HeartRateVariabilityRmssd" to ReactHeartRateVariabilityRmssdRecord::class.java,
    "Height" to ReactHeightRecord::class.java,
    "Hydration" to ReactHydrationRecord::class.java,
    "LeanBodyMass" to ReactLeanBodyMassRecord::class.java,
    "MenstruationFlow" to ReactMenstruationFlowRecord::class.java,
    "Nutrition" to ReactNutritionRecord::class.java,
    "OvulationTest" to ReactOvulationTestRecord::class.java,
    "OxygenSaturation" to ReactOxygenSaturationRecord::class.java,
    "Power" to ReactPowerRecord::class.java,
    "RespiratoryRate" to ReactRespiratoryRateRecord::class.java,
    "RestingHeartRate" to ReactRestingHeartRateRecord::class.java,
    "SexualActivity" to ReactSexualActivityRecord::class.java,
    "SleepSession" to ReactSleepSessionRecord::class.java,
    "SleepStage" to ReactSleepStageRecord::class.java,
    "Speed" to ReactSpeedRecord::class.java,
    "StepsCadence" to ReactStepsCadenceRecord::class.java,
    "Steps" to ReactStepsRecord::class.java,
    "TotalCaloriesBurned" to ReactTotalCaloriesBurnedRecord::class.java,
    "Vo2Max" to ReactVo2MaxRecord::class.java,
    "Weight" to ReactWeightRecord::class.java,
    "WheelchairPushes" to ReactWheelchairPushesRecord::class.java,
    "IntermenstrualBleeding" to ReactIntermenstrualBleedingRecord::class.java,
    "MenstruationPeriod" to ReactMenstruationPeriodRecord::class.java
  )

fun massToJsMap(mass: Mass): WritableNativeMap {
  return WritableNativeMap().apply {
    putDouble("inGrams", mass.inGrams)
    putDouble("inKilograms", mass.inKilograms)
    putDouble("inMilligrams", mass.inMilligrams)
    putDouble("inMicrograms", mass.inMicrograms)
    putDouble("inOunces", mass.inOunces)
    putDouble("inPounds", mass.inPounds)
  }
}

fun getMassFromJsMap(massMap: ReadableMap?): Mass {
  if (massMap == null) {
    throw InvalidMass()
  }

  val value = massMap.getDouble("value")
  return when (massMap.getString("unit")) {
    "grams" -> Mass.grams(value)
    "kilograms" -> Mass.kilograms(value)
    "milligrams" -> Mass.milligrams(value)
    "micrograms" -> Mass.micrograms(value)
    "ounces" -> Mass.ounces(value)
    "pounds" -> Mass.pounds(value)
    else -> Mass.grams(value)
  }
}

fun getLengthFromJsMap(length: ReadableMap?): Length {
  if (length == null) {
    throw InvalidLength()
  }

  val value = length.getDouble("value")
  return when (length.getString("unit")) {
    "meters" -> Length.meters(value)
    "kilometers" -> Length.kilometers(value)
    "miles" -> Length.miles(value)
    "inches" -> Length.inches(value)
    "feet" -> Length.feet(value)
    else -> Length.meters(value)
  }
}

fun lengthToJsMap(length: Length): WritableNativeMap {
  return WritableNativeMap().apply {
    putDouble("inMeters", length.inMeters)
    putDouble("inKilometers", length.inKilometers)
    putDouble("inMiles", length.inMiles)
    putDouble("inInches", length.inInches)
    putDouble("inFeet", length.inFeet)
  }
}
