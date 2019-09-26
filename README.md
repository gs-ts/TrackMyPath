# FlickrFlow

An android app that tracks your walk with images every 100 meters: 
- images fetched from Flickr based on location 
- pictures are shown in a list, and user can scroll through the stream
- one button start/stop, on each start the previous stream of photos gets wiped

---

### MVVM pattern with Clean architecture (as much as possible) developed with Kotlin.
Clean architecture consists of three layers:
- **Data**, which includes databases, clients, repositories, network
- **Domain**, which includes models, entities, and usecases
- **Presentation**, which includes UI related components, such as ViewModels, Fragments, Activities

Sources: [1](https://medium.com/androiddevelopers/google-i-o-2018-app-architecture-and-testing-f546e37fc7eb), [2](https://rubygarage.org/blog/clean-android-architecture), [3](https://proandroiddev.com/clean-architecture-data-flow-dependency-rule-615ffdd79e29)

##### Android Jetpack Components used:
- Fragment
- ViewModel 
- LiveData 
- Room
- [Location](https://github.com/googlesamples/android-play-location/tree/master/LocationUpdatesForegroundService)
- [ActivityScenario](https://developer.android.com/guide/components/activities/testing), instrumentation testing (part of AndroidX Test) 
- Espresso (UI tests)

##### Libraries:
- [Koin](https://insert-koin.io/), an easy-to-use DI framework. [Nice comparison with Dagger](https://medium.com/@farshidabazari/android-koin-with-mvvm-and-retrofit-e040e4e15f9d)
- [Kotlin Coroutines](https://developer.android.com/kotlin/coroutines)
- [fresco](https://github.com/facebook/fresco), an Android library for managing images and the memory they use
- [Retrofit](https://square.github.io/retrofit/)
- [OkHttp](https://square.github.io/okhttp/)
- [moshi](https://github.com/square/moshi), JSON library for Kotlin and Java 
- [Timber](https://github.com/JakeWharton/timber), a logger which provides utility on top of Android’s Log class

##### Flickr API:
- [flickr.photos.search](https://www.flickr.com/services/api/flickr.photos.search.html)
