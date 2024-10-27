package io.github.TestLibgdx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.badlogic.gdx.utils.viewport.Viewport

class GameScreen : Screen {
    //screen
    private lateinit var camera: OrthographicCamera
    private lateinit var viewPort: StretchViewport


    //graphics
    private var batch: SpriteBatch

    private lateinit var background: Texture

    private var textureAtlas: TextureAtlas
    private var backgrounds : Array<TextureRegion?>


    private lateinit var playerShipTextureRegion: TextureRegion
    private lateinit var playerShieldTextureRegion: TextureRegion
    private lateinit var enemyShipTextureRegion: TextureRegion
    private lateinit var enemyShieldTextureRegion: TextureRegion
    private lateinit var playerLaserTextureRegion: TextureRegion
    private lateinit var enemyLaserTextureRegion: TextureRegion

    //timming
    private var backgroundOffset = 0
    private val backgroundOffsets = Array(4) { 0F }
    private var backgroundMaxScrollingSpeed = 0f

    //world
    private val WORLD_WIDTH = 72F
    private val WORLD_HEIGHT = 128F

    //game object
    private var playerShip
    private var enemyShip

    init {
        camera = OrthographicCamera()
        viewPort = StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera)

        //set up the texture atlas
        textureAtlas = TextureAtlas("images.atlas")
        backgrounds = Array(4) {null}

        backgrounds[0] = textureAtlas.findRegion("Starscape00")
        backgrounds[1] = textureAtlas.findRegion("Starscape01")
        backgrounds[2] = textureAtlas.findRegion("Starscape02")
        backgrounds[3] = textureAtlas.findRegion("Starscape03")


        backgroundMaxScrollingSpeed = WORLD_HEIGHT / 4


        //setup game object
        playerShip = Ship()


        //background = Texture("darkPurpleStarscape.png")
        //backgroundOffset = 0
        batch = SpriteBatch()


    }

    override fun dispose() {

    }

    override fun show() {

    }

    override fun render(delta: Float) {
        batch.begin()

        //scrolling background
        renderBackground(delta)

        batch.end()

    }

    private fun renderBackground(delta: Float) {
        backgroundOffsets[0] += delta * backgroundMaxScrollingSpeed / 8
        backgroundOffsets[1] += delta * backgroundMaxScrollingSpeed / 4
        backgroundOffsets[2] += delta * backgroundMaxScrollingSpeed / 2
        backgroundOffsets[3] += delta * backgroundMaxScrollingSpeed
        for (i in backgrounds.indices) {
            if (backgroundOffsets[i] > WORLD_WIDTH) {
                backgroundOffsets[i] = 0f
            }
            batch.draw(
                backgrounds[i],
                backgroundOffsets[i],
                0f,
                WORLD_WIDTH,
                WORLD_HEIGHT
            )
            batch.draw(
                backgrounds[i],
                backgroundOffsets[i] - WORLD_WIDTH,
                0f,
                WORLD_WIDTH,
                WORLD_HEIGHT
            )
        }
    }

    override fun resize(width: Int, height: Int) {
        viewPort.update(width, height, true)
        batch.projectionMatrix = camera.combined
    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun hide() {

    }
}
