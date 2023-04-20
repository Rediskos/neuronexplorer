package ru.kcode.feature.nlayers.dimentions

import org.deeplearning4j.nn.api.Layer
import ru.kcode.feature.nlayers.DimLayer
import ru.kcode.feature.nlayers.models.NSphere
import ru.kcode.utils.NetworkModelInstance

class ThreeDimLayer(
    private val rows: Long,
    private val columns: Long,
    private val layersCount: Long,
    detailLayer: Layer,
    private val centerX: Float,
    private val centerY: Float,
    private val centerZ: Float
) : DimLayer(detailLayer = detailLayer) {

    private var width: Float = 0.0f
    private var height: Float = 0.0f
    private var depth: Float = 0.0f

    override val models: List<NSphere> by lazy {
        val models = ArrayList<NSphere>()
        width = NSphere.DEFAULT_WIDTH * 2 * columns - NSphere.DEFAULT_WIDTH
        height = NSphere.DEFAULT_HEIGHT * 2 * rows - NSphere.DEFAULT_HEIGHT
        depth = NSphere.DEFAULT_WIDTH * 2 * layersCount - NSphere.DEFAULT_WIDTH
        var x = centerX
        var y = centerY - height / 2
        var z = centerZ - width / 2
        for (currentLayer in 1..layersCount) {
            for (currentColumn in 1..columns) {
                for (currentRow in 1..rows) {
                    val sphere = NSphere(x = x, y = y, z = z)
                    z += NSphere.DEFAULT_WIDTH * 2
                    models.add(sphere)
                }
                z = centerZ - width / 2
                y += NSphere.DEFAULT_HEIGHT * 2
            }
            y = centerY - height / 2
            x += NSphere.DEFAULT_WIDTH * 2
        }
        models
    }

    override fun getModelInstances(): List<NetworkModelInstance> = models.map {
        it.toModelInstance()
    }

    override fun getWidth(): Float = width
    override fun getHeight(): Float = height
    override fun getDepth(): Float = depth
}
