package com.zentsugo.spacetreason.entities.enemies;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.zentsugo.spacetreason.Listener;
import com.zentsugo.spacetreason.SpaceTreason;
import com.zentsugo.spacetreason.Utils;
import com.zentsugo.spacetreason.entities.IntelligentBullet;
import com.zentsugo.spacetreason.entities.Entity;
import com.zentsugo.spacetreason.entities.IntelligentBullet;
import com.zentsugo.spacetreason.screens.PlayScreen;

public class Enemy extends Entity implements Poolable {
	
	private SpaceTreason game;
	private PlayScreen lvl;
	private SpriteBatch batch;
	private ArrayList<IntelligentBullet> activeiBullets;
	private Pool<IntelligentBullet> pooliBullets;
	private float move_speed = 250f;
	private float dx, dy;
	private final int base_life_points = 3;
	private int life_points = 3;
	//private AutoShooter autoshooter;
	private float shootDelay = 2f;
	private float cooldown = 0;
	private boolean shoot;
	private boolean dirx;
	private boolean diry;
	private boolean dirx_right;
	private boolean diry_up;
	private float bullet_damage = 1;
	//public boolean shoot = false;
	
	public Enemy(SpaceTreason game, PlayScreen lvl) {
		this.game = game;
		this.lvl = lvl;
		batch = game.batch;
		shoot = false;
		diry = true;
		dirx = false;
		set(new Sprite(Listener.enemy_texture));
		setOrigin(getWidth() / 2, getHeight() / 2);
		activeiBullets = new ArrayList<IntelligentBullet>();
		pooliBullets = new Pool<IntelligentBullet>() {
			@Override
			public IntelligentBullet newObject() {
				return new IntelligentBullet(new Sprite(new Texture("images/enemy_bullet.png")));//.follow(PlayScreen1.player)
			}
		};
		//autoshooter = new AutoShooter();
	}
	
	public Vector2 getPosition() {
		return new Vector2(getX(), getY());
	}
	
	public void init(float x, float y) {
		setPosition(x, y);
		setAlive(true);
 	}
	
	public void update(float delta) {
		if (isAlive()) {
			//obligation to move
			if (dirx == false && diry == false) diry = true;
			else if (dirx == true && diry == true) {
				diry = false;
				dirx = true;
			}
			
			/*if (rotation) {
				//rotation
				Vector2 vector1 = PlayScreen1.getPlayer().getPosition();
				Vector2 vector2 = getPosition();
				float angle = (float) Math.toDegrees(Math.atan2(vector1.x - vector2.x, vector2.y - vector1.y));
				setRotation(angle);
			}*/
			
			//life check
			lifeCheck();
			
			dir(delta);
			setX(getX() + dx);
			setY(getY() + dy);
			
			if (Utils.isOutOfScreen(getX(), getY())) {
				setAlive(false);
			}
			
			//shoot each cooldown-shootDelay<=0
			if (shoot) {
				cooldown -= delta;
				if (cooldown <= 0) {
					cooldown = shootDelay;
					shoot();
				}
			}
			
			if (shoot) {
				//bullets
				IntelligentBullet item;
				
				for (int i = activeiBullets.size(); --i >= 0;) {
					item = activeiBullets.get(i);
					if (!item.isAlive()) {
						activeiBullets.remove(item);
						pooliBullets.free(item);
					}
					//item.follow(PlayScreen1.player);
					item.update(delta);
				}
			}
		}
	}
	
	public void render(float delta) {
		batch.begin();
			draw(batch);
			if (!activeiBullets.isEmpty()) {
				for (int i = 0; i < activeiBullets.size(); i++) {
					IntelligentBullet item = activeiBullets.get(i);
					item.render(batch);
				}
			}
		batch.end();
	}
	
	public void setDirx(boolean dirx, boolean dirx_right) {
		this.dirx = dirx;
		this.dirx_right = dirx_right;
		if (dirx_right)
			setRotation(90f);
		else
			setRotation(-90f);
	}
	
	public void setDiry(boolean diry, boolean diry_up) {
		this.diry = diry;
		this.diry_up = diry_up;
		if (diry_up)
			setRotation(-180f);
		else
			setRotation(0f);
	}
	
	private void dir(float delta) {
		dx = 0;
		dy = 0;
		if (dirx) {
			if (dirx_right)
				dx = move_speed * delta;
			else
				dx = -move_speed * delta;
		}
		
		if (diry) {
			if (diry_up)
				dy = move_speed * delta;
			else
				dy = -move_speed * delta;
		}
	}

	public void hit(float life_points) {
		this.life_points-=life_points;
	}
	
	public int getLife() {
		return life_points;
	}
	
	public void lifeCheck() {
		if (life_points <= 0) {
			setAlive(false);
		}
	}
	
	public ArrayList<IntelligentBullet> getBullets() {
		return activeiBullets;
	}
	
	/**By default 2, the number of hit*/
	public void setLife(int flag) {
		life_points = flag;
	}
	
	public void setSpeed(float flag) {
		this.move_speed = flag;
	}
	
	/**By default 2f*/
	public void setShootSpeed(float flag) {
		this.shootDelay = flag;
	}
	
	/**By default false*/
	public void setShoot(boolean flag) {
		shoot = flag;
	}
	
	private void shoot() {
		IntelligentBullet item = pooliBullets.obtain();
		item.init(getX() + (getWidth() / 2), getY() - getHeight());
		item.setDamage(bullet_damage);
		item.follow(lvl.getPlayer());
		item.followOnce(true);
		activeiBullets.add(item);
	}
	
	/*** By default equals to 1 ***/
	public void setBulletDamage(float bullet_damage) {
		this.bullet_damage = bullet_damage;
	}

	@Override
	public void reset() {
		setPosition(0, 0);
		this.life_points = base_life_points;
		setDirx(false, true);
		setDiry(true, false);
		setBulletDamage(1f);
		shoot = false;
		setRotation(0f);
		for (int i = 0; i < activeiBullets.size(); i++) {
			IntelligentBullet item = activeiBullets.get(i);
			item.dispose();
		}
		activeiBullets.clear();
		pooliBullets.clear();
		setAlive(false);
		//shoot = false;
	}
	
	public void dispose() {
		Listener.enemy_texture.dispose();
		if (!activeiBullets.isEmpty()) {
			for (int i = 0; i < activeiBullets.size(); i++) {
				IntelligentBullet item = activeiBullets.get(i);
				item.dispose();
			}
		}
	}
	
	/*class AutoShooter {
		public Timer timer;
		public boolean running;
		
		public AutoShooter() {
			running = false;
		}
		
		public void init() {
			running = true;
			timer = new Timer();
			timer.scheduleAtFixedRate(new ShootTask(), 0, 1500);
		}
		
		public boolean isRunning() {
			return running;
		}
		
		class ShootTask extends TimerTask {
			@Override
			public void run() {
				if (isAlive())
					shoot = true;
				else {
					running = false;
					cancel();
				}
			}
		}
	}*/
}
