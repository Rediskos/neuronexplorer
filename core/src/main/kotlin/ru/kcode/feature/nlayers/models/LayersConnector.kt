package ru.kcode.feature.nlayers.models

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import ru.kcode.utils.NetworkModelInstance


class LayersConnector(
    private val startX: Float,
    private val startY: Float,
    private val startZ: Float,
    private val endX: Float,
    private val endY: Float,
    private val endZ: Float,
    private val capLength: Float = DEFAULT_CAP_LENGTH,
    private val stemThickness: Float = DEFAULT_STEM_THICKNESS,
    private val divisions: Int = DEFAULT_DIVISIONS,
    private val primitiveType: Int = GL20.GL_LINES,
    private val material: Material = DEFAULT_MATERIAL,
    private val attributes: Long = DEFAULT_ATTRIBUTES

) {
    private val model: Model by lazy {
        ModelBuilder().createArrow(
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

    fun toModelInstance(): NetworkModelInstance {
        return NetworkModelInstance(model)
    }

    fun dispose() {
        model.dispose()
    }

    companion object {
        const val DEFAULT_CAP_LENGTH = 0.01f
        const val DEFAULT_STEM_THICKNESS = 0.01f
        const val DEFAULT_DIVISIONS = 2
        val DEFAULT_MATERIAL = Material(ColorAttribute.createDiffuse(Color.GREEN))
        const val DEFAULT_ATTRIBUTES = (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal).toLong()
    }
}
