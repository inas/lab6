package inas.anisha.lab6

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {

    lateinit var wifiManager: WifiManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

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
    }

    private fun changeWifi() {
        if (!wifiManager.isWifiEnabled) wifiManager.isWifiEnabled = true

        wifiManager.configuredNetworks.forEach {
            if (it.SSID == "\"" + "Nanik Sri Rezeki" + "\"") {
                wifiManager.disconnect()
                wifiManager.enableNetwork(it.networkId, true)
                wifiManager.reconnect()
                return
            }
        }

        val wifiConfig = WifiConfiguration()
        wifiConfig.SSID = "\"" + "Nanik Sri Rezeki" + "\""
        wifiConfig.preSharedKey = String.format("\"%s\"", "140697NA")

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

    companion object {
        const val LOCATION_REQUEST = 101
    }
}
