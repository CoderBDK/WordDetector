package com.coderbdk.worddetector.ui.model

sealed class WordDetectIntent {
    data class InputText(val text: String) : WordDetectIntent()
    data class AddHighlightWords(val words: List<String>) : WordDetectIntent()
    object DetectBadWords : WordDetectIntent()
    object ClearAll : WordDetectIntent()
    data class ChangeDetectionMode(val mode: WordDetectionMode) : WordDetectIntent()
}
