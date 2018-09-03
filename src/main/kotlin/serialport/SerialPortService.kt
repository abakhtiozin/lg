package serialport

import com.serialpundit.serial.SerialComManager

class SerialPortService {
    private val scm = SerialComManager()

    private var handle: Long = 0
    lateinit var port: String

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
    }

    private fun reset() {
        println("reset")
        sendCommands("3")
        scm.closeComPort(handle)
    }

    private fun turnOnPairing() {
        println("turn on pairing")
        sendCommands("5")
        Thread.sleep(7000)
    }

    fun sendCommands(vararg commands: String) {
        for (command in commands) scm.writeString(handle,"$command\r\n",0)
    }

    operator fun invoke(init: SerialPortService.() -> Unit) {
        init.invoke(this)
    }

    fun actions(function: SerialPortActions.() -> () -> Unit) {
        function.invoke(SerialPortActions(this))
    }

    class SerialPortActions(service: SerialPortService) {
        val connect: () -> Unit = { service.connect() }
        val reset: () -> Unit = { service.reset() }
        val turnOnPairing: () -> Unit = { service.turnOnPairing() }
    }
}
