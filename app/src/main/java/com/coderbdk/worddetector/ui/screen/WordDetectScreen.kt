package com.coderbdk.worddetector.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.coderbdk.worddetector.ui.model.HighlightWord
import com.coderbdk.worddetector.ui.model.WordDetectIntent
import com.coderbdk.worddetector.ui.model.WordDetectState
import com.coderbdk.worddetector.ui.model.WordDetectionMode
import com.coderbdk.worddetector.ui.theme.BackgroundColor
import com.coderbdk.worddetector.ui.viewmodel.WordDetectViewModel
import java.util.Locale
import kotlin.math.min

enum class WordDetectContentState {
    DETECT_CONTENT, RESULT_CONTENT
}

@Composable
fun WordDetectScreen(viewModel: WordDetectViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()
    var contentState by remember { mutableStateOf(WordDetectContentState.DETECT_CONTENT) }

    AnimatedContent(
        targetState = contentState
    ) {
        when (it) {
            WordDetectContentState.DETECT_CONTENT -> {
                WordDetectContent(state, viewModel::handleIntent) {
                    contentState = WordDetectContentState.RESULT_CONTENT
                }
            }

            WordDetectContentState.RESULT_CONTENT -> {
                WordDetectResultContent(state) {
                    contentState = WordDetectContentState.DETECT_CONTENT
                }
            }
        }
    }

}


@Composable
fun WordDetectContent(
    state: WordDetectState,
    handleIntent: (WordDetectIntent) -> Unit,
    onChangeResultContentState: () -> Unit
) {

    var input by remember { mutableStateOf("") }
    var customHighlight by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .background(
                color = BackgroundColor
            )
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "WORD DETECTOR",
            fontSize = 24.sp,
            color = Color.Cyan,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = input,
            onValueChange = {
                input = it
                handleIntent(WordDetectIntent.InputText(it))
            },
            placeholder = { Text("Paste or type text here...", color = Color.Cyan.copy(0.4f)) },
            maxLines = 5,
            shape = RoundedCornerShape(16.dp),
            trailingIcon = {
                if (state.inputText.isNotEmpty()) {
                    IconButton(onClick = {
                        handleIntent(WordDetectIntent.ClearAll)
                        input = ""
                    }) {
                        Icon(Icons.Default.Close, null, tint = Color.Cyan)
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Cyan,
                unfocusedBorderColor = Color.Cyan.copy(0.4f),
                focusedTextColor = Color.Cyan,
                unfocusedTextColor = Color.Cyan,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp)
        )

        Spacer(Modifier.height(16.dp))

        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = state.detectionMode == WordDetectionMode.CENSOR_BAD_WORDS,
                onClick = {
                    handleIntent(WordDetectIntent.ChangeDetectionMode(WordDetectionMode.CENSOR_BAD_WORDS))
                },
                colors = RadioButtonDefaults.colors(
                    unselectedColor = Color.Cyan.copy(0.4f),
                    selectedColor = Color.Cyan,
                )
            )
            Text("Filter Bad Words".uppercase(Locale.ROOT), color = Color.Cyan.copy(0.4f))
            Spacer(Modifier.weight(1f))
            RadioButton(
                selected = state.detectionMode == WordDetectionMode.HIGHLIGHT_WORDS,
                onClick = {
                    handleIntent(
                        WordDetectIntent.ChangeDetectionMode(
                            WordDetectionMode.HIGHLIGHT_WORDS
                        )
                    )
                },
                colors = RadioButtonDefaults.colors(
                    unselectedColor = Color.Cyan.copy(0.4f),
                    selectedColor = Color.Cyan
                )
            )
            Text("Highlight Words".uppercase(Locale.ROOT), color = Color.Cyan.copy(0.4f))
        }

        if (state.detectionMode == WordDetectionMode.HIGHLIGHT_WORDS) {
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = customHighlight,
                onValueChange = { customHighlight = it },
                placeholder = {
                    Text(
                        "Words to highlight (comma separated)",
                        color = Color.Cyan.copy(0.4f)
                    )
                },
                maxLines = 5,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Cyan,
                    unfocusedBorderColor = Color.Cyan.copy(0.4f),
                    focusedTextColor = Color.Cyan,
                    unfocusedTextColor = Color.Cyan,
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                when (state.detectionMode) {
                    WordDetectionMode.CENSOR_BAD_WORDS -> {
                        handleIntent(WordDetectIntent.DetectBadWords)
                    }

                    WordDetectionMode.HIGHLIGHT_WORDS -> {
                        val words =
                            customHighlight.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                        handleIntent(WordDetectIntent.AddHighlightWords(words))

                    }
                }
                onChangeResultContentState()
            },
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(width = 2.dp, color = Color.Cyan),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .align(Alignment.CenterHorizontally)
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.Cyan.copy(0.5f),
                            BackgroundColor
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Text(
                "DETECT",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = Color.Cyan
            )
        }

    }
}

@Composable
fun HighlightedText(text: String, highlights: List<HighlightWord>) {
    if (highlights.isEmpty()) {
        Text(text, color = Color.Cyan)
        return
    }

    var lastIndex = 0
    val annotatedString = buildAnnotatedString {
        highlights.sortedBy { it.start }.forEach { h ->
            if (h.start > lastIndex) {
                append(text.substring(lastIndex, h.start))
            }
            pushStyle(SpanStyle(background = Color.Blue))
            append(text.substring(h.start, h.end))
            pop()
            lastIndex = h.end
        }
        if (lastIndex < text.length) append(text.substring(lastIndex))
    }
    Text(annotatedString, color = Color.Cyan)
}

@Composable
fun WordDetectResultContent(state: WordDetectState, onBack: () -> Unit) {
    BackHandler { onBack() }

    Column(
        modifier = Modifier
            .background(
                color = BackgroundColor
            )
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = onBack
            ) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, null, tint = Color.Cyan)
            }
            Text(
                "WORD DETECTOR",
                fontSize = 24.sp,
                color = Color.Cyan,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                modifier = Modifier.fillMaxWidth()
            )
        }
        when (state.detectionMode) {
            WordDetectionMode.CENSOR_BAD_WORDS -> {
                Text(
                    "Filtered Text:",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Cyan
                )
                Text(
                    state.filteredText,
                    modifier = Modifier.padding(top = 8.dp),
                    color = Color.Cyan
                )
            }

            WordDetectionMode.HIGHLIGHT_WORDS -> {
                Spacer(Modifier.height(16.dp))
                Text(
                    "Highlighted Words:",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Cyan
                )
                HighlightedText(state.inputText, state.highlightedWords)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WordDetectPreview() {
    WordDetectScreen()
}
