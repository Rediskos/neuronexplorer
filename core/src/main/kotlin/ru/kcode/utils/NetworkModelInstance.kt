package ru.kcode.utils

import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.BoundingBox

class NetworkModelInstance: ModelInstance {
    val center = Vector3()
    val dimensions = Vector3()
    val radius: Float by lazy {
        dimensions.len()/2f
    }

    constructor(model: Model) : super(model)
    constructor(model: Model, x: Float, y: Float, z:Float) : super(model, x, y, z)

    init {
        calculateBoundingBox(bounds)
        bounds.getCenter(center)
        bounds.getDimensions(dimensions)
    }

    companion object {
        private val bounds = BoundingBox()
    }
}
