package ble

import tinyb.*

open class BleNewService(var address: String = "") {

    var service = BleDeviceService()
    var characteristic: BleCharacteristic = BleCharacteristic()
    private var device: BluetoothDevice? = null
    var gattService: BluetoothGattService? = null
    private val manager = BluetoothManager.getBluetoothManager()

    open operator fun invoke(init: BleNewService.() -> Unit) {
        init.invoke(this)
    }

    private fun discovery() {
        var i = 0
        while (i < 10) {
            device = manager.devices.firstOrNull {
                it.address == address
            }
            if (device != null) {
                printDevice(device)
                try {
                    manager.stopDiscovery()
                } catch (e: BluetoothException) {
                    throw Exception("Discovery could not be stopped.")
                }
                return
            } else {
                Thread.sleep(4000)
                ++i
            }
        }
        if (device == null) {
            throw Exception("Device with address:$address was not found")
        }
    }

    private fun connect() {
        try {
            device?.connect()
        } catch (e: BluetoothException) {
            throw Exception(e)
        }
    }

    fun disconnect() {
        manager.stopDiscovery()
        manager.stopNearbyDiscovery()
        device?.disconnect()
        device?.remove()
    }

    fun connect(init: BleDeviceService.() -> Unit) {
        discovery()
        connect()
        init.invoke(service)
    }

    inner class BleDeviceService {

        operator fun invoke(init: BleDeviceService.() -> Unit) {
            init.invoke(this)
        }

        @Throws(InterruptedException::class)
        private fun getService(device: BluetoothDevice?, UUID: String): BluetoothGattService? {
            println("Services exposed by device:")
            var tempService: BluetoothGattService? = null
            var bluetoothServices: List<BluetoothGattService>?
            do {
                bluetoothServices = device?.services
                if (bluetoothServices == null) {
                    return null
                }
                for (service in bluetoothServices) {
                    if (service.uuid == UUID) {
                        tempService = service
                    }
                }
                Thread.sleep(4000)
            } while (bluetoothServices!!.isEmpty())
            return tempService
        }

        fun findService(sUUID: String) {
            val service = getService(device, sUUID)
            if (service == null) {
                device?.disconnect()
                throw Exception("This device does not have the service we are looking for.")
            }
            println("Found service " + service.uuid)
            gattService = service
        }
    }
    inner class BleCharacteristic {

        private var gattcharacteristic: BluetoothGattCharacteristic? = null

        operator fun invoke(init: BleCharacteristic.() -> Unit) {
            init.invoke(this)
        }

        fun read(): ByteArray {
            return gattcharacteristic?.readValue() ?: byteArrayOf(0)
        }

        fun write(byte: Byte) {
            gattcharacteristic?.writeValue(byteArrayOf(byte))
        }

        fun connect(cUUID: String) {
            var isFound = false
            val characteristics = gattService?.characteristics
            if (characteristics != null) {
                for (characteristic in characteristics) {
                    if (characteristic.uuid.contains(cUUID, true)) {
                        this.gattcharacteristic = characteristic
                        isFound = true
                    }
                }
            }
            if (!isFound || characteristics == null) {
                throw Exception("Characteristic $cUUID not found")
            }
        }
    }
}

private fun printDevice(device: BluetoothDevice?) {
    print("Address = " + device?.address)
    print(" Name = " + device?.name)
    print(" Connected = " + device?.connected)
    println()
}

