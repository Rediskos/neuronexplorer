package ru.kcode.animations

import ru.kcode.animations.steps.LayerAnimationStep
import ru.kcode.feature.nlayers.DimConnection
import ru.kcode.feature.nlayers.DimLayer
import ru.kcode.utils.NetworkModelInstance

class AnimationController(
    private val numberOfLayers: Int,
    private val connections: List<DimConnection>,
    private val layers: List<DimLayer>,
    private val target: DoubleArray
) {
    private val maxStage by lazy { numberOfLayers - 2 }
    private var currentStage = 0
    private var stageType: LayerAnimationStep = LayerAnimationStep.PREPARING_FOR_DATA_MOVING
    private var currentConnections: DimConnection = currentConnections()
    private var currentActivationInstances: List<NetworkModelInstance>? = null
    private var currentBackpropInstances: List<NetworkModelInstance>? = null
    private lateinit var currentLayer: DimLayer
    private lateinit var nextLayer: DimLayer
    private var backPropagationState = false
    private var prevDeltas = target
    init {

    }

    fun animateStage(delta: Float): Boolean {
        if (currentStage > maxStage && !backPropagationState) {
            backPropagationState = true
            stageType = LayerAnimationStep.PREPARING_FOR_BACKPROPAGATION
            prevDeltas = target
        }

        if (currentStage < 1 && stageType == LayerAnimationStep.PREPARING_FOR_BACKPROPAGATION) {
            stageType = LayerAnimationStep.ENDED
        }
        when (stageType) {
            LayerAnimationStep.PREPARING_FOR_DATA_MOVING -> preparingForDataMoving(delta)
            LayerAnimationStep.DATA_MOVING -> dataMoving(delta).also {
                if (!it) {
                    stageType = LayerAnimationStep.PREPARING_FOR_NEURON_ACTIVATION
                }
            }
            LayerAnimationStep.PREPARING_FOR_NEURON_ACTIVATION -> {
                prepairingForNeuronActivation()
            }
            LayerAnimationStep.NEURON_ACTIVATION -> {
                neuronActivation()
            }
            LayerAnimationStep.PREPARING_FOR_BACKPROPAGATION -> {
                preparingForBackPropagation(delta)
            }
            LayerAnimationStep.BACK_PROPAGATION_MOVING -> backPropagationMoving(delta).also {
                if (!it) {
                    stageType = LayerAnimationStep.BACK_PROPAGATION_ACTIVATING
                }
            }
            LayerAnimationStep.BACK_PROPAGATION_ACTIVATING -> {
                backPropagationActivating()
            }
            LayerAnimationStep.ENDED -> {
                currentStage = 0
                stageType = LayerAnimationStep.PREPARING_FOR_DATA_MOVING
                backPropagationState = false
            }
        }
        return true
    }

    private fun backPropagationActivating() {
        val prevLayer = prevLayer()
        currentBackpropInstances = prevLayer.getBackProbInstances()
        prevDeltas = currentLayer.backProbDeltas
        stageType = LayerAnimationStep.PREPARING_FOR_BACKPROPAGATION
        currentStage -= 1
    }

    private fun backPropagationMoving(delta: Float): Boolean {
        var stillActive = false
        currentConnections.connector.forEach {
            if (it.isAnimating()) {
                stillActive = true
                it.animateReverse(delta)
            }
        }
        return stillActive
    }

    private fun preparingForBackPropagation(delta: Float) {
        currentConnections = currentConnections(-1)
        currentLayer.calculateLoss(prevDeltas, currentConnections)
        currentConnections.connector.forEach {
            it.newBackPropagationSignal(currentLayer.backProbDeltas[it.outputInd.toInt()])
            it.animateReverse(delta)
        }
        stageType = LayerAnimationStep.BACK_PROPAGATION_MOVING
    }

    private fun neuronActivation() {
        nextLayer.activateNeurons()
        currentActivationInstances = nextLayer().getActionInstances()
        stageType = LayerAnimationStep.PREPARING_FOR_DATA_MOVING
        currentStage += 1
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
        nextLayer.forgetSignals()
        currentConnections.connector.forEach {
            nextLayer.holdedSignals[it.outputInd.toInt()] += it.getSignalStrength() ?: 0.0
        }
        stageType = LayerAnimationStep.NEURON_ACTIVATION
    }
    private fun currentConnections(shift: Int = 0) = connections[currentStage + shift]
    fun currentActivationInstances() = currentActivationInstances
    private fun currentLayer() = layers[currentStage]
    private fun nextLayer() = layers[currentStage + 1]
    private fun prevLayer() = layers[currentStage - 1]

    fun getCurrentConnections() = currentConnections.connector.map { it.getMoverInstance() }
}
