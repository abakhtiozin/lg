package ble

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.io.FileReader

data class BleConfig(
        val device: Device
) {
    companion object {
        private var isNotLoad = true
        private lateinit var reference: BleConfig
        fun init(): BleConfig {
            if (isNotLoad) {
                reference = Gson().fromJson(
                        FileReader("mapping_ble.json"),
                        BleConfig::class.java
                )
                isNotLoad = false
            }
            return reference
        }
    }
}

data class Device(
        val address: String,
        val service: String,
        @SerializedName("light-state") val lightState: LightState,
        @SerializedName("water-level-state") val waterLevelState: WaterLevelState
)

data class LightState(
        val uuid: String,
        @SerializedName("Ambient") val ambient: String,
        @SerializedName("Warning 1") val warning1: String,
        @SerializedName("Warning 2") val warning2: String,
        @SerializedName("Warning 3") val warning3: String,
        val off: String,
        val on: String
)

data class WaterLevelState(
        val uuid: String,
        val low: String,
        val good: String
)
