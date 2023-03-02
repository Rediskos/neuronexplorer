package ru.kcode.feature.nlayers

import com.badlogic.gdx.graphics.g3d.ModelInstance

abstract class DimConnections {
    abstract fun getModelInstances(): List<ModelInstance>
}
