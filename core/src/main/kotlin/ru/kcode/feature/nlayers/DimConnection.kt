package ru.kcode.feature.nlayers

import ru.kcode.feature.nlayers.models.LayersConnector
import ru.kcode.utils.NetworkModelInstance

abstract class DimConnection {
    abstract val models: List<LayersConnector>
    abstract fun getModelInstances(): List<NetworkModelInstance>

    fun dispose() = models.forEach { it.dispose() }
}
