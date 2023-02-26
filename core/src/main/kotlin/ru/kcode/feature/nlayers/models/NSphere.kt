package ru.kcode.feature.nlayers.models

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder

data class NSphere(
    var x: Float = 0f,
    var y: Float = 0f,
    var z: Float = 0f,
    val width: Float = DEFAULT_WIDTH,
    val height: Float = DEFAULT_HEIGHT,
    val depth: Float = DEFAULT_DEPTH,
    val divisionsU: Int = DEFAULT_DIVISION_U,
    val divisionsV: Int = DEFAULT_DIVISION_V,
    val material: Material = DEFAULT_MATERIAL,
    val attributes: Long = DEFAULT_ATTRIBUTES,
) {
    private fun toModel(): Model {
        return ModelBuilder().createSphere(width, height, depth, divisionsU, divisionsV, material, attributes)
    }

    fun toModelInstance(): ModelInstance {
        return ModelInstance(toModel(), x, y, z)
    }
    companion object {
        const val DEFAULT_WIDTH = 5f
        const val DEFAULT_HEIGHT = 5f
        const val DEFAULT_DEPTH = 5f
        const val DEFAULT_DIVISION_U = 5
        const val DEFAULT_DIVISION_V = 5
        val DEFAULT_MATERIAL = Material(ColorAttribute.createDiffuse(Color.GREEN))
        const val DEFAULT_ATTRIBUTES = (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal).toLong()
    }
}
