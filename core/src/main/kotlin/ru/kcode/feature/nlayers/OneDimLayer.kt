package ru.kcode.feature.nlayers

import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import ru.kcode.feature.nlayers.models.NSphere

class OneDimLayer(
    private val count: Long,
    private var x: Float,
    private var y: Float,
    private val z: Float
) : DimLayer() {
    private val models: List<NSphere> by lazy {
        val models = ArrayList<NSphere>()
        for (i in 1..count) {
            val sphere = NSphere(x = x, y = y, z = z)
            y += NSphere.DEFAULT_HEIGHT * 2
            models.add(sphere)
        }
        models
    }

    override fun getModelInstances(): List<ModelInstance> = models.map { it.toModelInstance() }
}
