package com.zentsugo.spacetreason.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.zentsugo.spacetreason.Utils;

public class IntelligentBullet extends Entity implements Poolable {
	private float move_speed = 400f;
	private boolean follow = false;
	private boolean once = false;
	private Entity e = null;
	private Vector2 vector2 = null;
	private float cooldown = 4f;
	private float damage = 1f;
	
	public IntelligentBullet(Sprite sprite) {
		if (sprite == null)
			set(new Sprite(new Texture("images/bullet.png")));
		else
			set(sprite);
		setAlive(false);
		setOrigin(getWidth() / 2, getHeight() / 2);
	}
	
	public void init(float x, float y) {
		setPosition(x, y);
		setAlive(true);
	}
	
	@Override
	public void reset() {
		setPosition(0, 0);
		cooldown = 4f;
		follow = false;
		e = null;
		once = false;
		vector2 = null;
		angle = 0f;
		setAlive(false);
	}
	
	public void follow(Entity e) {
		follow = true;
		this.e = e;
	}
	
	public void followOnce(boolean flag) {
		once = flag;
	}
	
	public boolean hasEnemy() {
		return e!=null;
	}
	
	public float getDamage() {
		return damage;
	}
	
	public void setDamage(float damage) {
		this.damage = damage;
	}
	
	private float angle = 0;
	
	@Override
	public void rotate(float angle) {
		super.rotate(angle);
		this.angle += angle;
	}
	
	@Override
	public void setRotation(float angle) {
		super.setRotation(angle);
		this.angle = angle;
	}
	
	@Override
	public void update(float delta) {
		//rotation
		cooldown -= delta;
		if (cooldown <= 0)
			setAlive(false);
		if (Utils.isOutOfScreen(getX(), getY())) setAlive(false);
		if (e != null && !e.isAlive()) {
			follow = false;
		}
		if (follow) {
			Vector2 vector1 = getPosition(); //position
			if (once) {
				if (vector2 == null) {
					vector2 = e.getPosition(); //target
				} else {
					follow = false;
					return;
				}
			} else {
				vector2 = e.getPosition();
			}
			
			//alternative to (down)
			angle = Utils.faceTo(vector1, vector2);
			
			Vector2 direction = Utils.moveTo(vector1, angle);
			setPosition(getX() + direction.x * move_speed * delta, getY() + direction.y * move_speed * delta);
			
			/*float angle = (float) Math.toDegrees(Math.atan2(vector2.y - vector1.y, vector2.x - vector1.x));
			setRotation(angle);
			
			//move in the facing direction
			float moveX = (float) (Math.cos(angle) * move_speed * delta);
			float moveY = (float) (Math.sin(angle) * move_speed * delta);
			*/
		} else {
			if (angle == 0) {
				setY(getY() + (move_speed * delta));
			} else {
				Vector2 direction = Utils.moveTo(getPosition(), angle);
				setPosition(getX() + direction.x * move_speed * delta, getY() + direction.y * move_speed * delta);
			}
		}
	}
	
	@Override
	public void render(SpriteBatch batch) {
		draw(batch);
	}
	
}
