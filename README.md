## CMT Reference Application

This sample application is designed to demonstrate best practices when integrating the DriveWell SDK for Android into a consumer app.

## Branches

This branch integrates SDK version **12.2.2**.

The **develop** branch demonstrates integration with the latest version of the DriveWell SDK. Checkout a **support/xx** branch to see usage of that particular version of the SDK.

## Architecture & Modules

### Architecture

This sample app was built using modern application development best practices with regard to architecture and clean code principles. Google's architecture and library recommendations were also taken into account. In a nutshell, the app is built with a multi-module approach, using MVVM and Jetpack Compose. A typical screen is made up of:

* A Compose Screen (for example, **RegisterEmailScreen.kt**) and
* A ViewModel (**RegisterEmailViewModel.kt**), which communicates with the SDK via the wrappers module.

### Tech

The sample app is built in a reactive manner, using Kotlin Flows and Coroutines. It also leans heavily on Google's first party libraries for the various Jetpack projects, such as Compose. The app uses Accompanist utilities for the UI, however this has no bearing on the SDK integration code.
The repository also includes custom **buildSrc** scripts and Gradle plugins that demonstrate code quality and dependency configuration best practices for multi-module projects. (Dependencies are defined in **Dependencies.kt.**)

### Modules

The app is made up of several modules to optimize compile times and improve coding practices. The modules are:

* **common** - Contains code and utilities that are visible in all other modules.
* **theme** - Contains the Compose theme, colors, dimens and any additional resources used by UI code throughout the app. It also contains reusable UI components.
* **navigation** - Contains boilerplate code related to navigation. The module also contains **route descriptors** for all the screens in the app. All feature modules depend on this module as a Screen without a corresponding Route is unreachable.
* **wrappers** - The primary integration surface with the DriveWell SDK. Contains code that bridges the reactive coroutine-based reference app and the Rx-based SDK. Also attempts to harmonize the API with the iOS Reference App. It contains the notification **Service**, **Listener**, and **Receiver** subclasses that the SDK needs in order to function.
* **features/*** - Contains most of the screens in the app. Screens are split into modules by topic. Every feature module contains Compose Screen files and ViewModels, additional supporting classes, and, importantly, a **Navigation.kt** file. This file lists the Routes from **navigation** and maps them to the Compose Screens. This mapping is then used in the **app** module to build a complete navigation graph.
* **app** - Contains the Application class and the code that brings all the other modules together. It implements some global services, like **ErrorService** and **AppVersionProvider**, and contains the **MainActivity** that hosts the UI and sets up global listeners.

## Building & Running the App

### Prerequisites

To build the app, you'll need:

- Latest stable Android Studio that supports AGP 7.4.1 and JDK11
- A username and password to access DriveWell SDK artifacts in the CMT Artifactory server
- A backend URL and corresponding API key
- A **google-services.json** for Firebase Messaging and (optionally) Crashlytics
- A Google Maps API key ([https://developers.google.com/maps/documentation/android-sdk/get-api-key](https://developers.google.com/maps/documentation/android-sdk/get-api-key))

### Building the Reference App

To build the reference app, complete the following steps:

1. Clone this Git project to your machine.
2. Add two property files to the root of the repo:

   a. Add an **artifactory.properties** file that includes the following:

   `username=[your username or email]`

   `password=[your password]`

   `url=[URL of the repository, by default use: https://censiodev.jfrog.io/artifactory/android-maven-external]`
       
   b. Add a company.properties file that includes the following (note that the backend url should not contain a trailing slash):

   `baseUrl=[backend URL]`

   `apiKey=[API key]`

   `googleMapsApiKey=[Google Maps API key]`

3. Open the project in Android Studio.
4. Locate an Android phone running OS version 7 or later with an accelerometer, gyroscope, and GPS.
5. Run/debug the project on the phone, either in Studio or on the command line `./gradlew installDebug`.

### Firebase Integration (Push & Crashlytics)

The reference app is set up to receive push notifications from Firebase Cloud Messaging.
To enable both push notifications and Crashlytics, obtain a `google-services.json` file from your Firebase project and place it in the root of the `app/` directory, then sync with Gradle. The services will automatically be enabled upon the next build. 

## Basic Information About Using the App

1. When the app launches, you will be able to authenticate either by creating a new user or logging in as a returning user.
2. When registering a new user, the App-Only Mode toggle is disabled by default to support tracking with App + Tag. To track trips only with your phone, enable the App-Only Mode toggle.
3. For DriveWell Tag users:

   a. You can link Tags from the Vehicles screen.

   b. With the Tag detector enabled (the default), gently shake your Tag to start a new trip.
4. For App-Only users:

   a. With the App-Only Mode enabled, take the phone with you in a car and go for a short test trip.

   b. For best results on this first trip, place the phone in the cup holder or another stable, exposed location where GPS will be available.
5. After your trip is finished, recorded sensor data will be uploaded and scored. You can view your recorded trip within the trip list view in the app, or via the CMT web support portal.
6. If you would like to use CrashAssist feature:

   a. Tap on the **DriveWell Crash Feature** option on the Dashboard.

   b. On the following screen, enable CrashAssist using the toggle below the CrashAssist card.

   c. If your device recognizes a crash, it will notify you via a notification.

   d. If you tap on the notification the crash flow will be immediately started.

## Code Samples for Existing Projects

If you are integrating the SDK with an existing project, the following files in the sample app contain code that you can use to perform certain common tasks.

* **Registering a user** => **RegisterEmailViewModel.kt**, **AuthenticationManagerImpl.kt**

* **Logging a user in** => **ExistingUserEmailViewModel.kt**, **VerifyCodeViewModel.kt**, **AuthenticationManagerImpl.kt**

* **Requesting permissions from the user** => see the entire **:features:permission** module

* **Subclassing the CMT Android Trip Recording Service** => **ReferenceAppTelematicsService.kt**

* **Subclassing the CMT Service Listener** => **ReferenceAppTelematicsListener.kt**

* **Subclassing the CMT Service Notification Receiver** => **ReferenceAppTelematicsReceiver.kt**

* **Receiving trip suppressions warnings** => **ServiceAnomalyReceiver.kt**

* **Configuring the CMT Data Model object in your application** => **SdkManagerImpl.kt**

* **Linking Tags** => see the **:features:tags** module

* **Obtaining the latest trip results** => **TripManagerImpl.kt**

* **Vehicles list, creating vehicles** => see the **:features:tags** module

* **Handling impacts** => see the **crash** package in the **:wrappers** module

* **Push messages** => **ReferenceAppPushService.kt**

* **Low battery detections** => **BatteryIsLowForTripRecordingDriverImpl.kt**

* **Device specific settings** => **SettingsManagerImpl.kt**

* **Screen navigation analytics** => **AnalyticsManagerImpl.kt**, **MainViewModel.kt**

* **Drawing a map for a trip** => **RoutePreview.kt**
