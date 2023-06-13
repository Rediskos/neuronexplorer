package ru.kcode.feature.nlayers

import org.deeplearning4j.nn.api.Layer
import org.nd4j.linalg.api.ops.impl.scalar.Relu6
import ru.kcode.feature.nlayers.models.LayersConnector
import ru.kcode.feature.nlayers.models.NSphere
import ru.kcode.feature.nlayers.models.NSquare
import ru.kcode.utils.NetworkModelInstance


abstract class DimLayer(val size: Int = DEFAULT_NODE_SIZE, val detailLayer: Layer) {

    abstract val models: List<NSphere>
    abstract val activationModels: MutableList<NSquare>
    abstract val holdedSignals: MutableList<Double>

    abstract fun getModelInstances(): List<NetworkModelInstance>
    abstract fun getActionInstances(): List<NetworkModelInstance>
    abstract fun getWidth(): Float
    abstract fun getHeight(): Float
    abstract fun getDepth(): Float

    abstract fun forgetSignals()

    fun activateNeurons() {
        activationModels.forEachIndexed{index: Int, nSquare: NSquare ->
            val current = activationModels[index]
            val signal = reLuFunction(holdedSignals[index].toFloat())
            activationModels[index] = current.copy(
                depth = NSphere.DEFAULT_DEPTH * signal * ACTIVATION_COEFICIETN,
                material = NSphere.getMaterial(LayersConnector.getColorForWeight(holdedSignals[index].toFloat()))
            )
        }
    }

    private fun reLuFunction(num: Float) = if (num < 0) 0f else num
    fun dispose() {
        models.forEach { it.dispose() }
    }
    companion object {
        const val DEFAULT_NODE_SIZE = 1
        const val DEFAULT_HEIGHT_SHIFT = NSphere.DEFAULT_HEIGHT * 5
        const val DEFAULT_WIDTH_SHIFT = NSphere.DEFAULT_WIDTH * 5
        const val ACTIVATION_COEFICIETN = 10f
    }
}
