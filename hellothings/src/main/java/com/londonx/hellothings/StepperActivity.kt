package com.londonx.hellothings

import android.app.Activity
import android.os.Bundle
import com.polidea.androidthings.driver.steppermotor.Direction
import com.polidea.androidthings.driver.uln2003.driver.ULN2003Resolution
import com.polidea.androidthings.driver.uln2003.motor.ULN2003StepperMotor

class StepperActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val motor = ULN2003StepperMotor("IO1", "IO2", "IO3", "IO4")
        motor.rotate(360.0*10, Direction.CLOCKWISE, ULN2003Resolution.FULL.id, 10.0)
    }
}
