# health-connect-sample
## react native bridge를 활용한 health-connect data 처리

### google health connect docs
> https://developer.android.com/guide/health-and-fitness/health-connect/get-started?hl=ko 

#### 참고 source code
> https://github.com/android/health-samples/tree/main/health-connect/HealthConnectSample  
> https://github.com/matinzd/react-native-health-connect

데이터를 가져오는 함수는 readRecords 입니다.
```
readRecords(
  recordType: string,
  options: ReadRecordsOptions,
): Promise<Array<{}>>;
```

heart rate 예제
```
const endTime = new Date().toISOString();
const heartRate = await readRecords('HeartRate', {
  timeRangeFilter: {
  operator: 'between',
    startTime: '2023-01-09T12:00:00.405Z',
    endTime: endTime,
  },
});
```

현재 동작하는 data 는 **HeartRate**, **RestingHeartRate**, **SleepSession**, **SleepStage**, **Steps** 이며 구현된 소스코드를 참조하여 확장 할 수 있습니다.
