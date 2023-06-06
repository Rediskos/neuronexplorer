package ru.kcode.animations

import com.badlogic.gdx.graphics.g3d.ModelBatch
import ru.kcode.animations.steps.LayerAnimationStep
import ru.kcode.feature.nlayers.DimConnection

class AnimationController(
    private val numberOfLayers: Int,
    private val connections: List<DimConnection>
) {
    private val maxStage by lazy { numberOfLayers - 2 }
    private var currentStage = 0
    private var stageType: LayerAnimationStep = LayerAnimationStep.DATA_MOVING
    lateinit var currentConnections: DimConnection

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
        currentConnections = connections[currentStage]
        currentConnections.connector.forEach {
            it.animate(delta)
        }
    }

    fun getCurrentConnections() = currentConnections.connector.map { it.getMoverInstance() }
}
