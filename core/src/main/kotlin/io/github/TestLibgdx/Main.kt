package io.github.TestLibgdx

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms. */
class Main : Game() {
    private lateinit var gameScreen: GameScreen
    override fun create() {
        gameScreen = GameScreen()
        setScreen(gameScreen)
    }

    override fun resize(width: Int, height: Int) {
        gameScreen.resize(width, height)
    }

    override fun dispose() {
        super.dispose()
        gameScreen.dispose()
    }

}
