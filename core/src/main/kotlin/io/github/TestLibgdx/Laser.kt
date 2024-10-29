package io.github.TestLibgdx

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion

class Laser(

    //position & dimension
    xPosition: Float,
    yPosition: Float,
    width: Float,
    height: Float,
    //laser physical characteristics
    movementSpeed: Float,
    //graphics
    laserTextureRegion: TextureRegion
) {
    //position & dimension
    private var mXPosition: Float = xPosition
    private var mYPosition: Float = yPosition
    private var mWidth: Float = width
    private var mHeight: Float = height

    //laser physical characteristics
    private var mMovementSpeed: Float = movementSpeed

    //graphic
    private var mLaserTextureRegion: TextureRegion = laserTextureRegion
    fun draw(batch: Batch) {
        batch.draw(
            mLaserTextureRegion,
            mXPosition - mWidth / 2,
            mYPosition - mHeight / 2,
            mWidth,
            mHeight
        )
    }
}
