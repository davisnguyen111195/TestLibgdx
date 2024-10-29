package io.github.TestLibgdx

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion

abstract class Ship(


    //position & dimension
    xCenter: Float,
    yCenter: Float,
    width: Float,
    height: Float,
    //ship characteristics
    movementSpeed: Float,
    shield: Int,
    //graphics
    shipTextureRegion: TextureRegion,
    shieldTextureRegion: TextureRegion,
    laserTextureRegion: TextureRegion,

    // laser information
    laserWidth: Float,
    laserHeight: Float,
    laserMovementSpeed: Float,
    timeBetweenShots: Float

) {

    //ship characteristics
    val mMovementSpeed: Float = movementSpeed
    val mShield: Int = shield

    //position & dimension
    val mXPosition: Float = xCenter - width / 2
    val mYPosition: Float = yCenter - height / 2
    val mWidth: Float = width
    val mHeight: Float = height

    //graphics
    val mShipTexture: TextureRegion = shipTextureRegion
    val msShieldTexture: TextureRegion = shieldTextureRegion
    val mLaserTextureRegion: TextureRegion = laserTextureRegion
    var mLaserMovementSpeed: Float = laserMovementSpeed
    var mTimeSinceLastShot: Float = 0f
    var mTimeBetweenShots: Float = timeBetweenShots

    //laser
    val mLaserWidth: Float = laserWidth
    val mLaserHeight: Float = laserHeight

    fun update(delta: Float) {
        mTimeSinceLastShot += delta
    }

    fun canFireLaser(): Boolean {
        return mTimeSinceLastShot - mTimeBetweenShots >= 0
    }

    abstract fun fireLasers(): Array<Laser?>

    fun draw(batch: Batch) {
        batch.draw(mShipTexture, mXPosition, mYPosition, mWidth, mHeight)
        if (mShield > 0) {
            batch.draw(msShieldTexture, mXPosition, mYPosition, mWidth, mHeight)
        }
    }
}
