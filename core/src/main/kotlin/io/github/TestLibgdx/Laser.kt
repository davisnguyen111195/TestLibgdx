package io.github.TestLibgdx

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle

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
    var mXPosition: Float = xPosition
        get() = field
        set(value) {
            field = value
        }
    var mYPosition: Float = yPosition
        get() = field
        set(value) {
            field = value
        }
    val mWidth: Float = width
    var mHeight: Float = height
        get() = field
        set(value) {
            field = value
        }

    //laser physical characteristics
    var mMovementSpeed: Float = movementSpeed
        get() = field
        set(value) {
            field = value
        }

    //graphic
    private var mLaserTextureRegion: TextureRegion = laserTextureRegion
    var mBoundingBox: Rectangle = Rectangle(
        mXPosition - mWidth / 2,
        mYPosition - mHeight / 2,
        mWidth,
        mHeight
    )
        get() = field

    fun draw(batch: Batch) {
        batch.draw(
            mLaserTextureRegion,
            mBoundingBox.x, mBoundingBox.y, mBoundingBox.width, mBoundingBox.height
        )
    }


}
