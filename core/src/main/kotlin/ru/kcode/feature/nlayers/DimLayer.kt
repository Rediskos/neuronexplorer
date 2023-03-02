package ru.kcode.feature.nlayers

import com.badlogic.gdx.graphics.g3d.ModelInstance
import ru.kcode.feature.nlayers.models.NSphere


abstract class DimLayer(val size: Int) {

    abstract val models: List<NSphere>
    constructor(): this(DEFAULT_NODE_SIZE)

    abstract fun getModelInstances(): List<ModelInstance>
    abstract fun getWidth(): Float
    abstract fun getHeight(): Float
    abstract fun getDepth(): Float

    fun dispose() {
        models.forEach { it.dispose() }
    }

    companion object {
        const val DEFAULT_NODE_SIZE = 1
    }
}
