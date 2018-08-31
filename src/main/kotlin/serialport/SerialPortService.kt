package serialport

import com.serialpundit.serial.SerialComManager
import java.lang.Thread.sleep

class SerialPortService(private val portName: String) {
    private val scm = SerialComManager()

    private var handle: Long = 0

    fun connect() {
        this.handle = scm.openComPort(
                portName,
                true,
                true,
                true
        )
        scm.configureComPortData(
                handle,
                SerialComManager.DATABITS.DB_8,
                SerialComManager.STOPBITS.SB_1,
                SerialComManager.PARITY.P_NONE,
                SerialComManager.BAUDRATE.B115200,
                0
        )
        scm.configureComPortControl(
                handle,
                SerialComManager.FLOWCONTROL.NONE,
                'x',
                'x',
                false,
                false
        )
    }

    fun reset() {
        println("reset")
        sendCommands(
                "help",
                "3"
        )
        sleep(3000)
        scm.closeComPort(handle)
    }

    fun turnOnPairing() {
        println("turn on pairing")
        sendCommands(
                "help",
                "5"
        )
        Thread.sleep(7000)
    }

    fun sendCommands(vararg a: String) {
        for (command in a) {
            scm.writeString(
                    handle,
                    "$command\r\n",
                    0
            )
        }
    }
}