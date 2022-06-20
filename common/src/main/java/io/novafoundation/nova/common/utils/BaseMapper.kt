package io.novafoundation.nova.common.utils

import java.util.ArrayList

abstract class BaseMapper<From, To> {

    abstract fun map(from: From): To

    open fun mapList(froms: List<From>): List<To> {
        return ArrayList<To>(froms.size).apply {
            froms.forEach {
                add(map(it))
            }
        }
    }

}
