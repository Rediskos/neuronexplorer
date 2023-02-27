package ru.kcode.feature.nlayers

import com.badlogic.gdx.graphics.g3d.ModelInstance
import ru.kcode.feature.nlayers.DimLayer
import ru.kcode.feature.nlayers.models.NSphere

class ThreeDimLayer(
    private val rows: Long,
    private val columns: Long,
    private val depth: Long,
    private val startX: Float,
    private val startY: Float,
    private val startZ: Float
) : DimLayer() {
    private val models: List<NSphere> by lazy {
        val models = ArrayList<NSphere>()
        var x = startX
        var y = startY
        var z = startZ
        for (d in 1..depth) {
            for (c in 1..columns) {
                for (r in 1..rows) {
                    val sphere = NSphere(x = x, y = y, z = z)
                    z += NSphere.DEFAULT_WIDTH * 2
                    models.add(sphere)
                }
                z = startZ
                y += NSphere.DEFAULT_HEIGHT * 2
            }
            y = startY
            x += NSphere.DEFAULT_WIDTH * 2
        }
        models
    }
    override fun getModelInstances(): List<ModelInstance> = models.map { it.toModelInstance() }
}
