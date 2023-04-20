package ru.kcode.feature.nlayers

import org.deeplearning4j.nn.api.Layer
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import ru.kcode.feature.nlayers.connections.DenseConnection
import ru.kcode.feature.nlayers.dimentions.OneDimLayer
import ru.kcode.feature.nlayers.models.LayersConnector
import ru.kcode.feature.nlayers.models.NSphere
import ru.kcode.utils.NetworkModelInstance

private const val ONE_DIMENSIONS = 2
private const val THREE_DIMENSIONS = 4
private const val NEXT_LAYER_X_SHIFT = NSphere.DEFAULT_WIDTH * 100

class NLayerRenderController(private val modelLayers: MultiLayerNetwork) {
    private var nextLayerCenterX = 0f
    private var nextLayerCenterY = 0f
    private var nextLayerCenterZ = 0f
    private var isFirstLayer = true
    private val tmpLayers: ArrayList<DimLayer> = arrayListOf()
    private val layers: List<DimLayer> by lazy {
        modelLayers.layers.forEach {
            analyzeLayer(it)?.let { dimLayer ->
                tmpLayers.add(dimLayer)
            }
            if (isFirstLayer) {
                isFirstLayer = false
                analyzeLayer(it)?.let { dimLayer ->
                    tmpLayers.add(dimLayer)
                }
            }
        }
        nextLayerCenterY = tmpLayers.maxOfOrNull { it.getHeight() } ?: 0f
        nextLayerCenterY /= 2
        nextLayerCenterZ = tmpLayers.maxOfOrNull { it.getWidth() } ?: 0f
        nextLayerCenterZ /= 2
        tmpLayers
    }

    private val connectios: List<DimConnection> by lazy {
        val connections = arrayListOf<DimConnection>()
        for (layerInd in 0 until (layers.size - 1)) {

            val connection = DenseConnection(
                layers[layerInd],
                layers[layerInd + 1],
                modelLayers.layers[layerInd]
            )
            connections.add(connection)
        }
        connections
    }

    private val layerInstances: List<List<NetworkModelInstance>> by lazy {
        layers.map {
            it.getModelInstances()
        }
    }

    private val connectionInstances: List<List<NetworkModelInstance>> by lazy {
        connectios.map {
            it.getModelInstances()
        }
    }
    fun getLayersInstances(): List<List<NetworkModelInstance>> = layerInstances
    fun getConnectionsInstances(): List<List<NetworkModelInstance>> = connectionInstances

    fun getConnections(): List<List<LayersConnector>> = connectios.map { it.models }
    fun getOneConnection(idx: Int): LayersConnector = connectios.map { it.models }.flatten()[idx]
    private fun analyzeLayer(layer: Layer): DimLayer? {

        return when (layer.javaClass.simpleName) {
            "DenseLayer" -> {
                print("kekw")
                val dims = layer.getParam("W").shape().run {
                    if (isFirstLayer) first() else last()
                }
                val prevHeight = tmpLayers.lastOrNull()?.getHeight()
                OneDimLayer(
                    dims,
                    prevHeight,
                    layer,
                    centerX = nextLayerCenterX,
                    centerY = nextLayerCenterY,
                    centerZ = nextLayerCenterZ,
                ).also {
                    nextLayerCenterX += NEXT_LAYER_X_SHIFT
                }
            }
            "OutputLayer" -> {
                print("lelw")
                val dims = layer.getParam("W").shape().run {
                    if (isFirstLayer) first() else last()
                }
                val prevHeight = tmpLayers.lastOrNull()?.getHeight()
                OneDimLayer(
                    dims,
                    prevHeight,
                    layer,
                    centerX = nextLayerCenterX,
                    centerY = nextLayerCenterY,
                    centerZ = nextLayerCenterZ
                ).also {
                    nextLayerCenterX += NEXT_LAYER_X_SHIFT
                }
            }
            else -> null
        }
    }
    fun dispose() = layers.forEach { it.dispose() }

}
