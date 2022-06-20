package io.novafoundation.nova.common.utils

import androidx.recyclerview.widget.DiffUtil

interface DiffEquals {
    fun isItemSame(other: Any?): Boolean

    fun isContentSame(other: Any?): Boolean

}
/**
 * Compares two objects by their parameters. The [properties] must
 * always be the properties of the [T] class. Otherwise, this function
 * will fail due to [UNCHECKED_CAST].
 */
/**
 * Simple Diff utils implementation. It will compare two items and
 * search for differences. Items should implement [DiffEquals] for
 * this callback.
 */
class DefaultDiffUtilCallback<T : DiffEquals> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(
        oldItem: T,
        newItem: T
    ): Boolean {
        return oldItem.isItemSame(newItem)
    }

    override fun areContentsTheSame(
        oldItem: T,
        newItem: T
    ): Boolean {
        return oldItem.isContentSame(newItem)
    }

}

@Suppress("UNCHECKED_CAST")
inline fun <reified T> T.equalTo(
    other: Any?,
    vararg properties: T.() -> Any?
): Boolean {
    if (other !is T) return false
    properties.forEach {
        val currentValue = it.invoke(this)
        val otherValue = it.invoke(other)
        if (currentValue is List<*>) {
            if (otherValue !is List<*> || !currentValue.isListContentEqualsOrSame(otherValue)) {
                return false
            }
        } else if (it.invoke(this) != it.invoke(other)) {
            return false
        }
    }
    return true
}

/**
 * Compare two lists by their content.
 * This method uses a default equal operation to compare list items
 * or the diff equals mechanic if the list item is implements [DiffEquals]
 * @return true when same, false otherwise
 */
fun List<*>.isListContentEqualsOrSame(other: List<*>): Boolean {
    if (size != other.size) return false
    withIndex().forEach {
        if (it.value is DiffEquals && other[it.index] is DiffEquals) {
            if (!(it.value as DiffEquals).isContentSame(other[it.index])) {
                return false
            }
        } else if (it.value != other[it.index]) {
            return false
        }
    }
    return true
}
