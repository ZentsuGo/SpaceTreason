package com.zentsugo.spacetreason.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.zentsugo.spacetreason.Utils;

public class Bullet extends Entity implements Poolable {
	private float move_speed = 600f;
	private float damage = 1f;
	private boolean diry;
	private float angle = 0f;
	
	public Bullet(Sprite sprite) {
		if (sprite == null)
			set(new Sprite(new Texture("images/bullet.png")));
		else
			set(sprite);
		setAlive(false);
		setOrigin(getWidth() / 2, getHeight() / 2);
		diry = true;
	}
	
	public void init(float x, float y) {
		setPosition(x, y);
		setAlive(true);
	}
	
	@Override
	public void setRotation(float degrees) {
		super.setRotation(degrees);
	}
	
	@Override
	public float getRotation() {
		return super.getRotation();
	}
	
	public float getDamage() {
		return damage;
	}
	
	public void setDamage(float damage) {
		this.damage = damage; 
	}
	
	public Bullet setDirY(boolean flag) {
		diry = flag;
		return this;
	}
	
	/** By default 600f */
	public Bullet setSpeed(float flag) {
		this.move_speed = flag;
		return this;
	}
	
	@Override
	public void reset() {
		setPosition(0, 0);
		setAlive(false);     
	}
	
	public void update(float delta) {
		if (Utils.isOutOfScreen(getX(), getY())) setAlive(false);
		if (getRotation() != 0) {
			Vector2 direction = Utils.moveTo(getPosition(), getRotation()/100f + 156.2f/100f);
			setX(getX() + direction.x * move_speed * delta);
			setY(getY() + direction.y * move_speed * delta);
		} else {
			setY(getY() + (move_speed * delta));
		}
	}
	
	public void render(SpriteBatch batch) {
		draw(batch);
	}
	
	/*public Bullet follow(Entity entity) {
		//entity.getBoundingRectangle().
		float dirX = entity.getX() - getX();
		float dirY = entity.getY() - getY();
		
		if (Utils.isOutOfScreen(getX(), getY())) {
			setAlive(false);
		}
		setY(dirY);
		setX(dirX);
		return this;
	}*/
}
