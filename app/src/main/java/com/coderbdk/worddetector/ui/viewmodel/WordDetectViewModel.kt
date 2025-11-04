package com.coderbdk.worddetector.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.coderbdk.worddetector.R
import com.coderbdk.worddetector.ui.model.HashOutput
import com.coderbdk.worddetector.ui.model.HighlightWord
import com.coderbdk.worddetector.ui.model.WordDetectIntent
import com.coderbdk.worddetector.ui.model.WordDetectState
import com.coderbdk.worddetector.ui.model.normalizeWord
import com.coderbdk.worddetector.ui.model.sha256Hex
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json

class WordDetectViewModel : ViewModel() {

    private val badWordHashes = mutableSetOf<String>()

    private val _highlightWords = mutableListOf<String>()

    private val _state = MutableStateFlow(WordDetectState())
    val state: StateFlow<WordDetectState> = _state.asStateFlow()

    fun loadBadWords(context: Context) {
        if (badWordHashes.isEmpty()) {
            val json = context.resources.openRawResource(R.raw.en_output_bad_word).bufferedReader()
                .use { it.readText() }
            val data =
                Json.decodeFromString<HashOutput>(json)
            badWordHashes.addAll(data.hashes)
            // Log.i("WordDetect", "Loaded ${badWordHashes.size} hashed words")
        }
    }

    fun handleIntent(intent: WordDetectIntent) {
        when (intent) {
            is WordDetectIntent.InputText -> {
                _state.value = _state.value.copy(inputText = intent.text)
            }

            is WordDetectIntent.AddHighlightWords -> {
                _highlightWords.clear()
                _highlightWords.addAll(intent.words)
                highlightWords(_state.value.inputText)
            }

            WordDetectIntent.DetectBadWords -> {
                val filtered = censorBadWords(_state.value.inputText)
                _state.value = _state.value.copy(filteredText = filtered)
            }

            WordDetectIntent.ClearAll -> {
                _state.value = WordDetectState()
            }

            is WordDetectIntent.ChangeDetectionMode -> {
                _state.update {
                    it.copy(
                        detectionMode = intent.mode
                    )
                }
            }
        }
    }

    private fun censorBadWords(text: String): String {
        val words = text.split("\\b".toRegex())
        val censored = words.map { token ->
            val normalized = normalizeWord(token)
            val hash = sha256Hex(normalized)
            if (badWordHashes.contains(hash)) {
                "*".repeat(token.length)
            } else token
        }
        return censored.joinToString("")
    }


    private fun highlightWords(text: String) {
        val highlights = mutableListOf<HighlightWord>()
        _highlightWords.forEach { word ->
            Regex("\\b${Regex.escape(word)}\\b", RegexOption.IGNORE_CASE).findAll(text)
                .forEach { match ->
                    highlights.add(
                        HighlightWord(
                            match.range.first,
                            match.range.last + 1,
                            match.value
                        )
                    )
                }
        }
        _state.value = _state.value.copy(highlightedWords = highlights)
    }
}