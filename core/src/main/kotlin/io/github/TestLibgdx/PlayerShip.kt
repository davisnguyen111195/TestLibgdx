package io.github.TestLibgdx

import com.badlogic.gdx.graphics.g2d.TextureRegion

class PlayerShip(
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


    override fun fireLasers(): Array<Laser?> {
        val laser: Array<Laser?> = Array(2) { null }
        laser[0] = Laser(
            mBoundingBox.x + mBoundingBox.width * 0.07f,
            mBoundingBox.y + mBoundingBox.height * 0.45f,
            mLaserWidth,
            mLaserHeight,
            mLaserMovementSpeed,
            mLaserTextureRegion
        )
        laser[1] = Laser(
            mBoundingBox.x + mBoundingBox.width * 0.93f,
            mBoundingBox.y + mBoundingBox.height * 0.45f,
            mLaserWidth,
            mLaserHeight,
            mLaserMovementSpeed,
            mLaserTextureRegion
        )

        mTimeSinceLastShot = 0f
        return laser
    }
}
