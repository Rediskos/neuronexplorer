package ru.kcode.feature.nlayers.models

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector3
import ru.kcode.feature.nlayers.animations.MovingAnimation
import ru.kcode.utils.NetworkModelInstance
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction
import kotlin.math.absoluteValue


class LayersConnector(
    val startX: Float,
    val startY: Float,
    val startZ: Float,
    val endX: Float,
    val endY: Float,
    val endZ: Float,
    val weight: Double,
    private val capLength: Float = DEFAULT_CAP_LENGTH,
    private val stemThickness: Float = DEFAULT_STEM_THICKNESS,
    private val divisions: Int = DEFAULT_DIVISIONS,
    private val primitiveType: Int = GL20.GL_LINES,
    private val material: Material = DEFAULT_MATERIAL,
    private val attributes: Long = DEFAULT_ATTRIBUTES

) {
    private val mover = NSphere(startX, startY, startZ)
    private val moverInstance by lazy { mover.toModelInstance() }
    private val moverAnimation by lazy {
        MovingAnimation(
            moverInstance,
            Vector3(startX, startY, startZ),
             Vector3(endX, endY, endZ)
        )
    }
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

    fun animate(delta: Float): Boolean {
        if (!moverAnimation.isActive()) moverAnimation.start()
        return moverAnimation.step(delta)
    }

    fun isAnimating(): Boolean = moverAnimation.isActive()
    fun toModelInstance(): NetworkModelInstance {
        return NetworkModelInstance(model)
    }

    fun draw(shapeRenderer: ShapeRenderer) {
        shapeRenderer.color = DEFAULT_COLOR;
        Gdx.gl.glLineWidth(weight.toFloat().absoluteValue * 10)
        shapeRenderer.line(
            startX,
            startY,
            startZ,
            endX,
            endY,
            endZ,
        )
    }

    fun getMoverInstance(): ModelInstance = moverInstance
    fun dispose() {
        model.dispose()
    }

    companion object {
        const val DEFAULT_CAP_LENGTH = 0f
        const val DEFAULT_STEM_THICKNESS = 0.01f
        const val DEFAULT_DIVISIONS = 2
        val DEFAULT_COLOR: Color = Color.GREEN
        val DEFAULT_MATERIAL = Material(ColorAttribute.createDiffuse(DEFAULT_COLOR))
        const val DEFAULT_ATTRIBUTES = (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal).toLong()
    }
}
