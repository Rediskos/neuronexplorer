package ru.kcode.feature.nlayers.models

import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import ru.kcode.utils.NetworkModelInstance

data class NSquare(
    var x: Float = 0f,
    var y: Float = 0f,
    var z: Float = 0f,
    val width: Float = NSphere.DEFAULT_WIDTH,
    val height: Float = NSphere.DEFAULT_HEIGHT,
    val depth: Float = NSphere.DEFAULT_DEPTH,
    val divisionsU: Int = NSphere.DEFAULT_DIVISION_U,
    val divisionsV: Int = NSphere.DEFAULT_DIVISION_V,
    val material: Material = NSphere.DEFAULT_MATERIAL,
    val attributes: Long = NSphere.DEFAULT_ATTRIBUTES,
) {
    private val model: Model by lazy {
        ModelBuilder().createBox(width, height, depth, material, attributes)
    }

    fun toModelInstance(): NetworkModelInstance {
        return NetworkModelInstance(model, x, y, z)
    }

    fun dispose() {
        model.dispose()
    }
}