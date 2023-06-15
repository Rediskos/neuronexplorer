package ru.kcode.animations

interface Animation {
    fun start()
    fun end()
    fun step(delta: Float): Boolean

    fun reverse(delta: Float): Boolean
    fun isActive(): Boolean
}
