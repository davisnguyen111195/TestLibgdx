package io.github.TestLibgdx

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle

class Explosion(
    texture: Texture,
    boundingBox: Rectangle,
    totalAnimationTime: Float
) {
    private val explosionAnimation: Animation<TextureRegion>
    private val boundingBox: Rectangle = boundingBox
    private var explosionTimer: Float = totalAnimationTime

    init {
        val textureRegion2D: Array<Array<TextureRegion>> = TextureRegion.split(
            /* texture = */
            texture,
            /* tileWidth = */
            64,
            /* tileHeight = */
            64
        )

        val textureRegion1D = Array(16) {
            textureRegion2D[it / 4][it % 4]
        }


        explosionAnimation = Animation<TextureRegion>(
            totalAnimationTime / 16,
            *textureRegion1D
        )
        explosionTimer = 0F
    }


    fun update(delta: Float) {
        explosionTimer += delta
    }

    fun draw(batch: SpriteBatch) {
        batch.draw(
            explosionAnimation?.getKeyFrame(explosionTimer),
            boundingBox.x,
            boundingBox.y,
            boundingBox.width,
            boundingBox.height
        )
    }

    fun isFinished(): Boolean? {
        return explosionAnimation?.isAnimationFinished(explosionTimer)
    }
}
