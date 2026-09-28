# NgamingCase

Android case study. The app lists posts from [JSONPlaceholder](https://jsonplaceholder.typicode.com/posts). You can delete a post by swiping it and edit its title and description on the detail screen.

<p>
  <img src="docs/list.png" width="240" alt="Post list" />
  <img src="docs/delete.png" width="240" alt="Swipe to delete with undo" />
  <img src="docs/detail.png" width="240" alt="Edit post" />
</p>

## Features

- Post list with title, short description and a round grayscale image, with dividers between rows
- Swipe to delete with undo
- Detail screen for editing the title and description
- Pull to refresh
- Cached posts are shown when offline
- Loading, empty and error states, with retry
- Light and dark theme

## Stack

| | |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 |
| Architecture | MVVM, use cases |
| Async | Coroutines, Flow |
| DI | Hilt (KSP) |
| Network | Retrofit, OkHttp, kotlinx.serialization |
| Local storage | Room |
| Images | Coil 3 |
| Navigation | Navigation 3 |
| Tests | JUnit 4, MockK, Turbine, Truth, Compose UI test |
| Code quality | Detekt, Spotless (ktlint), Android Lint |

## Modules

```
:app                          Application, MainActivity
:core:common                  Result and error types, base UI state, dispatcher qualifier
:core:designsystem            Theme, Ngaming* components, BaseViewModel, BaseScreen
:core:ui                      Error messages
:core:database                Room database, DAO, entity
:core:testing                 Test helpers
:network                      OkHttp, Retrofit, JSON setup
:navigation                   NavHost and Navigator
:feature:posts:domain         Model, repository interface, use cases
:feature:posts:data           Repository, API, DTO, mappers
:feature:posts:presentation   Screens, ViewModels, routes
```

Common Gradle config is in `build-logic` as convention plugins, and dependency versions are in `gradle/libs.versions.toml`.

Each feature registers its own screens with an `EntryProviderScope` extension (`postsEntries`), so adding a feature means one extra line in `MainActivity`.

## Notes on the implementation

JSONPlaceholder accepts `PUT` and `DELETE` but doesn't save anything. Without a local copy, a refresh would bring deleted posts back and undo edits. Room is the source of truth: the UI observes Room, and the network only writes into it. Deleted posts are kept with an `isDeleted` flag and edited posts with `isLocallyModified`, and a refresh skips both.

The image URL uses the post id instead of the list position (`https://picsum.photos/300/300?random={id}&grayscale`). With the position, every image below a deleted row would change. The URL is built in the data layer, so the domain model only has an `imageUrl`.

Swiping a post hides it right away and shows a snackbar with Undo. The `DELETE` request goes out only after the snackbar closes without Undo. If it fails, the post is restored and an error is shown.

Saving sends `PUT` first and writes to Room only when it succeeds. If it fails, the entered text stays on screen. The edited text is kept in `SavedStateHandle`, so it survives rotation and process death.

On the first launch the list is loaded from the API. After that the cached list is shown immediately and refreshed in the background. If a refresh fails while there are posts on screen, the list stays and an error dialog is shown.

Loading and error handling is shared. Use cases return `Flow<RestResult<T>>` (loading, result, loading done), and ViewModels extending `BaseViewModel` collect it with `request()`. The base class shows a loading overlay and handles errors: an error dialog if the screen has content, a full screen error with Retry if it doesn't. `BaseScreen` renders these states, so each screen only renders its own content.

## Requirements from the brief

| Requirement | How |
|---|---|
| RxJava / Coroutines | Coroutines and Flow |
| Dagger2 / Hilt | Hilt |
| Retrofit | Retrofit with kotlinx.serialization |
| MVVM | ViewModel with StateFlow |
| DiffUtil | `LazyColumn` with stable keys and immutable models |
| Divider | `HorizontalDivider` |
| Swipe to delete | `SwipeToDismissBox` |
| Detail screen | Navigation 3 destination |

## Build

Needs Android Studio (latest stable) and JDK 17.

```bash
./gradlew assembleDebug
```

The API base URL is in `gradle.properties` (`baseUrl`). You can override it for a single build:

```bash
./gradlew assembleDebug -PbaseUrl=https://example.com/
```

## Tests

```bash
./gradlew testDebugUnitTest
./gradlew connectedDebugAndroidTest
```

Unit tests cover the repository, both ViewModels and the result/error helpers. Instrumented tests cover the DAO with an in-memory database and the list screen UI.

```bash
./gradlew spotlessCheck detekt lintDebug
```

## Known limitations

- picsum.photos may return a different image for the same `random` value. Coil's disk cache keeps images stable, but they can change after clearing app data.
- Failed deletes and edits are not retried later. A WorkManager based sync queue would handle that.
- No search or pagination.
