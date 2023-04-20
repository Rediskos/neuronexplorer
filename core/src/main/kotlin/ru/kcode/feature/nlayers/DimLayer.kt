package ru.kcode.feature.nlayers

import org.deeplearning4j.nn.api.Layer
import ru.kcode.feature.nlayers.models.NSphere
import ru.kcode.utils.NetworkModelInstance


abstract class DimLayer(val size: Int = DEFAULT_NODE_SIZE, val detailLayer: Layer) {

    abstract val models: List<NSphere>

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
