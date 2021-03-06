package com.example.jetpackcompose.theme

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.MutableState
import androidx.compose.state
import androidx.ui.core.Text
import androidx.ui.core.setContent
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.Icon
import androidx.ui.graphics.Color
import androidx.ui.layout.Column
import androidx.ui.layout.LayoutHeight
import androidx.ui.layout.LayoutPadding
import androidx.ui.layout.LayoutSize
import androidx.ui.layout.LayoutWidth
import androidx.ui.layout.Row
import androidx.ui.material.Card
import androidx.ui.material.DrawerState
import androidx.ui.material.IconButton
import androidx.ui.material.MaterialTheme
import androidx.ui.material.ModalDrawerLayout
import androidx.ui.material.Surface
import androidx.ui.material.Switch
import androidx.ui.material.TopAppBar
import androidx.ui.material.Typography
import androidx.ui.material.darkColorPalette
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.Menu
import androidx.ui.material.lightColorPalette
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontFamily
import androidx.ui.text.font.FontWeight
import androidx.ui.text.style.TextAlign
import androidx.ui.text.style.TextIndent
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.TextUnit
import androidx.ui.unit.dp
import androidx.ui.unit.sp
import com.example.jetpackcompose.core.LOREM_IPSUM_1
import com.example.jetpackcompose.core.LOREM_IPSUM_2
import com.example.jetpackcompose.core.LOREM_IPSUM_3

class DarkModeActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This is an extension function of Activity that sets the @Composable function that's
        // passed to it as the root view of the activity. This is meant to replace the .xml file
        // that we would typically set using the setContent(R.id.xml_file) method. The setContent
        // block defines the activity's layout.
        setContent {
            // Reacting to state changes is core to how Jetpack Compose works. This state variable
            // is used to control if dark mode is enabled or not. The value is toggled using a
            // button that's part of the ThemedDrawerAppComponent composable. Every time the
            // value of this variable changes, the relevant sub composables of
            // ThemedDrawerAppComponent that use enableDarkMode are automatically recomposed.
            val enableDarkMode = state { false }
            CustomTheme(enableDarkMode) {
                ThemedDrawerAppComponent(enableDarkMode)
            }
        }
    }
}

// We represent a Composable function by annotating it with the @Composable annotation. Composable
// functions can only be called from within the scope of other composable functions. We should
// think of composable functions to be similar to lego blocks - each composable function is in turn
// built up of smaller composable functions.
@Composable
fun CustomTheme(enableDarkMode: MutableState<Boolean>, children: @Composable()() -> Unit) {
    // lightColorPalette is a default implementation of the ColorPalette from the MaterialDesign
    // specification https://material.io/design/color/the-color-system.html#color-theme-creation.
    // for easy use. In this case, I'm just showing an example of how you can
    // override any of the values that are a part of the Palette even though I'm just using the
    // default values itself.
    val lightColors = lightColorPalette(
        primary = Color(0xFF6200EE),
        primaryVariant = Color(0xFF3700B3),
        onPrimary = Color(0xFFFFFFFF),
        secondary = Color(0xFF03DAC5),
        secondaryVariant = Color(0xFF0000FF),
        onSecondary = Color(0xFF000000),
        background = Color(0xFFFFFFFF),
        onBackground = Color(0xFF000000),
        surface = Color(0xFFFFFFFF),
        onSurface = Color(0xFF000000),
        error = Color(0xFFB00020),
        onError = Color(0xFFFFFFFF)
    )

    // lightColorPalette is a default implementation of dark mode ColorPalette from the
    // Material Design specification
    // https://material.io/design/color/the-color-system.html#color-theme-creation.
    val darkColors = darkColorPalette()
    val colors = if (enableDarkMode.value) darkColors else lightColors

    // Data class holding typography definitions as defined by the
    // Material typography specification
    // https://material.io/design/typography/the-type-system.html#type-scale
    val typography = Typography(
        body1 = TextStyle(fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            textIndent = TextIndent(firstLine = TextUnit.Companion.Sp(16)),
            textAlign = TextAlign.Justify
        )
    )

    // A MaterialTheme comprises of colors, typography and the child composables that are going
    // to make use of this styling.
    MaterialTheme(colors = colors, children = children, typography = typography)
}

@Composable
fun ThemedDrawerAppComponent(enableDarkMode: MutableState<Boolean>) {
    val (drawerState, onDrawerStateChange) = state { DrawerState.Closed }
    val currentScreen = state { ThemedDrawerAppScreen.Screen1 }
    ModalDrawerLayout(
        drawerState = drawerState,
        onStateChange = onDrawerStateChange,
        gesturesEnabled = drawerState == DrawerState.Opened,
        drawerContent = {
            ThemedDrawerContentComponent(
                currentScreen = currentScreen,
                closeDrawer = { onDrawerStateChange(DrawerState.Closed) }
            )
        },
        bodyContent = {
            ThemedBodyContentComponent(
                currentScreen = currentScreen.value,
                enableDarkMode = enableDarkMode,
                openDrawer = {
                    onDrawerStateChange(DrawerState.Opened)
                }
            )
        }
    )
}

@Composable
fun ThemedDrawerContentComponent(
    currentScreen: MutableState<ThemedDrawerAppScreen>,
    closeDrawer: () -> Unit
) {
    Column(modifier = LayoutHeight.Fill) {
        Clickable(onClick = {
            currentScreen.value = ThemedDrawerAppScreen.Screen1
            closeDrawer()
        }) {
            Text(text = ThemedDrawerAppScreen.Screen1.name, modifier = LayoutPadding(16.dp))
        }

        Clickable(onClick = {
            currentScreen.value = ThemedDrawerAppScreen.Screen2
            closeDrawer()
        }) {
            Text(text = ThemedDrawerAppScreen.Screen2.name, modifier = LayoutPadding(16.dp))
        }

        Clickable(onClick = {
            currentScreen.value = ThemedDrawerAppScreen.Screen3
            closeDrawer()
        }) {
            Text(text = ThemedDrawerAppScreen.Screen3.name, modifier = LayoutPadding(16.dp))
        }
    }
}

