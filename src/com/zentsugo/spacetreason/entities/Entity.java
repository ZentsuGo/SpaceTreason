package com.zentsugo.spacetreason.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Entity extends Sprite {
	private boolean alive;
	
	public Vector2 getPosition() {
		return new Vector2(getX(), getY());
	}
	
	public void update(float delta) {
	}
	
	public void render(SpriteBatch batch) {
	}
	
	public void setAlive(boolean flag) {
		alive = flag;
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public void dispose() {
		if (getTexture() != null)
			getTexture().dispose();
	}
}
