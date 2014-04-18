# Sulabh

Sulabh is a Android app which allows you to find loos around your location within 2 km radious.
You can also add new loo's information on the map.

## Setup

*Note: These instructions assume you have a Java 1.6 [JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html) installed.*

To start a new Android project:

1. Install the [Android SDK](http://developer.android.com/sdk/index.html). On Mac OS X with [Homebrew](http://brew.sh/) just run `brew install android-sdk`

2. Set your `ANDROID_HOME` environment variable to `/usr/local/opt/android-sdk`.

3. Run the Android SDK GUI and install API 18 and any other APIs you might need. You can start the GUI by invoking `android`

4. Download Deckard from GitHub:   
    ```bash    
    wget https://github.com/ankitrokdeonsns/sulabh-android-app.zip 
    ```        
    ```         
    unzip sulabh-android-app.zip
    ```

# IntelliJ / Android Studio Support

1. Make sure you have `gradle v1.11`

2. Run `gradle idea` after cloning repo  

3. open project via generated `.ipr` file  

4. Choose project SDK as android-19(with Java 1.6)  

5. mark `src` as source root in IntelliJ IDEA  

6. add `local.properties` at project root with content as `sdk.dir=YOUR_ANDROID_HOME_PATH`  

7. create a run configuartion in IntelliJ for running android application 