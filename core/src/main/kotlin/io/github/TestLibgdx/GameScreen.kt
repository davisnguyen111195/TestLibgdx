package io.github.TestLibgdx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.FitViewport
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
    private var explosionTexture: Texture

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
    private val timeBetweenEnemySpawns = 1f
    private var enemySpawnTimer = 0f

    //world
    private val WORLD_WIDTH = 72F
    private val WORLD_HEIGHT = 128F
    private val TOUCH_MOVEMENT_THRESHOLD = 0.5f

    //game object
    private var playerShip: PlayerShip
    private var enemyShips: LinkedList<EnemyShip>
    private var playerLaserList: LinkedList<Laser>
    private var enemyLaserList: LinkedList<Laser>

    private lateinit var explosionList: LinkedList<Explosion>

    init {
        camera = OrthographicCamera()
        viewPort = FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera)

        //set up the texture atlas
        textureAtlas = TextureAtlas("images.atlas")
        backgrounds = Array(4) { null }
        explosionTexture = Texture("explosion.png")
        backgrounds[0] = textureAtlas.findRegion("Starscape00")
        backgrounds[1] = textureAtlas.findRegion("Starscape01")
        backgrounds[2] = textureAtlas.findRegion("Starscape02")
        backgrounds[3] = textureAtlas.findRegion("Starscape03")

        playerShipTextureRegion = textureAtlas.findRegion("playerShip2_blue")
        playerShieldTextureRegion = textureAtlas.findRegion("shield1")

        enemyShipTextureRegion = textureAtlas.findRegion("enemyRed3")
        enemyShieldTextureRegion = textureAtlas.findRegion("shield2")
        enemyShieldTextureRegion.flip(false, true)
        backgroundMaxScrollingSpeed = WORLD_HEIGHT / 4
        playerLaserTextureRegion = textureAtlas.findRegion("laserBlue03")
        enemyLaserTextureRegion = textureAtlas.findRegion("laserRed03")

        //setup game object
        playerShip = PlayerShip(
            WORLD_WIDTH / 2, WORLD_HEIGHT / 4, 10f, 10f,
            40f, 6,
            playerShipTextureRegion,
            playerShieldTextureRegion,
            playerLaserTextureRegion,
            0.4f,
            4f,
            45f,
            0.5f
        )
        enemyShips = LinkedList()


        playerLaserList = LinkedList()
        enemyLaserList = LinkedList()


        explosionList = LinkedList()
        batch = SpriteBatch()


    }

    private fun spawnEnemyShips(delta: Float) {
        enemySpawnTimer += delta
        if (enemySpawnTimer > timeBetweenEnemySpawns) {
            enemyShips.add(
                EnemyShip(
                    WORLD_WIDTH / 2, WORLD_HEIGHT * 3 / 4, 10f, 10f,
                    40f, 2,
                    enemyShipTextureRegion,
                    enemyShieldTextureRegion,
                    enemyLaserTextureRegion,
                    0.3f,
                    5f,
                    50f,
                    0.8f
                )
            )
            enemySpawnTimer -= timeBetweenEnemySpawns
        }
    }

    override fun dispose() {

    }

    override fun show() {

    }

    override fun render(delta: Float) {
        batch.begin()

        //scrolling background
        renderBackground(delta)

        detectInput(delta)
        spawnEnemyShips(delta)
        playerShip.update(delta)
        val enemyListIterator: ListIterator<EnemyShip> = enemyShips.listIterator()
        while (enemyListIterator.hasNext()) {
            val enemyShip = enemyListIterator.next()

            moveEnemies(enemyShip, delta)
            enemyShip.update(delta)

            //enemy ship
            enemyShip.draw(batch)
        }


        //player ship
        playerShip.draw(batch)
        //lasers

        renderLaser(delta)

        // detect collisions between lasers and ships
        detectCollistions()
        //explosions
        renderExplosions(delta)


        batch.end()

    }

    private fun moveEnemies(enemyShip: EnemyShip, delta: Float) {
        val leftLimit = -enemyShip.mBoundingBox.x
        val downLimit = WORLD_HEIGHT / 2 - enemyShip.mBoundingBox.y
        val rightLimit =
            WORLD_WIDTH - enemyShip.mBoundingBox.x - enemyShip.mBoundingBox.width
        val upLimit =
            WORLD_HEIGHT - enemyShip.mBoundingBox.y - enemyShip.mBoundingBox.height
        var xMove = enemyShip.getMDirectionVector().x * enemyShip.mMovementSpeed * delta
        var yMove = enemyShip.getMDirectionVector().y * enemyShip.mMovementSpeed * delta

        if (xMove > 0) xMove = Math.min(xMove, rightLimit)
        else xMove = Math.max(xMove, leftLimit)

        if (yMove > 0) yMove = Math.min(yMove, upLimit)
        else yMove = Math.max(yMove, downLimit)
        enemyShip.translate(xMove, yMove)
    }

    private fun detectInput(delta: Float) {
        val leftLimit = -playerShip.mBoundingBox.x
        val downLimit = -playerShip.mBoundingBox.y
        val rightLimit = WORLD_WIDTH - playerShip.mBoundingBox.x - playerShip.mBoundingBox.width
        val upLimit =
            7 * WORLD_HEIGHT / 8 - playerShip.mBoundingBox.y - playerShip.mBoundingBox.height

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && rightLimit > 0) {
            playerShip.translate(Math.min(playerShip.mMovementSpeed * delta, rightLimit), 0f)
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) && upLimit > 0) {
            playerShip.translate(0f, Math.min(playerShip.mMovementSpeed * delta, upLimit))
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && leftLimit < 0) {
            playerShip.translate(Math.max(-playerShip.mMovementSpeed * delta, leftLimit), 0f)
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && downLimit < 0) {
            playerShip.translate(0f, Math.max(-playerShip.mMovementSpeed * delta, downLimit))
        }

        if (Gdx.input.isTouched) {
            val xTouchPixel: Float = Gdx.input.x.toFloat()
            val yTouchPixel: Float = Gdx.input.y.toFloat()

            var touchPoint = Vector2(xTouchPixel, yTouchPixel)
            touchPoint = viewPort.unproject(touchPoint)

            val playerShipCenter =
                Vector2(
                    playerShip.mBoundingBox.x + playerShip.mBoundingBox.width / 2,
                    playerShip.mBoundingBox.y + playerShip.mBoundingBox.height / 2
                )

            val touchDistance = touchPoint.dst(playerShipCenter)
            if (touchDistance > TOUCH_MOVEMENT_THRESHOLD) {
                val xTouchDifference = touchPoint.x - playerShipCenter.x
                val yTouchDifference = touchPoint.y - playerShipCenter.y
                var xMove = xTouchDifference / touchDistance * playerShip.mMovementSpeed * delta
                var yMove = yTouchDifference / touchDistance * playerShip.mMovementSpeed * delta

                if (xMove > 0) xMove = Math.min(xMove, rightLimit)
                else xMove = Math.max(xMove, leftLimit)

                if (yMove > 0) yMove = Math.min(yMove, upLimit)
                else yMove = Math.max(yMove, downLimit)
                playerShip.translate(xMove, yMove)
            }
        }
    }

    fun detectCollistions() {
        //for each player laser, check whether it intersects an enemy ship
        var laserListIterator = playerLaserList.listIterator()
        while (laserListIterator.hasNext()) {
            val laser = laserListIterator.next()
            val enemyShipListIterator = enemyShips.listIterator()
            while (enemyShipListIterator.hasNext()) {
                val enemyShip = enemyShipListIterator.next()
                if (enemyShip.intersects(laser.mBoundingBox)) {
                    //contact with enemy ship
                    if (enemyShip.hitAndCheckDestroyed(laser)) {
                        enemyShipListIterator.remove()
                        explosionList.add(
                            Explosion(
                                explosionTexture,
                                Rectangle(enemyShip.mBoundingBox),
                                0.7f,
                            )
                        )
                    }
                    laserListIterator.remove()
                    break
                }
            }

        }


        //for each enemy laser, check whether it intersects an player ship
        laserListIterator = enemyLaserList.listIterator()
        while (laserListIterator.hasNext()) {
            val laser = laserListIterator.next()
            if (playerShip.intersects(laser.mBoundingBox)) {
                //contact with enemy ship
                playerShip.hitAndCheckDestroyed(laser)
                laserListIterator.remove()
            }
        }
    }

    fun renderExplosions(delta: Float) {
        val explosionListIterator = explosionList.listIterator()
        while (explosionListIterator.hasNext()) {
            val explosion: Explosion = explosionListIterator.next()
            explosion.update(delta)
            if (explosion.isFinished() == true) {
                explosionListIterator.remove()
            } else {
                explosion.draw(batch)
            }
        }
    }

    fun renderLaser(delta: Float) {
        if (playerShip.canFireLaser()) {
            val lasers = playerShip.fireLasers()
            for (laser in lasers) {
                if (laser != null) {
                    playerLaserList.add(laser)
                }
            }
        }
        val enemyShipIterator = enemyShips.listIterator()
        while (enemyShipIterator.hasNext()) {
            val enemyShip = enemyShipIterator.next()
            if (enemyShip.canFireLaser()) {
                val lasers = enemyShip.fireLasers()
                for (laser in lasers) {
                    if (laser != null) {
                        enemyLaserList.add(laser)
                    }
                }
            }
        }


        //draw lasers
        var listIterator = playerLaserList.listIterator()
        while (listIterator.hasNext()) {
            val laser = listIterator.next()
            laser.draw(batch)
            laser.mBoundingBox.y += laser.mMovementSpeed * delta
            if (laser.mBoundingBox.y > WORLD_HEIGHT) {
                listIterator.remove()
            }

        }

        listIterator = enemyLaserList.listIterator()
        while (listIterator.hasNext()) {
            val laser = listIterator.next()
            laser.draw(batch)
            laser.mBoundingBox.y -= laser.mMovementSpeed * delta
            if (laser.mBoundingBox.y + laser.mBoundingBox.height < 0f) {
                listIterator.remove()
            }

        }
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
