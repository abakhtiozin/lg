import ble.BleService
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import serialport.SerialPortService
import serialport.Status

class LightsTest {
    init {
        System.getProperties()
                .filter { it.key.toString().contains("java.library.path") }
                .forEach{ println(it)}
    }
    private val bleAddress = "D6:C9:F5:73:32:1B"
    private val serviceUuid = "58a78b01-e280-48a4-8668-b8d8cf947cf8"
    private val portName = "/dev/ttyACM0"
    private var serialport: SerialPortService = SerialPortService()
    private val device = BleService()

    @Before
    fun setUp() {
        println("set up")
        serialport {
            port = portName
            actions {
                connect()
                reset()
            }
            actions {
                connect()
                turnOnPairing()
            }
        }
    }

    @Test
    fun changeLightStatesToOff() {
        device {
            address = bleAddress
            connect {
                service {
                    findService(serviceUuid)
                    characteristic {
                        connect("8b07")
                        write(0x03)
                        Assert.assertTrue(byteArrayOf(3) contentEquals  read())
                    }
                }
            }
        }
    }

    @Test
    @Ignore
    fun `set WLS to OFF on device read WLS on BLE`() {
        serialport {
            port = portName
            actions {
                waterLevel { Status.OFF }
            }
        }
        device {
            address = bleAddress
            connect {
                service {
                    findService(serviceUuid)
                    characteristic {
                        connect("8b08")
                        Assert.assertTrue(byteArrayOf(0) contentEquals  read())
                    }
                }
            }
        }
    }

    @After
    fun tearDown() {
        println("tear down")
        device { disconnect() }
        serialport {
            actions { reset() }
        }
    }
}