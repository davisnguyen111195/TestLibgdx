package io.github.TestLibgdx

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion

class Ship(


    //position & dimension
    xCenter: Float,
    yCenter: Float,
    width: Float,
    height: Float,
    //ship characteristics
    movementSpeed: Float,
    shield: Int,
    //graphics
    shipTexture: TextureRegion,
    shieldTexture: TextureRegion
) {
    //ship characteristics
    private val mMovementSpeed: Float = movementSpeed
    private val mShield: Int = shield

    //position & dimension
    private val mXPosition: Float = xCenter - width / 2
    private val mYPosition: Float = yCenter - height / 2
    private val mWidth: Float = width
    private val mHeight: Float = height

    //graphics
    private val mShipTexture: TextureRegion = shipTexture
    private val msShieldTexture: TextureRegion = shieldTexture


    fun draw(batch: Batch) {
        batch.draw(mShipTexture, mXPosition, mYPosition, mWidth, mHeight)
        if (mShield > 0) {
            batch.draw(msShieldTexture, mXPosition, mYPosition, mWidth, mHeight)
        }
    }
}
