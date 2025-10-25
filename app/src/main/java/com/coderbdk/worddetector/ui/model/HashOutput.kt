package com.coderbdk.worddetector.ui.model


import kotlinx.serialization.Serializable
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.text.Normalizer


@Serializable
data class HashOutput(val hashes: List<String>, val count: Int)

fun normalizeWord(s: String): String {
    val mapped = s
        .replace('4','a').replace('@','a')
        .replace('3','e').replace('1','i').replace('0','o')
        .replace('$','s').replace('5','s').replace('7','t')
    val norm = Normalizer.normalize(mapped, Normalizer.Form.NFKC)
    return norm.trim().lowercase()
}

fun sha256Hex(s: String): String {
    val md = MessageDigest.getInstance("SHA-256")
    val dig = md.digest(s.toByteArray(StandardCharsets.UTF_8))
    return dig.joinToString("") { "%02x".format(it) }
}