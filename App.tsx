/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import React, {useEffect} from 'react';
import {Animated, SafeAreaView, Text, useColorScheme} from 'react-native';

import {Colors} from 'react-native/Libraries/NewAppScreen';

import {
  checkHealthConnectSupported,
  initialize,
  readRecords,
  requestPermissions,
} from './HealthConnect';
import View = Animated.View;

// @ts-ignore
function App(): JSX.Element {
  const isDarkMode = useColorScheme() === 'dark';
  const endTime = new Date().toISOString();
  const connectHealthModule = async () => {
    console.log('start check available');
    const check = await checkHealthConnectSupported();
    console.log('check available result: ', check);
    if (check.healthPlatformInstalled && check.isApiSupported) {
      console.log('start initialize');
      const isInitialized = await initialize();
      console.log('Initialization Result: ', isInitialized);
      if (isInitialized) {
        console.log('start  requestPermissions');
        const grantedPermissions = await requestPermissions([
          {accessType: 'read', recordType: 'HeartRate'},
          {accessType: 'read', recordType: 'RestingHeartRate'},
          {accessType: 'read', recordType: 'SleepSession'},
          {accessType: 'read', recordType: 'SleepStage'},
          {accessType: 'read', recordType: 'Steps'},
        ]);
        console.log('grantedPermissions result: ', grantedPermissions);

        console.log('start get sample data');
        const heartRate = await readRecords('HeartRate', {
          timeRangeFilter: {
            operator: 'between',
            startTime: '2023-01-09T12:00:00.405Z',
            endTime: endTime,
          },
        });
        console.log('heartRateRec', heartRate);

        const restingHeartRate = await readRecords('RestingHeartRate', {
          timeRangeFilter: {
            operator: 'between',
            startTime: '2023-01-09T12:00:00.405Z',
            endTime: endTime,
          },
        });

        console.log('restingHeartRate', restingHeartRate);

        const steps = await readRecords('Steps', {
          timeRangeFilter: {
            operator: 'between',
            startTime: '2023-01-09T12:00:00.405Z',
            endTime: endTime,
          },
        });

        console.log('Steps', steps);

        const sleepSession = await readRecords('SleepSession', {
          timeRangeFilter: {
            operator: 'between',
            startTime: '2023-01-09T12:00:00.405Z',
            endTime: endTime,
          },
        });

        console.log('sleepSession', sleepSession);

        const sleepStage = await readRecords('SleepStage', {
          timeRangeFilter: {
            operator: 'between',
            startTime: '2023-01-09T12:00:00.405Z',
            endTime: endTime,
          },
        });

        console.log('sleepStage', sleepStage);
      };
    }
  }


  useEffect(() => {
    connectHealthModule().then(() => console.log('finish get sample data'));
  }, []);

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };

  return (
    <SafeAreaView style={backgroundStyle}>
      <View
        style={{
          flexDirection: 'row',
          alignItems: 'center',
          justifyContent: 'center',
          backgroundColor: 'yellowGreen',
          minHeight: '100%',
        }}>
        <Text>health connect</Text>
      </View>
    </SafeAreaView>
  );
}

export default App;
