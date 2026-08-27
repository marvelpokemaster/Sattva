import { StyleSheet, View } from 'react-native';
import { StatusBar } from 'expo-status-bar';
import SattvaDomBridge from './src/SattvaDomBridge';

export default function App() {
  return (
    <View style={styles.container}>
      <StatusBar style="light" backgroundColor="#1E1B18" />
      <SattvaDomBridge 
        initialRoute="/"
        dom={{
          scrollEnabled: true,
          contentInsetAdjustmentBehavior: 'never',
          style: styles.bridge
        }}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#1E1B18',
  },
  bridge: {
    flex: 1,
    width: '100%',
    height: '100%',
    backgroundColor: '#FAF6EF',
  },
});
