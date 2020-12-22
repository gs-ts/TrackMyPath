# Track my path

An android app that tracks your walk with images every 100 meters:
- images fetched from Flickr based on location
- pictures are shown in a list, and user can scroll through the stream
- one button start/stop, on each start the previous stream of photos gets wiped
- when the app is removed from background and user has not stopped the tracking, the tracking continues in a service

*Please create a Flickr account and use your own api key. Add it in the FlickrService file.*

---

### MVVM pattern with Clean architecture developed with Kotlin.
Clean architecture consists of three layers:
- **Data**, includes data objects, databases, network clients, repositories.
- **Domain**, includes use cases of business logic. This layer orchestrates the flow of data from Data Layer to Presentation and the other way.
- **Presentation**, includes UI related components, such as ViewModels, Fragments, Activities.

##### Android Jetpack Components used:
- Fragment
- ViewModel
- View Binding
- LiveData
- Room
- [Location](https://github.com/googlesamples/android-play-location/tree/master/LocationUpdatesForegroundService)
- [ActivityScenario](https://developer.android.com/guide/components/activities/testing), instrumentation testing (part of AndroidX Test)
- Espresso (UI tests)

##### Libraries:
- [Koin](https://insert-koin.io/), (in master branch) an easy-to-use DI framework. [Nice comparison with Dagger](https://medium.com/@farshidabazari/android-koin-with-mvvm-and-retrofit-e040e4e15f9d)
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) (in feature-hilt-di branch) a DI library for Android based on Dagger
- [Kotlin Coroutines](https://developer.android.com/kotlin/coroutines)
- [fresco](https://github.com/facebook/fresco), an Android library for managing images and the memory they use
- [Retrofit](https://square.github.io/retrofit/)
- [OkHttp](https://square.github.io/okhttp/)
- [moshi](https://github.com/square/moshi), JSON library for Kotlin and Java
- [Timber](https://github.com/JakeWharton/timber), a logger which provides utility on top of Android’s Log class

##### Flickr API:
- [flickr.photos.search](https://www.flickr.com/services/api/flickr.photos.search.html)

Sources:
- [Google I/O 2018 app — Architecture and Testing](https://medium.com/androiddevelopers/google-i-o-2018-app-architecture-and-testing-f546e37fc7eb)
- [Clean Architecture of Android Apps with Practical Examples](https://rubygarage.org/blog/clean-android-architecture)
- [Clean Architecture Guide (with tested examples): Data Flow != Dependency Rule](https://proandroiddev.com/clean-architecture-data-flow-dependency-rule-615ffdd79e29)

----

### Screenshots

<img src="/screenshots/scrn1.png" width="260">
