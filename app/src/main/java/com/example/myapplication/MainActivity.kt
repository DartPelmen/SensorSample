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
    /**
     * Обработчик событий SensorEvent (получение значений с сенсора и изменения точности измерения)
     * */
    private val listener = object : SensorEventListener{
        /**
         * Метод обратного вызова для обработки получения значения сенсора.
         * Обратите внимание на обработку получаемой информации
         * */
        override fun onSensorChanged(p0: SensorEvent?) {
            binding.xField.text = "x: ${p0?.values?.get(0)?.toString()}";
            binding.yField.text = "y: ${p0?.values?.get(1)?.toString()}";
            binding.zField.text = "z: ${p0?.values?.get(2)?.toString()}";
        }
        /**
         * Метод обратного вызова для обработки изменения точности измерения
         * */
        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        }
    }
    private lateinit var binding: ActivityMainBinding
    /**
     * Описание сенсора
     * */
    private var sensor: Sensor? = null
    /**
     * Менеджер сенсоров, предоставляемый системным сервисом
     * */
    private var s: SensorManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //получение списка сенсоров
        s = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        //вывод списка сенсоров
        s?.getSensorList(Sensor.TYPE_ALL)?.forEach(Consumer { x: Sensor ->
                Log.d(
                    "SENSOR",
                    x.name
                )
            })
        //получение гироскопа по умолчанию (при наличии)
            sensor = if(s?.getSensorList(Sensor.TYPE_GYROSCOPE)?.size!! >0) s?.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
                else null
        //регистрация прослушивателя гироскопа (при наличии)
            sensor?.let {
                s?.registerListener(listener,it,SensorManager.SENSOR_DELAY_NORMAL)
            }
        }

    override fun onResume() {
        super.onResume()
        //регистрация прослушивателя гироскопа (при наличии)
        s?.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onStop() {
        super.onStop()
        //снятие регистрации прослушивателя гироскопа (при наличии)
        s?.unregisterListener(listener)
    }

}