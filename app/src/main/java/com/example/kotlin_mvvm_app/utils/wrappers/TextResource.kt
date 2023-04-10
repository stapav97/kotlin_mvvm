package com.example.kotlin_mvvm_app.utils.wrappers

import android.content.Context

/**
 *   Wrapper to deffer calculation until context will be available.
 *   Use [toValue] to get String value.
 *
 *   @param pattern for [String.format]. [text] and [textResId] ignored in this case
 */
class TextResource(
    private val text: String? = null,
    private val textResId: Int? = null,

    private val pattern: TextResource? = null,
    private val args: List<TextResource>? = null,
) {
    fun toValue(context: Context): String {
        if (text != null) {
            return text
        }
        if (textResId != null) {
            return context.getString(textResId)
        }

        // Formatted
        val list = args!!.map { it.toValue(context) }.toTypedArray()
        return String.format(pattern!!.toValue(context), *list)
    }

    /**
     * For debugging. Use [toValue] to get String value
     */
    override fun toString(): String {
        if (text != null) {
            return "TextResource: $text"
        }
        if (textResId != null) {
            return "TextResource: StringResId"
        }

        // Formatted
        return "TextResourceFormatted: pattern: ${pattern.toString()}; args: ${args!!.joinToString { it.toString() }}"
    }



}