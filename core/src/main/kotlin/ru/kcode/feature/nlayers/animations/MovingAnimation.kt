package ru.kcode.feature.nlayers.animations

import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector3


class MovingAnimation(
    private val modelInstance: ModelInstance,
    private val start: Vector3,
    private val end: Vector3,
    var duration: Float = BASE_MOVING_DURATION
): Animation {

    private var currentTime = 0f
    private var currentPosition: Vector3 = Vector3()
    private var isActive = false;

    override fun start() {
        isActive = true
        currentTime = 0f
        currentPosition = Vector3(start)
        modelInstance.transform.setToTranslation(currentPosition)
    }

    override fun step(delta: Float): Boolean {
        currentTime += delta
        if (currentTime > duration || !isActive) {
            end()
            return false
        }
        var alpha: Float = currentTime / duration  // Calculate the interpolation alpha

        alpha = MathUtils.clamp(alpha, 0f, 1f) // Clamp the alpha between 0 and 1

        currentPosition.set(start).lerp(end, Interpolation.linear.apply(alpha));

        modelInstance.transform.setToTranslation(currentPosition)
        println("$modelInstance start: $start, end: $end, current:$currentPosition")
        return true
    }

    override fun end() {
        isActive = false
    }

    override fun isActive(): Boolean = isActive

    companion object {
        const val BASE_MOVING_DURATION = 20f
    }
}
