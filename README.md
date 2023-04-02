![Sunny Day](art/banner.png)

# Sunny Day ☀️

The Android app that shows weather data from [Open-Meteo](https://open-meteo.com/) is a user-friendly application that
provides accurate and up-to-date weather information to users. The app is designed to display current weather
conditions, as well as forecasts for the next few days.

Upon launching the app, users are greeted with a clean and intuitive interface, featuring a simple and attractive design
that allows for easy navigation. The app's main screen provides users with a quick overview of the current weather
conditions in their location, as well as for any selected location, including temperature and icon.

## Screenshots

<img src=./screenshots/locations-light.png width=24% /> <img src=./screenshots/location-details-1-light.png width=24% /> <img src=./screenshots/location-details-2-light.png width=24% /> <img src=./screenshots/search-light.png width=24% />
<img src=./screenshots/locations-dark.png width=24% /> <img src=./screenshots/location-details-1-dark.png width=24% /> <img src=./screenshots/location-details-2-dark.png width=24% /> <img src=./screenshots/search-dark.png width=24% />

## Setup

### Google Maps API key

Google Maps API key is used for Google Places SDK (search). Create Google Maps API
key ([documentation](https://developers.google.com/maps/documentation/android-sdk/get-api-key#creating-api-keys)) and
set it in `~/secrets.properties`:

```
GOOGLE_MAPS_API_KEY=<api-key>
```

## Tech stack

- [Kotlin](https://kotlinlang.org/)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- MVI
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- [Navigation Component for Compose](https://developer.android.com/jetpack/compose/navigation)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Accompanist](https://google.github.io/accompanist/) ([permissions](https://google.github.io/accompanist/permissions/))
- [Material 3](https://m3.material.io/)
- [Arrow](https://arrow-kt.io/)
- [Room](https://developer.android.com/training/data-storage/room)
- [Retrofit](https://square.github.io/retrofit/)
- [Google Places SDK](https://developers.google.com/maps/documentation/places/android-sdk)
- [JUnit 4](https://junit.org/junit4/) (instrumented tests) / [JUnit 5](https://junit.org/junit5/) (unit tests)
- [Kotest](https://kotest.io/)
- [MockK](https://mockk.io/)
- [Turbine](https://github.com/cashapp/turbine)
- [MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver)
- [Detekt](https://detekt.dev/)
