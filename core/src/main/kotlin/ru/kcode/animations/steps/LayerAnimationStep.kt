package ru.kcode.animations.steps

enum class LayerAnimationStep {
  PREPARING_FOR_DATA_MOVING,
    DATA_MOVING,
    PREPARING_FOR_NEURON_ACTIVATION,
    NEURON_ACTIVATION,
    PREPARING_FOR_BACKPROPAGATION,
    BACK_PROPAGATION_MOVING,
    BACK_PROPAGATION_ACTIVATING,
    ENDED
}
