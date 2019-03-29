package br.com.bossini.fatec_ipi_tarde_paint_for_android;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainActivityFragment extends Fragment {

    private DoodleView doodleView;
    private float acceleration;
    private float lastAcceleration;
    private float currentAcceleration;
    private boolean dialogOnScreen = false;
    private static final int ACCELERATION_THRESHOLD = 1000000;
    private static final int SAVE_IMAGE_PERMISSION_REQUEST_CODE = 1;


    @Override
    public void onResume() {
        super.onResume();
        enableAccelerometerListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        disableAccelerometerListening();
    }

    private void disableAccelerometerListening (){
        SensorManager manager = (SensorManager)
                getActivity().getSystemService(Context.SENSOR_SERVICE);
        manager.
                unregisterListener(sensonEventListener,
                manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) );
    }

    private void enableAccelerometerListening(){
        SensorManager manager = (SensorManager)
                getActivity().getSystemService(Context.SENSOR_SERVICE);

        manager.registerListener(
                sensonEventListener,
                manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                        SensorManager.SENSOR_DELAY_NORMAL
        );

    }


    private void confirmErase (){
        EraseImageDialogFragment eraseImageDialogFragment =
                new EraseImageDialogFragment();
        eraseImageDialogFragment.show(getFragmentManager(), "erase fragment");
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.
                inflate(R.layout.fragment_main, container, false);
        setHasOptionsMenu(true);
        doodleView = v.findViewById(R.id.doodleView);
        currentAcceleration = SensorManager.GRAVITY_EARTH;
        lastAcceleration = SensorManager.GRAVITY_EARTH;
        acceleration = 0.0f;
        return v;

    }
    private final SensorEventListener sensonEventListener =
            new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    if (!dialogOnScreen){
                        String TAG = "TESTEACELEROMETRO";
                        float x = event.values[0];
                        float y = event.values[1];
                        float z = event.values[2];
                        Log.i(TAG, "x: " + x);
                        Log.i(TAG, "y: " + y);
                        Log.i(TAG, "z: " + z);

                        lastAcceleration = currentAcceleration;
                        currentAcceleration =
                                x * x + y * y + z * z;
                        acceleration =
                                currentAcceleration *
                                        (currentAcceleration - lastAcceleration);

                        if (acceleration > ACCELERATION_THRESHOLD){
                            //confirmErase();
                        }
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
            };
}
