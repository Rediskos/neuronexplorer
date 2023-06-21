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
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector3
import ru.kcode.animations.MovingAnimation
import ru.kcode.utils.NetworkModelInstance
import kotlin.math.abs
import kotlin.math.absoluteValue


class LayersConnector(
    val startX: Float,
    val startY: Float,
    val startZ: Float,
    val endX: Float,
    val endY: Float,
    val endZ: Float,
    val inputInd: Long,
    val outputInd: Long,
    val weight: Double,
    private val capLength: Float = DEFAULT_CAP_LENGTH,
    private val stemThickness: Float = DEFAULT_STEM_THICKNESS,
    private val divisions: Int = DEFAULT_DIVISIONS,
    private val primitiveType: Int = GL20.GL_LINES,
    private val material: Material = DEFAULT_MATERIAL,
    private val attributes: Long = DEFAULT_ATTRIBUTES

) {
    private val weightColor: Color = Color(
        MathUtils.clamp(-weight.toFloat(), 0f, 1f),
        MathUtils.clamp(weight.toFloat(), 0f, 1f),
        0f,
        1f
    )
    private val defaultHeight: Float = weight.toFloat().absoluteValue * 6
    private val mover = NSphere(
        startX,
        startY,
        startZ,
        height = defaultHeight,
        width = defaultHeight,
        depth = defaultHeight,
        material = NSphere.getMaterial(weightColor)
    )

    private var inputSignal: Double? = null
    private var outputSignal: Double? = null

    private val moverInstance by lazy { mover.toModelInstance() }
    private val moverAnimation by lazy {
        MovingAnimation(
            moverInstance,
            Vector3(startX, startY, startZ),
            Vector3(endX, endY, endZ)
        )
    }

    private var moverForSignal: NetworkModelInstance? = null
    private var moverForSignalAnimation: MovingAnimation? = null

    private var moverBackPropagation: NetworkModelInstance? = null
    private var moverBackPropagationAnimation: MovingAnimation? = null

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
        if (moverForSignalAnimation?.isActive() == false) moverForSignalAnimation?.start()
        return moverForSignalAnimation?.step(delta) ?: false
    }

    fun animateReverse(delta: Float): Boolean {
        if (moverBackPropagationAnimation?.isActive() == false) moverBackPropagationAnimation?.start()
        return moverBackPropagationAnimation?.step(delta) ?: false
    }

    fun isAnimating(): Boolean = moverForSignalAnimation?.isActive() ?: false
    fun isAnimatingReverse(): Boolean = moverBackPropagationAnimation?.isActive() ?: false
    fun toModelInstance(): NetworkModelInstance {
        return NetworkModelInstance(model)
    }

    fun draw(shapeRenderer: ShapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.color = weightColor
        Gdx.gl.glLineWidth(weight.toFloat().absoluteValue * 10)
        shapeRenderer.line(
            startX,
            startY,
            startZ,
            endX,
            endY,
            endZ,
        )
        shapeRenderer.end()
    }

    fun newInputSignal(signal: Double) {
        inputSignal = signal
        outputSignal = signal * weight
        val weightWithSignal = (signal * defaultHeight).toFloat()
        moverForSignal = mover.copy(
            height = weightWithSignal,
            width = weightWithSignal,
            depth = weightWithSignal,
            material = NSphere.getMaterial(getColorForWeight(weightWithSignal))
        ).toModelInstance()
        moverForSignalAnimation = MovingAnimation(
            moverForSignal ?: moverInstance,
            Vector3(startX, startY, startZ),
            Vector3(endX, endY, endZ)
        )
    }

    fun newBackPropagationSignal(signal: Double) {
        val backSignal = signal.toFloat() * defaultHeight
        moverBackPropagation = mover.copy(
            height = abs(backSignal),
            width = abs(backSignal),
            depth = abs(backSignal),
            material = NSphere.getMaterial(Color.BLUE)
        ).toModelInstance()
        moverBackPropagationAnimation = MovingAnimation(
            moverBackPropagation ?: moverInstance,
            Vector3(endX, endY, endZ),
            Vector3(startX, startY, startZ),
        )
    }

    fun getSignalStrength() = outputSignal

    fun getMoverInstance(): ModelInstance = moverForSignal ?: moverInstance
    fun getMoverBackProbInstance(): ModelInstance = moverBackPropagation ?: moverInstance
    fun dispose() {
        model.dispose()
    }



    companion object {
        fun getColorForWeight(weight: Float) = Color(
            MathUtils.clamp(-weight, 0f, 1f),
            MathUtils.clamp(weight, 0f, 1f),
            0f,
            1f
        )

        fun getBlueColor() = Color(Color.BLUE)
        const val DEFAULT_CAP_LENGTH = 0f
        const val DEFAULT_STEM_THICKNESS = 0.01f
        const val DEFAULT_DIVISIONS = 2
        val DEFAULT_COLOR: Color = Color.GREEN
        val DEFAULT_MATERIAL = Material(ColorAttribute.createDiffuse(DEFAULT_COLOR))
        const val DEFAULT_ATTRIBUTES = (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal).toLong()
    }
}
