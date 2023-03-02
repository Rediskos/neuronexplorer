package ru.kcode.feature.nlayers.models

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder


class LayersConnector(
    val startX: Float,
    val startY: Float,
    val startZ: Float,
    val endX: Float,
    val endY: Float,
    val endZ: Float,
    val capLength: Float = DEFAULT_CAP_LENGTH,
    val stemThickness: Float = DEFAULT_STEM_THICKNESS,
    val divisions: Int = DEFAULT_DIVISIONS,
    val primitiveType: Int = GL20.GL_TRIANGLES,
    val material: Material = DEFAULT_MATERIAL,
    val attributes: Long = DEFAULT_ATTRIBUTES

) {
    private fun toModel(): Model {
        return ModelBuilder().createArrow(
            /* x1 = */ startX,
            /* y1 = */ startY,
            /* z1 = */ startZ,
            /* x2 = */ endX,
            /* y2 = */ endY,
            /* z2 = */ endZ,
            /* capLength = */ capLength,
            /* stemThickness = */ stemThickness,
            /* divisions = */ divisions,
            /* primitiveType = */ primitiveType,
            /* material = */ material,
            /* attributes = */ attributes
        )
    }

    fun toModelInstance(): ModelInstance {
        return ModelInstance(toModel())
    }
    companion object {
        const val DEFAULT_CAP_LENGTH = 1f
        const val DEFAULT_STEM_THICKNESS = 1f
        const val DEFAULT_DIVISIONS = 5
        val DEFAULT_MATERIAL = Material(ColorAttribute.createDiffuse(Color.GREEN))
        const val DEFAULT_ATTRIBUTES = (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal).toLong()
    }
}
