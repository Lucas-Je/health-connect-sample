import type {HeartRateRecord, RecordType} from './records.types';

interface HeartRateRecordResult extends HeartRateRecord {}

type HealthConnectRecordResult = HeartRateRecordResult;

export type RecordResult<T extends RecordType> = Omit<
  Extract<HealthConnectRecordResult, {recordType: T}>,
  'recordType'
>;
