import ble.BleConfig
import ble.BleService
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import serialport.SerialPortConfig
import serialport.SerialPortService
import serialport.Status

class LightsTest {
    private val portConfig: SerialPortConfig = SerialPortConfig.init()
    private val bleConfig = BleConfig.init()
    private val bleAddress = bleConfig.device.address
    private val serviceUuid = bleConfig.device.service
    private var serialport: SerialPortService = SerialPortService()
    private val device = BleService()

    @Before
    fun setUp() {
        println("set up")
        serialport {
            port = portConfig.port
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
                        connect(bleConfig.device.lightState.uuid)
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
            port = portConfig.port
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