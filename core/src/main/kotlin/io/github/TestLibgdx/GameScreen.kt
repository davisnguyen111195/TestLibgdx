package io.github.TestLibgdx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.badlogic.gdx.utils.viewport.Viewport
import java.util.LinkedList

class GameScreen : Screen {
    //screen
    private var camera: OrthographicCamera
    private var viewPort: FitViewport


    //graphics
    private var batch: SpriteBatch

    private lateinit var background: Texture

    private var textureAtlas: TextureAtlas
    private var backgrounds: Array<TextureRegion?>


    private var playerShipTextureRegion: TextureRegion
    private var playerShieldTextureRegion: TextureRegion
    private var enemyShipTextureRegion: TextureRegion
    private var enemyShieldTextureRegion: TextureRegion
    private var playerLaserTextureRegion: TextureRegion
    private var enemyLaserTextureRegion: TextureRegion

    //timming
    private var backgroundOffset = 0
    private val backgroundOffsets = Array(4) { 0F }
    private var backgroundMaxScrollingSpeed = 0f

    //world
    private val WORLD_WIDTH = 72F
    private val WORLD_HEIGHT = 128F

    //game object
    private var playerShip: Ship
    private var enemyShip: Ship
    private var playerLaserList: LinkedList<Laser>
    private var enemyLaserList: LinkedList<Laser>

    init {
        camera = OrthographicCamera()
        viewPort = FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera)

        //set up the texture atlas
        textureAtlas = TextureAtlas("images.atlas")
        backgrounds = Array(4) { null }

        backgrounds[0] = textureAtlas.findRegion("Starscape00")
        backgrounds[1] = textureAtlas.findRegion("Starscape01")
        backgrounds[2] = textureAtlas.findRegion("Starscape02")
        backgrounds[3] = textureAtlas.findRegion("Starscape03")

        playerShipTextureRegion = textureAtlas.findRegion("playerShip2_blue")
        playerShieldTextureRegion = textureAtlas.findRegion("shield1")

        enemyShipTextureRegion = textureAtlas.findRegion("enemyRed3")
        enemyShieldTextureRegion = textureAtlas.findRegion("shield2")
        backgroundMaxScrollingSpeed = WORLD_HEIGHT / 4
        playerLaserTextureRegion = textureAtlas.findRegion("laserBlue03")
        enemyLaserTextureRegion = textureAtlas.findRegion("laserRed03")

        //setup game object
        playerShip = PlayerShip(
            WORLD_WIDTH / 2, WORLD_HEIGHT / 4, 10f, 10f,
            2f, 3,
            playerShipTextureRegion,
            playerShieldTextureRegion,
            playerLaserTextureRegion,
            0.4f,
            4f,
            45f,
            0.5f
        )

        enemyShip = EnemyShip(
            WORLD_WIDTH / 2, WORLD_HEIGHT * 3 / 4, 10f, 10f,
            2f, 1,
            enemyShipTextureRegion,
            enemyShieldTextureRegion,
            enemyLaserTextureRegion,
            0.3f,
            5f,
            50f,
            0.8f
        )

        playerLaserList = LinkedList()
        enemyLaserList = LinkedList()

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

        playerShip.update(delta)
        enemyShip.update(delta)


        //scrolling background
        renderBackground(delta)

        //enemy ship
        enemyShip.draw(batch)
        //player ship
        playerShip.draw(batch)
        //lasers
        if(playerShip.canFireLaser()){
            val lasers = playerShip.fireLasers()
            for(laser in lasers){
                if (laser != null) {
                    playerLaserList.add(laser)
                }
            }
        }

        if(enemyShip.canFireLaser()){
            val lasers = enemyShip.fireLasers()
            for(laser in lasers){
                if (laser != null) {
                    enemyLaserList.add(laser)
                }
            }
        }

        //draw lasers
        var listIterator = playerLaserList.listIterator()
        while(listIterator.hasNext()) {
            val laser = listIterator.next()
            laser.draw(batch)
            laser.mYPosition += laser.mMovementSpeed * delta
            if(laser.mYPosition > WORLD_HEIGHT){
                listIterator.remove()
            }

        }

        listIterator = enemyLaserList.listIterator()
        while(listIterator.hasNext()) {
            val laser = listIterator.next()
            laser.draw(batch)
            laser.mYPosition -= laser.mMovementSpeed * delta
            if(laser.mYPosition + laser.mHeight < 0f){
                listIterator.remove()
            }

        }
        //explosions

        batch.end()

    }

    private fun renderBackground(delta: Float) {
        backgroundOffsets[0] += delta * backgroundMaxScrollingSpeed / 8
        backgroundOffsets[1] += delta * backgroundMaxScrollingSpeed / 4
        backgroundOffsets[2] += delta * backgroundMaxScrollingSpeed / 2
        backgroundOffsets[3] += delta * backgroundMaxScrollingSpeed
        for (i in backgrounds.indices) {
            if (backgroundOffsets[i] > WORLD_HEIGHT) {
                backgroundOffsets[i] = 0f
            }
            batch.draw(
                backgrounds[i],
                0f, -backgroundOffsets[i],
                WORLD_WIDTH,
                WORLD_HEIGHT

            )
            batch.draw(
                backgrounds[i],
                0f,
                -backgroundOffsets[i] + WORLD_HEIGHT,
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
