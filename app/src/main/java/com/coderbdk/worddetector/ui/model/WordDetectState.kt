package com.coderbdk.worddetector.ui.model

data class WordDetectState(
    val inputText: String = "",
    val filteredText: String = "",
    val highlightedWords: List<HighlightWord> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val detectionMode: WordDetectionMode = WordDetectionMode.HIGHLIGHT_WORDS
)