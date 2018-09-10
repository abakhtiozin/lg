Tested on
Ubuntu version = Ubuntu 18.04.1 LTS
Java version = 1.8.0_144

project language: Kotlin
build and dependency manager: Gradle

Libraries and Dependencies:
TinyB - java library to work with bluetooth protocols (https://github.com/intel-iot-devkit/tinyb)
SerialPundit - java library to work with serial and usb protocols (https://github.com/RishiGupta12/SerialPundit)

Tinyb requires: 
    CMake 3.1+  
    GLib/GIO 2.40+
    BlueZ with GATT profile activated (5.37+) //tested on 5.48

Building project on Unix
Make sure you have:
    CMake 3.1+  
    GLib/GIO 2.40+
    BlueZ with GATT profile activated (5.37+) //tested on 5.48
    doxygen

Cmake commands:
-DCMAKE_INSTALL_PREFIX=/usr Command to change install path from /usr/local to /usr (IF NEEDED)
-DBUILDJAVA=ON

Clone git repository https://github.com/intel-iot-devkit/tinyb
open project root folder in terminal
cd tinyb
mkdir build
cd build
cmake .. -DCMAKE_INSTALL_PREFIX=/usr -DBUILDJAVA=ON 
make
make install

if there is no faults and errors you have to copy those files to clipboard
../build/src/libtinyb.so
../build/src/libtinyb.so.0
../build/src/libtinyb.so.0.5.1
../build/java/jni/libjavatinyb.so
../build/java/jni/libjavatinyb.so.0
../build/java/jni/libjavatinyb.so.0.5.1
../build/java/tinyb.jar

clone LG project with tests
open terminal in project root folder (where files like gradlew exist) 
 and write "./gradlew java_native"
 In console you will find something like
 /*
 .....
 Check your java library path
 java.library.path=/usr/java/packages/lib/amd64:/usr/lib64:/lib64:/lib:/usr/lib
 .....
 */
 You have to copy those 7 files that you copied and safe to clipboard on previous step to one of location you see above
 I copied to /usr/lib
 in the end I have
 ../usr/lib/libtinyb.so
 ../usr/lib/libtinyb.so.0
 ../usr/lib/libtinyb.so.0.5.1
 ../usr/lib/libjavatinyb.so
 ../usr/lib/libjavatinyb.so.0
 ../usr/lib/libjavatinyb.so.0.5.1
 ../usr/lib/tinyb.jar
Also you have to copy tinyb.jar file to project folder lg/lib/tinyb.jar

In the project root directory write ./gradlew clean test

TROUBLESHOOTING
Issue: Can't find particular device
Solution: Turn off/turn on bluetooth on you PC
Issue: Can't find any BLE device
Solution: to make bluetooth devices discoverable: sudo usermod -a -G bluetooth $user  


MAC OS:
Bluez supports only Linux
As a solution set up Linux VM

Project About
Config files
    mapping_comport.json
    mapping_ble.json
Before run tests change both files if needed
Default serial port = /dev/ttyACM0

