package inas.anisha.lab6

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var wifiManager: WifiManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    override fun onStart() {
        super.onStart()

        if (isPermissionsGranted()) {
            changeWifi()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                LOCATION_REQUEST
            )
        }

        Handler().postDelayed({
            if (wifiManager.connectionInfo.ssid == SSID) {
                result.text = concatenateString("Connection to specified wifi: ", "success")
            } else {
                result.text = concatenateString("Connection to specified wifi: ", "failed")
            }

            if (wifiManager.isWifiEnabled && wifiManager.connectionInfo.networkId != -1) {
                connection.text =
                    concatenateString("Wifi is connected to: ", wifiManager.connectionInfo.ssid)
            } else {
                connection.text = concatenateString("Wifi is", " not connected")
            }
        }, 7000)

    }
    private fun changeWifi() {
        if (!wifiManager.isWifiEnabled) wifiManager.isWifiEnabled = true

        wifiManager.configuredNetworks.forEach {
            if (it.SSID == SSID) {
                wifiManager.disconnect()
                wifiManager.enableNetwork(it.networkId, true)
                wifiManager.reconnect()
                return
            }
        }

        val wifiConfig = WifiConfiguration()
        wifiConfig.SSID = SSID
        wifiConfig.preSharedKey = PASSWORD

        val netId = wifiManager.addNetwork(wifiConfig)
        wifiManager.enableNetwork(netId, true)
        wifiManager.reconnect()

    }

    private fun isPermissionsGranted() =
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST -> {
                changeWifi()
            }
        }
    }

    external fun concatenateString(firstString: String, secondString: String): String

    companion object {
        init {
            System.loadLibrary("native-lib")
        }
        const val LOCATION_REQUEST = 101
        const val SSID = "\"" + "Nanik Sri Rezeki" + "\""
        const val PASSWORD = "\"" + "140697NA" + "\""

    }
}
