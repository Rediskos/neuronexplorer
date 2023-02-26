package ru.kcode.feature.nlayers

import com.badlogic.gdx.graphics.g3d.ModelInstance
import org.jetbrains.kotlinx.dl.api.core.layer.Layer
import ru.kcode.feature.nlayers.models.NSphere

private const val ONE_DIMENSIONS = 2
private const val THREE_DIMENSIONS = 4
private const val NEXT_LAYER_X_SHIFT = NSphere.DEFAULT_WIDTH * 2

class NLayerRenderController(private val modelLayers: List<Layer>) {
    private var nextLayerX = 0f
    private var nextLayerY = 0f
    private var nextLayerZ = 0f

    private val layers: List<DimLayer> by lazy {
        val layers = ArrayList<DimLayer>()
        modelLayers.forEach {
            analyzeLayer(it)?.let { dimLayer ->
                layers.add(dimLayer)
                nextLayerX += NEXT_LAYER_X_SHIFT
            }
        }
        layers
    }

    fun getLayerInstances(): List<List<ModelInstance>> {
        return layers.map {
            it.getModelInstances()
        }
    }

    private fun analyzeLayer(layer: Layer): DimLayer? {
        val dims = layer.outputShape.dims()
        return when (dims.size) {
            ONE_DIMENSIONS -> OneDimLayer(dims[1], x = nextLayerX, y = nextLayerY, z = nextLayerZ)
            else -> null
        }
    }
}
