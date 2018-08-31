package ble

import tinyb.*

class BleService(private val deviceMacAddress: String) {
    private var running = true
    fun connect(): BluetoothDevice? {
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

        val device = getDevice(deviceMacAddress)
        val isConnect = device?.connect()
        if (isConnect != null && isConnect) {
            System.out.println("Sensor with the provided address connected")
        }
        return device
    }

    fun findService(device: BluetoothDevice?, uuid: String): BluetoothGattService {
        @Throws(InterruptedException::class)
        fun getService(device: BluetoothDevice?, UUID: String): BluetoothGattService? {
            println("Services exposed by device:")
            var tempService: BluetoothGattService? = null
            var bluetoothServices: List<BluetoothGattService>? = null
            do {
                bluetoothServices = device?.services
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

        val service = getService(device, uuid)
        if (service == null) {
            System.err.println("This device does not have the service we are looking for.")
            device?.disconnect()
            System.exit(-1)
        }
        println("Found service " + service!!.uuid)
        return service
    }

    fun writeCharacteristic(service: BluetoothGattService, uuid: String, byte: Byte) {
        val characteristic = getCharacteristic(service, uuid)
        characteristic?.writeValue(byteArrayOf(byte))
    }

    fun readCharacteristic(service: BluetoothGattService, uuid: String): ByteArray? {
        val characteristic = getCharacteristic(service, uuid)
        return characteristic?.readValue()
    }

    private fun getCharacteristic(service: BluetoothGattService, UUID: String): BluetoothGattCharacteristic? {
        val characteristics = service.characteristics ?: return null
        for (characteristic in characteristics) {
            if (characteristic.uuid.contains(UUID, true)) {
                return characteristic
            }
        }
        return null
    }
}