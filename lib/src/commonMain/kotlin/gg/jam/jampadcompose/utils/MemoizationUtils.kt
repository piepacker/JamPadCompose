package gg.jam.jampadcompose.utils

fun <R> (() -> R).memoize(): () -> R {
    var cache: R? = null
    return { cache ?: this().also { cache = it } }
}
