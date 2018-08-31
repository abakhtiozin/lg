import com.serialpundit.serial.SerialComManager
import tinyb.*
import java.util.*


fun main(args: Array<String>) {
    val scm = SerialComManager()
    val handle1 = scm.openComPort(
            "/dev/ttyACM0",
            true,
            true,
            true
    )
    scm.configureComPortData(
            handle1,
            SerialComManager.DATABITS.DB_8,
            SerialComManager.STOPBITS.SB_1,
            SerialComManager.PARITY.P_NONE,
            SerialComManager.BAUDRATE.B115200,
            0
    )
    scm.configureComPortControl(
            handle1,
            SerialComManager.FLOWCONTROL.NONE,
            'x',
            'x',
            false,
            false
    )
    scm.writeString(handle1, "help\r\n", 0)
//    scm.writeString(handle1, "4\r\n", 0)
    scm.writeString(handle1, "5\r\n", 0)
    Thread.sleep(5000)

//    System.exit(1)
    val bleDevice = "D6:C9:F5:73:32:1B"
    val device = getDevice(bleDevice)
    if (device?.connect()!!) {
        System.out.println("Sensor with the provided address connected")
    }
    val serviceUUID = "58a78b01-e280-48a4-8668-b8d8cf947cf8"
    val service = getService(device, serviceUUID)

    if (service == null) {
        System.err.println("This device does not have the service we are looking for.")
        device.disconnect()
        System.exit(-1)
    }
    println("Found service " + service!!.uuid)

    val lightValue = getCharacteristic(service, "8b07")
    println("Current value" + Arrays.toString(lightValue?.readValue()))
    lightValue?.writeValue(byteArrayOf(0x03))
    print("Changed!")
}

var running = true

fun printDevice(device: BluetoothDevice) {
    print("Address = " + device.address)
    print(" Name = " + device.name)
    print(" Connected = " + device.connected)
    println()
}

@Throws(InterruptedException::class)
fun getDevice(address: String): BluetoothDevice? {
    val manager = BluetoothManager.getBluetoothManager()
    var sensor: BluetoothDevice? = null
    var i = 0
    while (i < 15 && running) {
        val list = manager.devices ?: return null

        for (device in list) {
            if (device.address == address) {
                printDevice(device)
                sensor = device
            }
        }

        if (sensor != null) {
            try {
                manager.stopDiscovery()
            } catch (e: BluetoothException) {
                System.err.println("Discovery could not be stopped.")
            }
            return sensor
        }
        Thread.sleep(4000)
        ++i
    }
    return null
}

@Throws(InterruptedException::class)
fun getService(device: BluetoothDevice, UUID: String): BluetoothGattService? {
    println("Services exposed by device:")
    var tempService: BluetoothGattService? = null
    var bluetoothServices: List<BluetoothGattService>? = null
    do {
        bluetoothServices = device.services
        if (bluetoothServices == null)
            return null

        for (service in bluetoothServices) {
            if (service.uuid == UUID) {
                tempService = service
            }
        }
        Thread.sleep(4000)
    } while (bluetoothServices!!.isEmpty() && running)
    return tempService
}

fun getCharacteristic(service: BluetoothGattService, UUID: String): BluetoothGattCharacteristic? {
    val characteristics = service.characteristics ?: return null
    for (characteristic in characteristics) {
        if (characteristic.uuid.contains(UUID, true)) {
            return characteristic
        }
    }
    return null
}