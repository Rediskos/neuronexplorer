package ru.kcode.feature.nlayers

import com.badlogic.gdx.graphics.g3d.ModelInstance
import org.jetbrains.kotlinx.dl.api.core.layer.Layer
import ru.kcode.feature.nlayers.dimentions.OneDimLayer
import ru.kcode.feature.nlayers.dimentions.ThreeDimLayer
import ru.kcode.feature.nlayers.models.NSphere

private const val ONE_DIMENSIONS = 2
private const val THREE_DIMENSIONS = 4
private const val NEXT_LAYER_X_SHIFT = NSphere.DEFAULT_WIDTH * 10

class NLayerRenderController(private val modelLayers: List<Layer>) {
    private var nextLayerCenterX = 0f
    private var nextLayerCenterY = 0f
    private var nextLayerCenterZ = 0f

    private val layers: List<DimLayer> by lazy {
        val layers = ArrayList<DimLayer>()
        modelLayers.forEach {
            analyzeLayer(it)?.let { dimLayer ->
                layers.add(dimLayer)
            }
        }
        nextLayerCenterY = layers.maxOfOrNull { it.getHeight() } ?: 0f
        nextLayerCenterY /= 2
        nextLayerCenterZ = layers.maxOfOrNull { it.getWidth() } ?: 0f
        nextLayerCenterZ /= 2
        layers
    }

    private val layerInstances: List<List<ModelInstance>> by lazy {
        layers.map {
            it.getModelInstances()
        }
    }
    fun getLayersInstances(): List<List<ModelInstance>> = layerInstances

    private fun analyzeLayer(layer: Layer): DimLayer? {
        val dims = layer.outputShape.dims()
        return when (dims.size) {
            ONE_DIMENSIONS -> {
                OneDimLayer(dims[1], centerX = nextLayerCenterX, centerY = nextLayerCenterY, centerZ = nextLayerCenterZ).also {
                 nextLayerCenterX += NEXT_LAYER_X_SHIFT
                }
            }
            THREE_DIMENSIONS -> {
                ThreeDimLayer(dims[1], dims[2], dims[3], nextLayerCenterX, nextLayerCenterY, nextLayerCenterZ).also {
                    nextLayerCenterX += NEXT_LAYER_X_SHIFT * dims[3]
                }
            }
            else -> null
        }
    }
    fun dispose() = layers.forEach { it.dispose() }

}
