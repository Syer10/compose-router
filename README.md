# compose-router

[![Build](https://github.com/zsoltk/compose-router/workflows/Build/badge.svg)](https://github.com/zsoltk/compose-router/actions)
[![Version](https://jitpack.io/v/syer10/compose-router.svg)](https://jitpack.io/#syer10/compose-router)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

![logo](https://i.imgur.com/kKcAHa3.png)

## What's this?

Routing functionality for JetBrains Compose with back stack:

- Helps to map your whole application structure using Compose — not just the UI parts
- Supports a single-Window approach — one Window, no Navigation component needed
- Simply branch on current routing and compose any other @Composable
- Back stack saves the history of routing
- Desktop implementation of Android Bundle class used
- Can be integrated with automatic back press handling to go back in screen history
- Can be integrated with automatic scoped `savedInstanceState` persistence
- Supports routing based on deep links (POC impl)

Tested with JetBrains Compose version **0.4.0-build190**

## Sample app

**TachideskJUI** - [manga-reader](https://github.com/Suwayomi/TachideskJUI/) — Built with `compose-router`, adding proper screen history functionality.

## Download

Groovy:
```groovy
implementation 'ca.gosyer:compose-router:{latest-version}'
```
Kotlin:
```kotlin
implementation("ca.gosyer:compose-router:{latest-version}")
```

## How to use

On any level where routing functionality is needed, create a sealed class to represent your routing:

```kotlin
sealed class Routing {
    object AlbumList : Routing()
    data class PhotosOfAlbum(val album: Album) : Routing()
    data class FullScreenPhoto(val photo: Photo) : Routing()
}
```

Use the `Router` Composable and enjoy back stack functionality:

```kotlin
@Composable
fun GalleryView(defaultRouting: Routing) {
    Router("GalleryView", defaultRouting) { backStack ->
        // compose further based on current routing:
        when (val routing = backStack.last()) {
            is Routing.AlbumList -> AlbumList.Content(
                onAlbumSelected = {
                    // add a new routing to the back stack:
                    backStack.push(Routing.PhotosOfAlbum(it))
                })

            is Routing.PhotosOfAlbum -> PhotosOfAlbum.Content(
                album = routing.album,
                onPhotoSelected = {
                    // add a new routing to the back stack:
                    backStack.push(Routing.FullScreenPhoto(it))
                })

            is Routing.FullScreenPhoto -> FullScreenPhoto.Content(
                photo = routing.photo
            )
        }
    }
}
```

For more usage examples see the example apps.

To go back in the back stack, you can either call the `.pop()` method programmatically, or just press the back button on the device (see next section for back press integration).

Back stack operations:

- **push()**
- **pushAndDropNested()**
- **pop()**
- **replace()**
- **newRoot()**

## Connect it to back press event

To ensure that back press automatically pops the back stack and restores history, add this to your main function:

```kotlin
fun main() {
    SwingUtilities.invokeLater {
        val window = AppWindow(
            title = "App name"
        )

        val backPressHandler = BackPressHandler()
        window.keyboard.setShortcut(Key.Home) {
            backPressHandler.handle()
        }

        window.show {
            setContent {
                Providers(
                    LocalBackPressHandler provides backPressHandler
                ) {
                    // Your root composable goes here
                }
            }
        }
    }
}
```

## Connect it to savedInstanceState

Router can automatically add scoped Bundle support for your client code.

Minimal setup:

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                BundleScope(savedInstanceState) {
                    // Your root composable goes here
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.saveLocal()
    }
}
```

In client code you can now use:

```kotlin
@Composable
fun Content() {
    var counter by persistentInt("counter", 0)

    Clickable(onClick = { counter++ }) {
        Text("Counter value saved/restored from bundle: $counter")
    }
}
```