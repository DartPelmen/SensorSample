package com.example.myapplication

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding
import java.util.function.Consumer


class MainActivity : AppCompatActivity() {
    private val listener = object : SensorEventListener{
        override fun onSensorChanged(p0: SensorEvent?) {
            binding.xField.text = "x: ${p0?.values?.get(0)?.toString()}";
            binding.yField.text = "y: "+ p0?.values?.get(1)?.toString();
            binding.zField.text = "z: "+ p0?.values?.get(2)?.toString();

        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        }
    }
    private lateinit var binding: ActivityMainBinding
    private var sensor: Sensor? = null
    private var s: SensorManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        s = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        s?.getSensorList(Sensor.TYPE_ALL)?.forEach(Consumer { x: Sensor ->
                Log.d(
                    "SENSOR",
                    x.name
                )
            })
            sensor = if(s?.getSensorList(Sensor.TYPE_GYROSCOPE)?.size!! >0) s?.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
                else null
            sensor?.let {
                s?.registerListener(listener,it,5000)
            }
        }

    override fun onResume() {
        super.onResume()
        s?.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onStop() {
        super.onStop()
        s?.unregisterListener(listener)
    }

}