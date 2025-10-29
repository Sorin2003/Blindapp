package com.example.blindapp // Your package name is correct

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.blindapp.ui.theme.BlindAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // We are not using enableEdgeToEdge() to have full control over the black background
        setContent {
            BlindAppTheme {
                // Surface container that uses the background color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black // Set the background to black as requested
                ) {
                    AccessibilityNavigator()
                }
            }
        }
    }
}

@Composable
fun AccessibilityNavigator() {
    // 1. Define the list of functions the user can select
    val functions = listOf("Read Text", "Describe Objects", "Describe Nature")
    var currentIndex by remember { mutableIntStateOf(0) }
    val currentFunction = functions[currentIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black), // Black background for the entire screen
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween // Pushes content to the top and bottom
    ) {
        // 2. The selected function text at the top of the screen
        Text(
            text = currentFunction,
            color = Color.White,
            fontSize = 40.sp, // Large, readable font size
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .padding(top = 100.dp)
                // *** ACCESSIBILITY CRITICAL ***
                // This makes TalkBack automatically read the text whenever it changes.
                .semantics { liveRegion = LiveRegionMode.Polite }
        )

        // 3. The navigation arrows at the bottom of the screen
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 50.dp), // Pushes arrows up from the very bottom edge
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Arrow
            ArrowButton(
                direction = "Left",
                onClick = {
                    currentIndex = if (currentIndex == 0) {
                        functions.size - 1 // Wrap around to the last item
                    } else {
                        currentIndex - 1
                    }
                }
            )

            // Right Arrow
            ArrowButton(
                direction = "Right",
                onClick = {
                    currentIndex = (currentIndex + 1) % functions.size // Wrap around to the first item
                }
            )
        }
    }
}

@Composable
fun ArrowButton(direction: String, onClick: () -> Unit) {
    val description = if (direction == "Left") {
        "Navigate to previous function"
    } else {
        "Navigate to next function"
    }

    Icon(
        imageVector = if (direction == "Left") Icons.Default.ArrowBack else Icons.Default.ArrowForward,
        contentDescription = description, // This clear description is read by TalkBack
        tint = Color.White,
        modifier = Modifier
            .size(150.dp) // Very large touch target
            .clickable(
                onClick = onClick,
                role = Role.Button, // Semantically this is a button
                indication = null, // Removes the visual ripple effect
                interactionSource = remember { MutableInteractionSource() }
            )
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun AccessibilityNavigatorPreview() {
    BlindAppTheme {
        AccessibilityNavigator()
    }
}