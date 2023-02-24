package ru.kcode

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import kotlin.random.Random


class Ball(
    private var x: Float,
    private var y: Float,
    private var radius: Float,
    private var velocityX: Float,
    private var velocityY: Float
) {
    fun update() {
        x += velocityX
        y += velocityY
        if(x < 0 || x > Gdx.graphics.width) velocityX = -velocityX
        if(y < 0 || y > Gdx.graphics.height) velocityY = -velocityY
    }

    fun draw(shapeRenderer: ShapeRenderer) {
        shapeRenderer.circle(x, y, radius)
    }

    companion object {
        fun getRandomBalls(amount: Int): List<Ball> {
            val balls = arrayListOf<Ball>()
            for (i in 0..amount) {
                balls.add(Ball(
                    x = Random.nextFloat() * Gdx.graphics.width,
                    y = Random.nextFloat() * Gdx.graphics.height,
                    radius = Random.nextFloat() * 50,
                    velocityX = Random.nextFloat() * 15,
                    velocityY = Random.nextFloat() * 15,
                ))
            }
            return balls
        }
    }
}
