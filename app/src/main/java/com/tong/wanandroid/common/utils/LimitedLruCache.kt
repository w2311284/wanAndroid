package com.tong.wanandroid.common.utils

import java.util.LinkedList

class LimitedLruCache<E>(private val limit: Int) : LinkedList<E>() {

    override fun add(element: E): Boolean {
        remove(element)
        super.add(element)
        while (size > limit) {
            super.remove()
        }
        return true
    }

    override fun get(index: Int): E {
        check(index in 0 until size) { IndexOutOfBoundsException("Index: $index, Size: $size") }
        return super.get(index).also {
            remove(it)
            add(it)
        }
    }
}