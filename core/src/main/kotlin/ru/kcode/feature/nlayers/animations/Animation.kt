package ru.kcode.feature.nlayers.animations

interface Animation {
    fun start()
    fun end()
    fun step(delta: Float): Boolean
    fun isActive(): Boolean
}
