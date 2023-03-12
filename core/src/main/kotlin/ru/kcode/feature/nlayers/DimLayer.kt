package ru.kcode.feature.nlayers

import ru.kcode.feature.nlayers.models.NSphere
import ru.kcode.utils.NetworkModelInstance


abstract class DimLayer(val size: Int) {

    abstract val models: List<NSphere>
    constructor(): this(DEFAULT_NODE_SIZE)

    abstract fun getModelInstances(): List<NetworkModelInstance>
    abstract fun getWidth(): Float
    abstract fun getHeight(): Float
    abstract fun getDepth(): Float

    fun dispose() {
        models.forEach { it.dispose() }
    }
    companion object {
        const val DEFAULT_NODE_SIZE = 1
        const val DEFAULT_HEIGHT_SHIFT = NSphere.DEFAULT_HEIGHT * 5
        const val DEFAULT_WIDTH_SHIFT = NSphere.DEFAULT_WIDTH * 5
    }
}
