package ru.kcode.feature.nlayers

import ru.kcode.feature.nlayers.models.LayersConnector
import ru.kcode.utils.NetworkModelInstance

abstract class DimConnection {
    abstract val connector: List<LayersConnector>
    abstract fun getModelInstances(): List<NetworkModelInstance>

    fun dispose() = connector.forEach { it.dispose() }
}
