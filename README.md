# RadioBrowser-Android
An Android library for consuming [RadioBrowser](https://www.radio-browser.info/) API

## Install
Add the Jitpack maven repository in the list of repositories.
```gradle
repositories {
    ...
    maven { url 'https://jitpack.io' }
}
```

Add the dependeny declaration to your app or module's `build.gradle` file.
```gradle
dependencies {
    implementation 'com.github.r-cohen:RadioBrowser-Android:1.0.9'
}
```

## Usage
Instantiate the RadioBrowserApi:
```kotlin
val api = RadioBrowserApi()
```
You can optionally set the User-Agent of your app in the RadioBrowserApi constructor.
If you don't set any User-Agent, the app will use the default one: `RC.RadioBrowserAndroid`
```kotlin
val api = RadioBrowserApi("MyRadioApp")
```

### Get list of countries
```kotlin
api.getCountries(
    onSuccess = { countries ->
        countries.forEach { country -> outputText("country: $country") }
    },
    onFail = { error -> outputText("error: $error") }
)
```

### Get list of stations by country
```kotlin
api.getStationsByCountry(
    countryCode = "IL",
    offset = 0,
    limit = 500,
    onSuccess = { stations ->
        stations.forEach { station -> outputText("station $station") }
    },
    onFail = { error -> outputText("error: $error") }
)
```

### Search a station by name
```kotlin
api.searchStationsByName(
    search = "kol chai",
    offset = 0,
    limit = 500,
    onSuccess = { stations ->
        stations.forEach { station -> outputText("station $station") }
    },
    onFail = { error -> outputText("error: $error") }
)
```

### Other usefull api methods
```kotlin
getStatesByCountry
getStationsByState
```

### User station click
When a user starts streaming a radio, it is good practice to call the `stationClick` API method in order to update the station's statistics.
```kotlin
api.stationClick(
    stationUuid = stationUuid,
    onSuccess = { clickResult ->
        outputText("$clickResult")
    },
    onFail = { error -> outputText("error: $error") }
)
```

[<img src=""https://cdn.buymeacoffee.com/buttons/v2/default-yellow.png" height="60" />](https://www.buymeacoffee.com/raphael.cohen)
