import tinyb.BluetoothDevice
import tinyb.BluetoothManager


fun main(args: Array<String>) {
    getDevice("")
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
        val list = manager.getDevices() ?: return null

        for (device in list) {
            printDevice(device)
            /*
                 * Here we check if the address matches.
                 */
            if (device.getAddress() == address)
                sensor = device
        }

        if (sensor != null) {
            return sensor
        }
        Thread.sleep(4000)
        ++i
    }
    return null
}