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

### Offline first with Room

- JSONPlaceholder accepts `PUT` and `DELETE` but doesn't actually save anything.
- The UI only reads from Room. The network just writes into Room.
- Deleted posts are kept with `isDeleted`, edited posts are marked with `isLocallyModified`.
- A refresh skips both, so deleted posts don't come back and edits aren't overwritten.

### Images

- The image URL uses the post id, not the list position: `https://picsum.photos/300/300?random={id}&grayscale`.
- With the position, every image below a deleted row would change.
- The URL is built in the data layer. The domain model only has `imageUrl`.

### Delete and undo

1. Swiping hides the post right away and shows a snackbar with Undo.
2. `DELETE` is sent only if the snackbar closes without Undo.
3. If the request fails, the post is restored and an error is shown.

### Editing

- Save sends `PUT` first and writes to Room only if it succeeds.
- If it fails, the entered text stays on screen.
- The text is kept in `SavedStateHandle`, so it survives rotation and process death.

### Refresh

- First launch: the list is loaded from the API.
- Later launches: the cached list shows immediately and refreshes in the background.
- If a refresh fails while posts are on screen, the list stays and an error dialog is shown.

### Loading and errors

- Use cases return `Flow<RestResult<T>>`: loading, result, loading done.
- ViewModels extend `BaseViewModel` and collect it with `request()`.
- The base class shows a loading overlay. On error it shows a dialog if there is content, or a full screen error with Retry if there isn't.
- `BaseScreen` draws these states, so screens only draw their own content.

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
