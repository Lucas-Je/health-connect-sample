import type {
  IntervalRecord,
  TimeRangeFilter,
  HeartRateSample,
} from './base.types';

export interface HeartRateRecord extends IntervalRecord {
  recordType: 'HeartRate';
  samples: HeartRateSample[];
}

export interface SleepSessionRecord extends IntervalRecord {
  recordType: 'SleepSession';
}

export interface SleepStageRecord extends IntervalRecord {
  recordType: 'SleepStage';
}

export interface StepsRecord extends IntervalRecord {
  recordType: 'Steps';
}
export interface RestingHeartRate extends IntervalRecord {
  recordType: 'RestingHeartRate';
}

export type HealthConnectRecord =
  | SleepSessionRecord
  | SleepStageRecord
  | StepsRecord
  | RestingHeartRate
  | HeartRateRecord;

export type RecordType = HealthConnectRecord['recordType'];

export interface ReadRecordsOptions {
  timeRangeFilter: TimeRangeFilter;
  dataOriginFilter?: string[];
  ascendingOrder?: boolean;
  pageSize?: number;
  pageToken?: string;
}
