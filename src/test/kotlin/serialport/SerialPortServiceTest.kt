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
        serialport = SerialPortService(portName)
        serialport.connect()
        serialport.reset()
    }

    @Test
    fun turnOnBlueTooth() {
        serialport = SerialPortService(portName)
        serialport.connect()
        serialport.turnOnPairing()
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
        serialport.reset()
    }

    /*
    connect {
        by ComPort {
            to "/dev/ttyACM0"
            do {
                reset
            }
        }
    }

    connect {
        by ComPort {
            to "/dev/ttyACM0"
            do {
                pairing on
            }
        }
        by BLE {
            to "D6:C9:F5:73:32:1B"

            do {
                pairing on
            }
        }
    }
    * */
}