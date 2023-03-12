package ru.kcode.feature.nlayers.dimentions

import ru.kcode.feature.nlayers.DimLayer
import ru.kcode.feature.nlayers.models.NSphere
import ru.kcode.utils.NetworkModelInstance

class OneDimLayer(
    private val count: Long,
    private var centerX: Float,
    private var centerY: Float,
    private val centerZ: Float
) : DimLayer() {

    private var width: Float = 0.0f
    private var height: Float = 0.0f
    private var depth: Float = 0.0f

    override val models: List<NSphere> by lazy {
        val models = ArrayList<NSphere>()
        width = NSphere.DEFAULT_WIDTH
        height = NSphere.DEFAULT_HEIGHT * 2 * count - NSphere.DEFAULT_HEIGHT
        depth = NSphere.DEFAULT_WIDTH
        val x = centerX
        var y = centerY - height / 2
        val z = centerZ
        for (i in 1..count) {
            val sphere = NSphere(x = x, y = y, z = z)
            y += NSphere.DEFAULT_HEIGHT * 2
            models.add(sphere)
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
