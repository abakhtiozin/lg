package serialport

import ble.BleService
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import tinyb.BluetoothDevice

class SerialPortServiceTest {

    val deviceMacAddress = "D6:C9:F5:73:32:1B"
    val portName = "/dev/ttyACM0"
    var device: BluetoothDevice? = null
    lateinit var serialport: SerialPortService

    @Before
    fun setUp() {
        println("set up")
        serialport = SerialPortService()
        serialport {
            port = portName
            actions {
                connect
                reset
            }
            actions {
                connect
                turnOnPairing
            }
        }
    }

    @Test
    fun turnOnBlueTooth() {
//        serialport = SerialPortService(portName)
//        serialport.connect()
//        serialport.turnOnPairing()
        val bleService = BleService(deviceMacAddress)
        val device = bleService.connect()
        val service = bleService.findService(device, "58a78b01-e280-48a4-8668-b8d8cf947cf8")
        bleService.writeCharacteristic(service, "8b07", 0x03)
        val actual: ByteArray? = bleService.readCharacteristic(service, "8b07")
        Assert.assertTrue(
                actual != null && actual contentEquals byteArrayOf(3)
        )
    }

    @After
    fun tearDown() {
        println("tear down")
        device?.disconnect()
//        serialport.reset()
    }

//    val comport = ComPort()
//    comport {
//        port "/dev/ttyACM0"
//        action {
//            connect
//            reset
//        }
//    }

    /*
    comport {
        port "/dev/ttyACM0"
        do {
            connect
            reset
        }
    }

    comport {
        port "/dev/ttyACM0"
        do {
            connect
            turnOnPairing
        }
    }
    ble {
        device "D6:C9:F5:73:32:1B"
        service "58a78b01-e280-48a4-8668-b8d8cf947cf8"
        lightService {
            write
            read
            assert
        }
        lightService {
            on
            off
            ambient
            warning1
            warning2
            warning3
        }
    }

    * */
}

