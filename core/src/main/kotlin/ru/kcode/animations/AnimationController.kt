package ru.kcode.animations

import ru.kcode.animations.steps.LayerAnimationStep
import ru.kcode.feature.nlayers.DimConnection
import ru.kcode.feature.nlayers.DimLayer

class AnimationController(
    private val numberOfLayers: Int,
    private val connections: List<DimConnection>,
    private val layers: List<DimLayer>
) {
    private val maxStage by lazy { numberOfLayers - 2 }
    private var currentStage = 0
    private var stageType: LayerAnimationStep = LayerAnimationStep.PREPARING_FOR_DATA_MOVING
    private var currentConnections: DimConnection = currentConnections()
    private lateinit var currentLayer: DimLayer
    private lateinit var nextLayer: DimLayer

    init {

    }

    fun animateStage(delta: Float): Boolean {
        if (currentStage > maxStage) {
            stageType = LayerAnimationStep.ENDED
            return false
        }
        when (stageType) {
            LayerAnimationStep.PREPARING_FOR_DATA_MOVING -> preparingForDataMoving(delta)
            LayerAnimationStep.DATA_MOVING -> dataMoving(delta).also {
                if (!it) {
                    stageType = LayerAnimationStep.PREPARING_FOR_NEURON_ACTIVATION
                }
            }
            LayerAnimationStep.PREPARING_FOR_NEURON_ACTIVATION -> {
                stageType = LayerAnimationStep.NEURON_ACTIVATION
                prepairingForNeuronActivation()
            }
            LayerAnimationStep.NEURON_ACTIVATION -> {
                stageType = LayerAnimationStep.PREPARING_FOR_DATA_MOVING
                currentStage += 1
            }
            LayerAnimationStep.BACK_PROPAGATION_MOVING -> TODO()
            LayerAnimationStep.ENDED -> {
                currentStage = 0
                stageType = LayerAnimationStep.PREPARING_FOR_DATA_MOVING
            }
        }
        return true
    }

    fun restartAnimation() {
        currentStage = 0
        stageType = LayerAnimationStep.PREPARING_FOR_DATA_MOVING
    }
    private fun dataMoving(delta: Float): Boolean {
        var stillActive = false
        currentConnections.connector.forEach {
            if (it.isAnimating()) {
                stillActive = true
                it.animate(delta)
            }
        }
        return stillActive
    }

    private fun preparingForDataMoving(delta: Float) {
        currentConnections = currentConnections()
        currentLayer = currentLayer()
        currentConnections.connector.forEach {
            it.newInputSignal(currentLayer.holdedSignals[it.inputInd.toInt()])
            it.animate(delta)
        }
        stageType = LayerAnimationStep.DATA_MOVING
    }

    private fun prepairingForNeuronActivation() {
        nextLayer = nextLayer()
        currentConnections.connector.forEach {
            nextLayer.holdedSignals[it.outputInd.toInt()] += it.getSignalStrength() ?: 0.0
        }
    }
    private fun currentConnections() = connections[currentStage]
    private fun currentLayer() = layers[currentStage]
    private fun nextLayer() = layers[currentStage + 1]

    fun getCurrentConnections() = currentConnections.connector.map { it.getMoverInstance() }
}
