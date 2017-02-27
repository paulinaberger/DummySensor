package org.mdsd2016.android.dummysensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {


    private static final String TAG_MAIN_ACTIVITY = "In-MainActivity";
    private SensorManager mSensorManager;
    private Sensor mProximitySensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        List<Sensor> sensorList = this.mSensorManager.getSensorList(Sensor.TYPE_ALL);

        printSensorList(sensorList);

        this.mProximitySensor = this.mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

    }

    private void printSensorList(List<Sensor> sensorList) {

        //sensor is a local variable

        StringBuilder sensorAvailableString = new StringBuilder();

        for (Sensor sensor: sensorList) {
            sensorAvailableString.append(sensor.getName()).append("\n");
        }

        // putting it to a TOAST
        Log.i(MainActivity.TAG_MAIN_ACTIVITY, sensorAvailableString.toString());
        Toast.makeText(this, sensorAvailableString.toString(), Toast.LENGTH_LONG).show();

    }

    @Override
    public void onSensorChanged (SensorEvent event){
        Log.i(MainActivity.TAG_MAIN_ACTIVITY, "Timestamp:"  + event.timestamp);
        Toast.makeText(this, "TimeStamp:" + event.timestamp, Toast.LENGTH_SHORT).show();

        long eventTime = event.timestamp;
  //      Toast.makeText(this, String.valueOf(eventTime), Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }

    @Override
    protected void onResume(){
        super.onResume();

        if(this.mProximitySensor !=null) {
            this.mSensorManager.registerListener(this,
                    this.mProximitySensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (this.mProximitySensor != null) {
            this.mSensorManager.unregisterListener(this, this.mProximitySensor);
        }
    }
}