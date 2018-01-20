package com.londonx.ggg

import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bronze.kutil.log
import com.bronze.kutil.view.LProgressDialog
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.bluetoothManager
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.IOException
import java.io.OutputStream
import java.util.*

class MainActivity : AppCompatActivity() {
    private var ops: OutputStream? = null

    private val connectingDialog by lazy {
        val temp = LProgressDialog(this)
        temp.setMessage(R.string.connecting)
        temp
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sw.setOnCheckedChangeListener { _, isChecked ->
            ops?.write(if (isChecked) "a".toByteArray() else "8".toByteArray())
            ops?.flush()
        }
    }

    override fun onResume() {
        super.onResume()
        val bt = bluetoothManager.adapter.bondedDevices
                .find { it.name == "DL_BDKA" } ?: return
        bt.uuids.forEach { log(it.toString()) }
        val socket = bt.createRfcommSocketToServiceRecord(
                UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"))
        startConnecting(socket) {
            sw.isEnabled = true
        }
    }

    override fun onPause() {
        super.onPause()
        ops?.close()
        ops = null
        sw.isEnabled = false
    }

    private var isConnecting = false
    private fun startConnecting(socket: BluetoothSocket, done: () -> Unit) {
        if (isConnecting) {
            return
        }
        isConnecting = true
        connectingDialog.show()
        doAsync {
            try {
                socket.connect()
            } catch (ignore: IOException) {
            } finally {
                uiThread {
                    if (connectingDialog.isShowing) {
                        connectingDialog.dismissByUser()
                    }
                    ops = socket.outputStream
                    isConnecting = false
                    if (socket.isConnected) {
                        done.invoke()
                    }
                }
            }
        }
    }
}
