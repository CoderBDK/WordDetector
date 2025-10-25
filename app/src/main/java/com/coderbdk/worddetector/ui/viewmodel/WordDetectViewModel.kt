package com.coderbdk.worddetector.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.coderbdk.worddetector.ui.model.HighlightWord
import com.coderbdk.worddetector.ui.model.WordDetectIntent
import com.coderbdk.worddetector.ui.model.WordDetectState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WordDetectViewModel : ViewModel() {

    private val badWords = listOf("")

    private val _highlightWords = mutableListOf<String>()

    private val _state = MutableStateFlow(WordDetectState())
    val state: StateFlow<WordDetectState> = _state.asStateFlow()

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
        var result = text
        badWords.forEach { word ->
            val regex = Regex("\\b$word\\b", RegexOption.IGNORE_CASE)
            result = result.replace(regex) { "*".repeat(it.value.length) }
        }
        return result
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