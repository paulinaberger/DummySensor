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

public class MainActivity extends AppCompatActivity implements SensorEventListener { // interface, so you need to overrite methods


    private static final String TAG_MAIN_ACTIVITY = MainActivity.class.getSimpleName();
    private SensorManager mSensorManager;
    private Sensor mProximitySensor;
    private Sensor mAccelerometerSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //after inflating layout - we call the getSystemService and use a constant, string. Refers to service.
        //element retunrs a sensor (has to be cast to be a sensor manager), same as notification manager, handles sensor events
        //once we have the sensor defined (mSensor), then we use the sensor manager to do things (i.e. get sensor list)
        this.mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        List<Sensor> sensorList = this.mSensorManager.getSensorList(Sensor.TYPE_ALL); // <-- correstponding to any type of sensor

        printSensorList(sensorList);
        //sensorManager was used to get the DefaultSensor of the tupe proximity. IT queries the system, which is the default sensor for type proximity
        // returns a sensor (defined as a field in the class).
        this.mProximitySensor = this.mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY); //once we have a reference to the sensor we can use
        //we have to register to the sensor (then you want to subsrcibe to sensor (done in onResume and onPause)

        this.mAccelerometerSensor = this.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Log.d(MainActivity.TAG_MAIN_ACTIVITY, this.mAccelerometerSensor == null ? "Null" : "Available"); //like doing an if else in 1 line
        //need to subscribe in onResume
        Log.d(MainActivity.TAG_MAIN_ACTIVITY, "Range: " + this.mAccelerometerSensor.getMaximumRange()
                + ",Resolution:" + this.mAccelerometerSensor.getResolution());


    }

    private void printSensorList(List<Sensor> sensorList) { // < accepts list of sensors

        //sensor is a local variable
        //using StringBuilder to build the black message toast that we receive when we run the application
        //taking the sensor list and adding a for each loop. for every entry of the list, we are putting the name
        //in a string Builder. Allows to define a String to be created.
        StringBuilder sensorAvailableString = new StringBuilder();

        for (Sensor sensor: sensorList) {
            sensorAvailableString.append(sensor.getName()).append("\n"); //placing name of the sensor.
        }

        // putting it to a TOAST -- it becomes a sensor when we define it in the toString method.
        Log.i(MainActivity.TAG_MAIN_ACTIVITY, sensorAvailableString.toString());
        Toast.makeText(this, sensorAvailableString.toString(), Toast.LENGTH_LONG).show();

    }

    @Override
    public void onSensorChanged (SensorEvent event) { //if there is any change to any sensor that i am subcribed, make me aware
        //everytime that i place my finger close to the sensor, print the timestamp event.
        //want to see it in a log and toast
        //we should check if the event receive correspond to the event that we want

        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            Log.i(MainActivity.TAG_MAIN_ACTIVITY, "Timestamp:" + event.timestamp);
            Toast.makeText(this, "TimeStamp:" + event.timestamp, Toast.LENGTH_SHORT).show();

        }else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) { //sensor event brings Type, gets the event and values. So we
            //want to show value

            turnScreenColour(event);
            
            // Toast.makeText(this, "Values:" + event.values[0] + ", " + event.values[1] + ", " + event.values[2], Toast.LENGTH_SHORT).show(); //values is an array, because it's being put in a toast
            //that expects to have a string, says that it will apply the toString method. WE don't know how it's implemented as an array
            // we assume that is' being added as a value, value, value (done in X, Y)

            //long eventTime = event.timestamp;
            //Toast.makeText(this, String.valueOf(eventTime), Toast.LENGTH_SHORT).show();
        }
 }

    private void turnScreenColour(SensorEvent event) {
        //we need to calculate a Vector (represents condition). Some amounts are vectors (i.e. speed or velocity)

        float xAcc = event.values[0];
        float yAcc = event.values[1];
        float zAcc = event.values[2];

        double magnitude = (xAcc * xAcc + yAcc * yAcc + zAcc * zAcc)/(SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);

        if(magnitude > 5.0) {
            Log.d(MainActivity.TAG_MAIN_ACTIVITY, "Suffle!! Value:" + magnitude);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }

    @Override
    protected void onResume(){ // everytime aplication is run you want to subscribe to sensor
        super.onResume();

        //checking if proximity sensor is availbale, if it is, then you want to register the listener.
        //if it's avaible then you want to unregister.

        //you have to also make note of the dealy, if you want to receive updates more or less oftten. Suggestive value.

        if(this.mProximitySensor !=null) {
            this.mSensorManager.registerListener(this,
                    this.mProximitySensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (this.mAccelerometerSensor != null){
            this.mSensorManager.registerListener(this,
                    this.mAccelerometerSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (this.mProximitySensor != null) {
            this.mSensorManager.unregisterListener(this, this.mProximitySensor); //you want to unsubscribe the listener
        }

        //same process, checking, registering and unregistering
        if (this.mAccelerometerSensor != null) {
            this.mSensorManager.unregisterListener(this, this.mAccelerometerSensor); //you want to unsubscribe the listener
        }
    }
}