package io.github.TestLibgdx

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import kotlin.math.PI

class EnemyShip(
    xCenter: Float,
    yCenter: Float,
    width: Float,
    height: Float,
    movementSpeed: Float,
    shield: Int,
    shipTextureRegion: TextureRegion,
    shieldTextureRegion: TextureRegion,
    laserTextureRegion: TextureRegion,
    laserWidth: Float,
    laserHeight: Float,
    laserMovementSpeed: Float,
    timeBetweenShots: Float
) : Ship(
    xCenter,
    yCenter,
    width,
    height,
    movementSpeed,
    shield,
    shipTextureRegion,
    shieldTextureRegion,
    laserTextureRegion,
    laserWidth,
    laserHeight,
    laserMovementSpeed,
    timeBetweenShots
) {
    private var mDirectionVector: Vector2 = Vector2(0f, -1f)
    fun getMDirectionVector(): Vector2 {
        return mDirectionVector
    }

    var timeSinceLastDirectionChange = 0f
    var directionChangeFrequency = 0.75f

    fun randomizeDirectionVector() {
        val bearing: Double = Main.random.nextDouble() * 2 * PI
        mDirectionVector.x = Math.sin(bearing).toFloat()
        mDirectionVector.y = Math.cos(bearing).toFloat()
    }

    override fun update(delta: Float) {
        super.update(delta)
        timeSinceLastDirectionChange += delta
        if (timeSinceLastDirectionChange > directionChangeFrequency) {
            randomizeDirectionVector()
            timeSinceLastDirectionChange -= directionChangeFrequency
        }
    }

    override fun fireLasers(): Array<Laser?> {
        val laser: Array<Laser?> = Array(2) { null }
        laser[0] = Laser(
            mBoundingBox.x + mBoundingBox.width * 0.18f,
            mBoundingBox.y - mLaserHeight,
            mLaserWidth,
            mLaserHeight,
            mLaserMovementSpeed,
            mLaserTextureRegion
        )
        laser[1] = Laser(
            mBoundingBox.x + mBoundingBox.width * 0.82f,
            mBoundingBox.y - mLaserHeight,
            mLaserWidth,
            mLaserHeight,
            mLaserMovementSpeed,
            mLaserTextureRegion
        )

        mTimeSinceLastShot = 0f
        return laser
    }
}
