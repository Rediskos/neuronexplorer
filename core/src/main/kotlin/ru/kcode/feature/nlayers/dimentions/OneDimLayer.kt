package ru.kcode.feature.nlayers.dimentions

import org.deeplearning4j.nn.api.Layer
import ru.kcode.feature.nlayers.DimLayer
import ru.kcode.feature.nlayers.models.NSphere
import ru.kcode.feature.nlayers.models.NSquare
import ru.kcode.utils.NetworkModelInstance
import kotlin.math.max

class OneDimLayer(
    private val count: Long,
    private val prevHeight: Float?,
    detailLayer: Layer,
    private var centerX: Float,
    private var centerY: Float,
    private val centerZ: Float,
) : DimLayer(detailLayer = detailLayer) {

    private var width: Float = 0.0f
    private var height: Float = 0.0f
    private var depth: Float = 0.0f
    private var heightCoeff: Float = DEFAULT_HEIGHT_COEFFICIENT

    init {
        prevHeight?.let {
            val defaultHeight = NSphere.DEFAULT_HEIGHT * (heightCoeff * count - 1)
            heightCoeff = max(heightCoeff, prevHeight/defaultHeight)
        }

        width = NSphere.DEFAULT_WIDTH
        height = NSphere.DEFAULT_HEIGHT * (heightCoeff * (count - 1))
        depth = NSphere.DEFAULT_WIDTH
    }

    override val holdedSignals: MutableList<Double> = MutableList(count.toInt()) { 0.0 }

    override val models: List<NSphere> by lazy {
        val models = ArrayList<NSphere>()
        val x = centerX
        var y = centerY - height / 2
        val z = centerZ
        for (i in 1..count) {
            val sphere = NSphere(x = x, y = y, z = z)
            y += NSphere.DEFAULT_HEIGHT * heightCoeff * 2f
            models.add(sphere)
        }
        models
    }
    override val activationModels: MutableList<NSquare> by lazy {
        val models = ArrayList<NSquare>()
        val x = centerX
        var y = centerY - height / 2
        val z = centerZ
        for (i in 1..count) {
            val square = NSquare(x = x, y = y, z = z)
            y += NSphere.DEFAULT_HEIGHT * heightCoeff * 2f
            models.add(square)
        }
        models
    }

    override fun getModelInstances(): List<NetworkModelInstance> = models.map {
        it.toModelInstance()
    }

    override fun getActionInstances(): List<NetworkModelInstance> = activationModels.map {
        it.toModelInstance()
    }

    override fun getWidth(): Float = width
    override fun getHeight(): Float = height
    override fun getDepth(): Float = depth

    override fun forgetSignals() {
        holdedSignals.forEachIndexed {index, _ ->
            holdedSignals[index] = 0.0
        }
    }
    companion object {
        private const val DEFAULT_HEIGHT_COEFFICIENT = 2f
    }
}
