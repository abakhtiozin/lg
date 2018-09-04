package serialport

import com.serialpundit.serial.SerialComManager
import java.lang.Thread.sleep

open class SerialPortService(
        var port: String = ""
) {
    var actions: SerialPortActions = SerialPortActions()
    private val scm = SerialComManager()
    private var handle: Long = 0

    operator fun invoke(init: SerialPortService.() -> Unit) {
        init.invoke(this)
    }

    private fun sendCommands(vararg commands: String) {
        commands.forEach { command ->
            val data = "$command\r\n"
            scm.writeString(handle, data, 0)
        }
    }

    private fun connect() {
        this.handle = scm.openComPort(
                port,
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
        sendCommands("help")
        sleep(2000)
    }

    private fun reset() {
        println("reset")
        sendCommands("3")
        scm.closeComPort(handle)
        sleep(1000)
    }

    private fun turnOnPairing() {
        println("turn on pairing")
        sendCommands("5")
        sleep(7000)
        println("pairing on!")
    }

    inner class SerialPortActions {

        operator fun invoke(init: SerialPortActions.() -> Unit) {
            init.invoke(this)
        }

        fun reset() = this@SerialPortService.reset()

        fun connect() = this@SerialPortService.connect()

        fun turnOnPairing() = this@SerialPortService.turnOnPairing()
    }
}