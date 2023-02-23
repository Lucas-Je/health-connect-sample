import type {RecordType} from './records.types';
import {checkHealthConnectSupported} from '../index';

export interface Permission {
  accessType: 'read' | 'write';
  recordType: RecordType;
}

export interface ConnectSupported {
  isApiSupported: Boolean;
  healthPlatformInstalled: Boolean;
}

export * from './records.types';
export * from './results.types';
export * from './aggregate.types';
