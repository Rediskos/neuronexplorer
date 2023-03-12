package ru.kcode.feature.nlayers.connections

import ru.kcode.feature.nlayers.DimConnection
import ru.kcode.feature.nlayers.DimLayer
import ru.kcode.feature.nlayers.models.LayersConnector
import ru.kcode.utils.NetworkModelInstance

class DenseConnection(private val startLayer: DimLayer, private val endLayer: DimLayer): DimConnection() {
    override val models: List<LayersConnector> by lazy {
        val models = arrayListOf<LayersConnector>()
        for (startModel in startLayer.models) {
            for (endModel in endLayer.models) {
                val connection = LayersConnector(
                    startModel.x, startModel.y, startModel.z,
                    endModel.x, endModel.y, endModel.z
                )
                models.add(connection)
            }
        }
        models
    }

    override fun getModelInstances(): List<NetworkModelInstance> = models.map { it.toModelInstance() }
}
