package ru.kcode

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import kotlinx.coroutines.*
import ru.kcode.feature.nlayers.NLayerRenderController


class App : ApplicationAdapter() {

    private var shape: ShapeRenderer? = null
    private var cam: PerspectiveCamera? = null
    private var instance: ModelInstance? = null
    var environment: Environment? = null
    var camController: CameraInputController? = null
    var modelBatch: ModelBatch? = null
    var model: Model? = null
    var layerController: NLayerRenderController? = null

    override fun create() {
        modelInit()
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
        model = modelBuilder.createSphere(
            5f, 5f, 5f, 5, 5,
            Material(ColorAttribute.createDiffuse(Color.GREEN)),
            (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal).toLong()
        )
        instance = ModelInstance(model, 0f, 0f, 0f)
    }

    private fun modelInit() {
        TestModel.modelInit()
        layerController = NLayerRenderController(TestModel.model.layers)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun render() {
        runBlocking {
            camController?.update()
            Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT.or(GL20.GL_DEPTH_BUFFER_BIT))
            modelBatch?.begin(cam);
            modelBatch?.render(instance, environment);

            layerController?.let {
                it.getLayersInstances().forEach { modelInstance ->
                    modelBatch?.render(modelInstance, environment)
                }

            }
            modelBatch?.end();
        }
    }

    override fun dispose() {
        shape?.dispose()
        model?.dispose()
        layerController?.dispose()
        modelBatch?.dispose()
    }
}
