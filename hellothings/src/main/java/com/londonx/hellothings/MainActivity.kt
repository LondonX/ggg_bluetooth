package com.londonx.hellothings

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManagerService

/**
 * Skeleton of an Android Things activity.
 *
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * val service = PeripheralManagerService()
 * val mLedGpio = service.openGpio("BCM6")
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
 * mLedGpio.value = true
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 *
 */
class MainActivity : Activity() {
    private val service = PeripheralManagerService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        service.gpioList.forEach { log(it) }
        val outPins = listOf(
                service.openGpio("IO1").also { it.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW) },
                service.openGpio("IO2").also { it.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW) },
                service.openGpio("IO3").also { it.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW) },
                service.openGpio("IO4").also { it.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW) }
        )
        Thread {
            while (!isDestroyed) {
                outPins.forEach {
                    it.value = !it.value
                    Thread.sleep(200)
                }
            }
            outPins.forEach {
                it.close()
            }
        }.start()
    }
}

fun Any.log(string: String) {
    Log.i(javaClass.simpleName, string)
}
