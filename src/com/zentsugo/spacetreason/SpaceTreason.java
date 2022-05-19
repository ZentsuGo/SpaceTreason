package com.zentsugo.spacetreason;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.zentsugo.spacetreason.screens.MainMenuScreen;
import com.zentsugo.spacetreason.screens.PlayScreen;
import com.zentsugo.spacetreason.screens.SplashScreen;

public class SpaceTreason extends Game {
	public SpriteBatch batch;
	public OrthographicCamera camera;

	@Override
	public void create () {
		batch = new SpriteBatch();
		Listener.loadAssets(); //load all assets for once
		camera = new OrthographicCamera(400, 400 * Gdx.graphics.getHeight()/Gdx.graphics.getWidth());
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		batch.dispose();
		System.out.println("Disposed.");
		super.dispose();
	}
}
