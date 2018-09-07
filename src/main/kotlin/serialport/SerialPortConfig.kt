package serialport

import com.google.gson.Gson
import java.io.FileReader

data class SerialPortConfig(
        val port: String,
        val screen: Screen
) {
    companion object {
        private var isNotLoad = true
        private lateinit var reference: SerialPortConfig
        fun init(): SerialPortConfig {
            if (isNotLoad) {
                reference = Gson().fromJson(
                        FileReader("mapping_comport.json"),
                        SerialPortConfig::class.java
                )
                isNotLoad = false
            }
            return reference
        }
    }
}

data class Screen(
        var activate_screen: String,
        var water_level_button_on: String,
        val water_level_button_off: String,
        var press_reset_button: String,
        var short_press_user_button: String,
        var long_press_user_button: String
)
