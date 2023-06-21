package ru.kcode.feature.nlayers

import org.deeplearning4j.nn.api.Layer
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.api.ops.impl.scalar.Relu6
import org.nd4j.linalg.learning.config.AdaBelief.DEFAULT_LEARNING_RATE
import ru.kcode.feature.nlayers.models.LayersConnector
import ru.kcode.feature.nlayers.models.NSphere
import ru.kcode.feature.nlayers.models.NSquare
import ru.kcode.utils.NetworkModelInstance


abstract class DimLayer(
    val size: Int = DEFAULT_NODE_SIZE,
    val detailLayer: Layer,
    open var isOutput: Boolean = false
) {

    abstract val models: List<NSphere>
    abstract val activationModels: MutableList<NSquare>
    abstract val backprobModels: MutableList<NSquare>
    abstract val weightModels: List<NSquare>
    abstract val holdedSignals: MutableList<Double>
    abstract val backProbInfluence: MutableList<Double>
    var backProbDeltas: DoubleArray = DoubleArray(
        detailLayer.getParam("W").shape().let { if (isOutput) it.last() else it.first() }.toInt()
    )

    abstract fun getModelInstances(): List<NetworkModelInstance>
    abstract fun getActionInstances(): List<NetworkModelInstance>

    fun getBackProbInstances() = backprobModels.map { it.toModelInstance() }
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

    fun calculateLoss(prev: DoubleArray, connection: DimConnection) {
        if (isOutput) {
            backProbDeltas =
                DoubleArray(holdedSignals.size) { i -> (holdedSignals[i] - prev[i]) * reluDerivative(holdedSignals[i]) }
            return
        }

        val nextDelta = DoubleArray(detailLayer.getParam("W").shape().last().toInt())

        backprobModels.forEachIndexed { index, nSquare ->
            backprobModels[index] = backprobModels[index].copy(depth = 0f)
        }

        connection.connector.forEach {
            val weight = it.weight
            val newDepth =
                weight - DEFAULT_LEARNING_RATE * prev[it.outputInd.toInt()] * holdedSignals[it.inputInd.toInt()]
            backprobModels[it.inputInd.toInt()] = backprobModels[it.inputInd.toInt()].let{ oldBackProb ->
                oldBackProb.copy(
                    depth = oldBackProb.depth + newDepth.toFloat()
                )
            }
            nextDelta[it.inputInd.toInt()] += prev[it.outputInd.toInt()] * weight
        }


        backProbDeltas = nextDelta

    }

    private fun reluDerivative(x: Double): Double {
        return if (x > 0) 1.0 else 0.0
    }
    private fun reLuFunction(num: Float) = if (num < 0) 0f else num
    fun dispose() {
        models.forEach { it.dispose() }
    }
    companion object {
        const val DEFAULT_LEARNING_RATE = 0.01f
        const val DEFAULT_NODE_SIZE = 1
        const val DEFAULT_HEIGHT_SHIFT = NSphere.DEFAULT_HEIGHT * 5
        const val DEFAULT_WIDTH_SHIFT = NSphere.DEFAULT_WIDTH * 5
        const val ACTIVATION_COEFICIETN = 10f
    }
}
