package com.zentsugo.spacetreason.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.zentsugo.spacetreason.Listener;

public class Explosion {
	public static final float time = 0.125f;
	public static final int SIZE = 64;
	public static final int IMAGE_SIZE = 32;
	
	private static Animation<TextureRegion> anim = null;
	private float x, y;
	private float statetime;
	
	private boolean alive;
	
	public Explosion(float x, float y, float w, float h) {
		this.x = x - w / 2;
		this.y = y - h / 2;
		statetime = 0;
		alive = true;
		if (anim == null)
			anim = new Animation<TextureRegion>(time, TextureRegion.split(Listener.explosion_texture, IMAGE_SIZE, IMAGE_SIZE)[0]);
	}
	
	public void update(float deltatime) {
		statetime += deltatime;
		if (anim.isAnimationFinished(statetime))
			alive = false;
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public void render(SpriteBatch batch) {
		batch.draw(anim.getKeyFrame(statetime), x, y, SIZE, SIZE);
	}
	
	public void dispose() {
		Listener.explosion_texture.dispose();
	}
}
