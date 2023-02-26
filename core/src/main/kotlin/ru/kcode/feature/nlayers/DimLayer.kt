package ru.kcode.feature.nlayers

import com.badlogic.gdx.graphics.g3d.ModelInstance


open abstract class DimLayer(val size: Int) {

    constructor(): this(DEFAULT_NODE_SIZE)

    abstract fun getModelInstances(): List<ModelInstance>

    companion object {
        const val DEFAULT_NODE_SIZE = 1
    }
}
