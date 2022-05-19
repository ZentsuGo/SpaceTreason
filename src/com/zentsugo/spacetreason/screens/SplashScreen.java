package com.zentsugo.spacetreason.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.zentsugo.spacetreason.Listener;
import com.zentsugo.spacetreason.SpaceTreason;
import com.zentsugo.tween.SpriteAccessor;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

public class SplashScreen implements Screen {
	
	private SpaceTreason game;
	private SpriteBatch batch;
	private TweenManager tweenManager;
	
	public SplashScreen(SpaceTreason game) {
		this.game = game;
		batch = game.batch;
	}
	
	@Override
	public void show() {
		tweenManager = new TweenManager();
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		
		Tween.set(Listener.splashScreen, SpriteAccessor.ALPHA).target(0).start(tweenManager);
		Tween.to(Listener.splashScreen, SpriteAccessor.ALPHA, 3).target(1).repeatYoyo(1, 2).setCallback(new TweenCallback() {
			@Override
			public void onEvent(int arg0, BaseTween<?> arg1) {
				game.setScreen(new MainMenuScreen(game));
			}
		}).start(tweenManager);
	}

	@Override
	public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		tweenManager.update(delta);
		
		batch.begin();
			Listener.splashScreen.draw(batch);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
		Listener.texture_splashScreen.dispose();
	}
}