@Composable
fun ThemedBodyContentComponent(
    currentScreen: ThemedDrawerAppScreen,
    enableDarkMode: MutableState<Boolean>,
    openDrawer: () -> Unit
) {
    val onCheckChanged = { _: Boolean ->
        enableDarkMode.value = !enableDarkMode.value
    }
    when (currentScreen) {
        ThemedDrawerAppScreen.Screen1 -> ThemedScreen1Component(
            enableDarkMode.value,
            openDrawer,
            onCheckChanged
        )
        ThemedDrawerAppScreen.Screen2 -> ThemedScreen2Component(
            enableDarkMode.value,
            openDrawer,
            onCheckChanged
        )
        ThemedDrawerAppScreen.Screen3 -> ThemedScreen3Component(
            enableDarkMode.value,
            openDrawer,
            onCheckChanged
        )
    }
}

@Composable
fun ThemedScreen1Component(
    enableDarkMode: Boolean,
    openDrawer: () -> Unit,
    onCheckChanged: (Boolean) -> Unit
) {
    Column(modifier = LayoutSize.Fill) {
        TopAppBar(
            title = { Text("Screen 1") },
            navigationIcon = {
                IconButton(onClick = openDrawer) {
                    Icon(icon = Icons.Filled.Menu)
                }
            }
        )
        Card(
            modifier = LayoutWidth.Fill,
            color = MaterialTheme.colors().surface
        ) {
            Row(modifier = LayoutPadding(16.dp)) {
                Switch(checked = enableDarkMode, onCheckedChange = onCheckChanged)
                Text(
                    text = "Enable Dark Mode", style = MaterialTheme.typography().body1,
                    modifier = LayoutPadding(start = 8.dp)
                )
            }
        }
        Surface(modifier = LayoutWeight(1f)) {
            Text(
                text = LOREM_IPSUM_1, style = MaterialTheme.typography().body1,
                modifier = LayoutPadding(16.dp)
            )
        }
    }
}

@Composable
fun ThemedScreen2Component(
    enableDarkMode: Boolean,
    openDrawer: () -> Unit,
    onCheckChanged: (Boolean) -> Unit
) {
    Column(modifier = LayoutSize.Fill) {
        TopAppBar(
            title = { Text("Screen 2") },
            navigationIcon = {
                IconButton(onClick = openDrawer) {
                    Icon(icon = Icons.Filled.Menu)
                }
            }
        )
        Card(
            modifier = LayoutWidth.Fill,
            color = MaterialTheme.colors().surface
        ) {
            Row(modifier = LayoutPadding(16.dp)) {
                Switch(checked = enableDarkMode, onCheckedChange = onCheckChanged)
                Text(text = "Enable Dark Mode", style = MaterialTheme.typography().body1,
                    modifier = LayoutPadding(start = 8.dp))
            }
        }
        Surface(modifier = LayoutWeight(1f)) {
            Text(text = LOREM_IPSUM_2, style = MaterialTheme.typography().body1,
                modifier = LayoutPadding(16.dp)
            )
        }
    }
}

@Composable
fun ThemedScreen3Component(
    enableDarkMode: Boolean,
    openDrawer: () -> Unit,
    onCheckChanged: (Boolean) -> Unit
) {
    Column(modifier = LayoutSize.Fill) {
        TopAppBar(
            title = { Text("Screen 3") },
            navigationIcon = {
                IconButton(onClick = openDrawer) {
                    Icon(icon = Icons.Filled.Menu)
                }
            }
        )
        Card(
            modifier = LayoutWidth.Fill,
            color = MaterialTheme.colors().surface
        ) {
            Row(modifier = LayoutPadding(16.dp)) {
                Switch(checked = enableDarkMode, onCheckedChange = onCheckChanged)
                Text(text = "Enable Dark Mode", style = MaterialTheme.typography().body1,
                    modifier = LayoutPadding(start = 8.dp))
            }
        }
        Surface(modifier = LayoutWeight(1f)) {
            Text(text = LOREM_IPSUM_3, style = MaterialTheme.typography().body1,
                modifier = LayoutPadding(16.dp)
            )
        }
    }
}

enum class ThemedDrawerAppScreen {
    Screen1,
    Screen2,
    Screen3
}

/**
 * Android Studio lets you preview your composable functions within the IDE itself, instead of
 * needing to download the app to an Android device or emulator. This is a fantastic feature as you
 * can preview all your custom components(read composable functions) from the comforts of the IDE.
 * The main restriction is, the composable function must not take any parameters. If your composable
 * function requires a parameter, you can simply wrap your component inside another composable
 * function that doesn't take any parameters and call your composable function with the appropriate
 * params. Also, don't forget to annotate it with @Preview & @Composable annotations.
 */
@Preview
@Composable
fun CustomThemeLightPreview() {
    CustomTheme(enableDarkMode = state { false }) {
        Card {
            Text("Preview Text", modifier = LayoutPadding(32.dp))
        }
    }
}

@Preview
@Composable
fun CustomThemeDarkPreview() {
    CustomTheme(enableDarkMode = state { true }) {
        Card {
            Text("Preview Text", modifier = LayoutPadding(32.dp))
        }
    }
}