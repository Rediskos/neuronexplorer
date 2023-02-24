package ru.kcode

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Circle


class App : ApplicationAdapter() {
    private var batch: SpriteBatch? = null
    private var image: Texture? = null
    private var circle: Circle? = null
    private var shape: ShapeRenderer? = null
    private var balls: List<Ball>? = null
    private var cam: PerspectiveCamera? = null
    private var instance: ModelInstance? = null
    var environment: Environment? = null
    var camController: CameraInputController? = null
    var modelBatch: ModelBatch? = null
    var model: Model? = null

    override fun create() {
        modelInit()
        batch = SpriteBatch()
        image = Texture("libgdx.png")
        circle = Circle(200.0F, 200.0F, 40.0F)
        balls = Ball.getRandomBalls(50)
        shape = ShapeRenderer()
        modelBatch = ModelBatch()
        environment = Environment()
        environment!!.set(ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f))
        environment!!.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f))

        cam = PerspectiveCamera(
            67f,
            Gdx.graphics.width.toFloat(),
            Gdx.graphics.height.toFloat()
        ).apply {
            position.set(10f, 10f, 10f)
            lookAt(0f, 0f, 0f)
            near = 1f
            far = 300f
            update()
        }
        camController = CameraInputController(cam);
        Gdx.input.inputProcessor = camController;
        val modelBuilder = ModelBuilder()
        model = modelBuilder.createBox(
            5f, 5f, 5f,
            Material(ColorAttribute.createDiffuse(Color.GREEN)),
            (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal).toLong()
        )
        instance = ModelInstance(model)
    }

    private fun modelInit() {
        TestModel.modelInit()
    }

    override fun render() {
        camController?.update()
        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT.or(GL20.GL_DEPTH_BUFFER_BIT))
        modelBatch?.begin(cam);
        modelBatch?.render(instance, environment);
        modelBatch?.end();
//        batch!!.begin()
//        batch!!.draw(image, 140f, 210f)
//        batch!!.end()
//        shape?.apply {
//            begin(ShapeRenderer.ShapeType.Filled)
//            balls?.forEach {
//                it.update()
//                it.draw(this)
//            }
//            end()
//        }
    }

    override fun dispose() {
        batch!!.dispose()
        image!!.dispose()
        shape!!.dispose()
        model!!.dispose()
        modelBatch!!.dispose()
    }
}
