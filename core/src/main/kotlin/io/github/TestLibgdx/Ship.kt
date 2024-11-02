package io.github.TestLibgdx

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle

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
    var mShield: Int = shield

    //position & dimension
//    var mXPosition: Float = xCenter - width / 2
//
//    var mYPosition: Float = yCenter - height / 2
//    val mWidth: Float = width
//    val mHeight: Float = height
    val mBoundingBox: Rectangle =
        Rectangle(xCenter - width / 2, yCenter - height / 2, width, height)


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
        //mBoundingBox.set(mXPosition, mYPosition, mWidth, mHeight)
        mTimeSinceLastShot += delta
    }

    fun canFireLaser(): Boolean {
        return mTimeSinceLastShot - mTimeBetweenShots >= 0
    }

    abstract fun fireLasers(): Array<Laser?>

    fun intersects(otherRectangle: Rectangle): Boolean {
        return mBoundingBox.overlaps(otherRectangle)
    }

    fun hit(laser: Laser) {
        if (mShield > 0) {
            mShield--
        }
    }

    fun draw(batch: Batch) {
        batch.draw(
            mShipTexture,
            mBoundingBox.x,
            mBoundingBox.y,
            mBoundingBox.width,
            mBoundingBox.height
        )
        if (mShield > 0) {
            batch.draw(
                msShieldTexture,
                mBoundingBox.x,
                mBoundingBox.y,
                mBoundingBox.width,
                mBoundingBox.height
            )
        }
    }

    fun translate(xChange: Float, yChange: Float) {
        mBoundingBox.setPosition(mBoundingBox.x + xChange, mBoundingBox.y + yChange)
    }

}
