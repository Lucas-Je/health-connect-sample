import {NativeModules, Platform} from 'react-native';

import {HealthConnectError} from './errors';
import type {
  AggregateRequest,
  AggregateResult,
  AggregateResultRecordType,
  HealthConnectRecord,
  Permission,
  ReadRecordsOptions,
  RecordResult,
  RecordType,
} from './types';
import type {TimeRangeFilter} from './types/base.types';
import {ConnectSupported} from './types';

const LINKING_ERROR =
  "The package 'react-native-health-connect' doesn't seem to be linked. Make sure: \n\n" +
  Platform.select({ios: "- You have run 'pod install'\n", default: ''}) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const PLATFORM_NOT_SUPPORTED_ERROR =
  'Platform not supported. This package only supports Android.';

// @ts-expect-error
const isTurboModuleEnabled = global.__turboModuleProxy != null;

const moduleProxy = (message: string) =>
  new Proxy(
    {},
    {
      get() {
        throw new Error(message);
      },
    },
  );

const HealthConnectModule = Platform.select({
  android: isTurboModuleEnabled
    ? require('./NativeHealthConnect').default
    : NativeModules.HealthConnect,
  ios: moduleProxy(PLATFORM_NOT_SUPPORTED_ERROR),
  default: moduleProxy(PLATFORM_NOT_SUPPORTED_ERROR),
});

const HealthConnect = HealthConnectModule
  ? HealthConnectModule
  : moduleProxy(LINKING_ERROR);

const DEFAULT_PROVIDER_PACKAGE_NAME = 'com.google.android.apps.healthdata';

export function checkHealthConnectSupported(
  providerPackageNames: string[] = [DEFAULT_PROVIDER_PACKAGE_NAME],
): Promise<ConnectSupported> {
  return HealthConnect.checkHealthConnectSupported(providerPackageNames);
}
export function initialize(
  providerPackageNames: string[] = [DEFAULT_PROVIDER_PACKAGE_NAME],
): Promise<boolean> {
  return HealthConnect.initialize(providerPackageNames);
}

/**
 * Request permissions to access Health Connect data
 * @param permissions list of permissions to request
 * @returns granted permissions
 */
export function requestPermissions(
  permissions: Permission[],
  providerPackageName = DEFAULT_PROVIDER_PACKAGE_NAME,
): Promise<Permission> {
  return HealthConnect.requestPermissions(permissions, providerPackageName);
}

export function getGrantedPermissions(): Promise<Permission> {
  return HealthConnect.getGrantedPermissions();
}

export function revokeAllPermissions(): void {
  return HealthConnect.revokeAllPermissions();
}

export function readRecords<T extends RecordType>(
  recordType: T,
  options: ReadRecordsOptions,
): Promise<RecordResult<T>[]> {
  return HealthConnect.readRecords(recordType, options);
}

export function getHeartRates<T extends RecordType>(
  options: ReadRecordsOptions,
): Promise<RecordResult<T>[]> {
  return HealthConnect.getHeartRates(options);
}

export function insertRecords(
  records: HealthConnectRecord[],
): Promise<string[]> {
  if (records.length === 0) {
    throw new HealthConnectError(
      'You must provide at least one record',
      'insertRecords',
    );
  }

  const recordTypes = records.map(record => record.recordType);
  const uniqueRecordTypes = new Set(recordTypes);
  if (uniqueRecordTypes.size > 1) {
    throw new HealthConnectError(
      'All records must have the same type',
      'insertRecords',
    );
  }

  return HealthConnect.insertRecords(records);
}

export function aggregateRecord<T extends AggregateResultRecordType>(
  request: AggregateRequest<T>,
): Promise<AggregateResult<T>> {
  return HealthConnect.aggregateRecord(request);
}

export function deleteRecordsByUuids(
  recordType: RecordType,
  recordIdsList: string[],
  clientRecordIdsList: string[],
): Promise<void> {
  return HealthConnect.deleteRecordsByUuids(
    recordType,
    recordIdsList,
    clientRecordIdsList,
  );
}

export function deleteRecordsByTimeRange(
  recordType: RecordType,
  timeRangeFilter: TimeRangeFilter,
): Promise<void> {
  return HealthConnect.deleteRecordsByTimeRange(recordType, timeRangeFilter);
}

export * from './constants';
