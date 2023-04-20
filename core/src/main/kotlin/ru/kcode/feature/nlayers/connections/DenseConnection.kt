package ru.kcode.feature.nlayers.connections

import org.deeplearning4j.nn.api.Layer
import ru.kcode.feature.nlayers.DimConnection
import ru.kcode.feature.nlayers.DimLayer
import ru.kcode.feature.nlayers.models.LayersConnector
import ru.kcode.utils.NetworkModelInstance

class DenseConnection(private val startLayer: DimLayer, private val endLayer: DimLayer, layer: Layer): DimConnection() {
    override val models: List<LayersConnector> by lazy {
        val models = arrayListOf<LayersConnector>()
        val weights = layer.getParam("W")
        val numInputs = weights.shape().first();// get the number of input features
        val numOutputs = weights.shape().last() // get the number of output features


        for (i in 0 until numInputs) {
            println("Weights for input feature $i:")
            for (j in 0 until numOutputs) {
                val weight = weights.getDouble(i, j) // get the weight for input i and output j
                val startModel = startLayer.models[i.toInt()]
                val endModel = endLayer.models[j.toInt()]
                val connection = LayersConnector(
                    startModel.x, startModel.y, startModel.z,
                    endModel.x, endModel.y, endModel.z,
                    weight
                )
                models.add(connection)
            }
        }
        models
    }
    private val modelInstance: List<NetworkModelInstance> by lazy {
        models.map {
            it.toModelInstance()
        }
    }
    override fun getModelInstances(): List<NetworkModelInstance> = modelInstance
}
