package ru.kcode

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector3
import ru.kcode.feature.nlayers.NLayerRenderController
import ru.kcode.utils.NetworkModelInstance


class App : ApplicationAdapter() {

    private var shape: ShapeRenderer? = null
    private var cam: PerspectiveCamera? = null
    private var instance: ModelInstance? = null
    private var tcount: Int = 0
    var environment: Environment? = null
    var camController: CameraInputController? = null
    var modelBatch: ModelBatch? = null
    var model: Model? = null
    var layerController: NLayerRenderController? = null

    override fun create() {
        modelInit()
        modelBatch = ModelBatch()
        shape = ShapeRenderer()
        environment = Environment()
        environment!!.set(ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f))
        environment!!.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f))

        cam = PerspectiveCamera(
            67f,
            Gdx.graphics.width.toFloat(),
            Gdx.graphics.height.toFloat()
        ).apply {
            position.set(10f, 10f, 100f)
            lookAt(0f, 0f, 0f)
            near = 1f
            far = 300f
            update()
        }
        camController = CameraInputController(cam).apply { translateUnits = 10f };
        Gdx.input.inputProcessor = camController;
    }

    private fun modelInit() {
        MLPMnistSingleLayerExample.main(arrayOf())
        MLPMnistSingleLayerExample.model
        layerController = NLayerRenderController(MLPMnistSingleLayerExample.model)
    }

    override fun render() {
        camController?.update()
        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT.or(GL20.GL_DEPTH_BUFFER_BIT))
        layerController?.let { layerController->

            shape?.projectionMatrix = cam?.combined

            layerController.getConnections().flatten().forEach {
                shape?.let { shape -> it.draw(shape) }
            }

            modelBatch?.begin(cam);
            layerController.getLayersInstances().forEach { modelInstances ->
                modelInstances.forEach { modelInstance ->
                    if (isVisible(cam, modelInstance)) {
                        modelBatch?.render(modelInstance, environment)
                    }
                }
//
//
//                layerController.getConnections().flatten().forEach { connection ->
//                    connection.animate(delta = Gdx.graphics.deltaTime)
//                    modelBatch?.render(connection.getMoverInstance())
//                }

            }
            layerController.processAnimationStage(Gdx.graphics.deltaTime)
            layerController.getConnectionsForAnimation()?.forEach {
                modelBatch?.render(it)
            }

            layerController.getConnectionsForBackProb()?.forEach {
                modelBatch?.render(it)
            }
            layerController.getActivationInstances()?.forEach {
                modelBatch?.render(it)
            }

            layerController.getBackProbActivationInstances()?.forEach {
                modelBatch?.render(it)
            }
            modelBatch?.end();
        }

    }

    private val position = Vector3()
    private fun isVisible(camera: PerspectiveCamera?, instance: NetworkModelInstance): Boolean {
        return true
        instance.transform.getTranslation(position);
        position.add(instance.center)
        return cam?.frustum?.pointInFrustum(position) ?: false;
    }

    override fun dispose() {
        shape?.dispose()
        model?.dispose()
        layerController?.dispose()
        modelBatch?.dispose()
    }
}
