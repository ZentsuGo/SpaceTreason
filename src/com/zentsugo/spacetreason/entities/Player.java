package com.zentsugo.spacetreason.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.zentsugo.spacetreason.Listener;
import com.zentsugo.spacetreason.SpaceTreason;
import com.zentsugo.spacetreason.screens.PlayScreen;

public class Player extends Entity {
	
	//enum for states
	public enum State {
		LEVEL_1,
		LEVEL_2,
		LEVEL_3,
		LEVEL_4,
		LEVEL_5
	}
	
	//level
	private State level;
	
	//game
	private SpaceTreason game;
	private SpriteBatch batch;
	private ArrayList<Bullet> activeBullets;
	private ArrayList<IntelligentBullet> activeiBullets;
	private Pool<Bullet> poolBullets;
	private Pool<IntelligentBullet> pooliBullets;
	private final float acc_speed = 400f;
	private final float base_speed = 300f;
	private float move_speed = base_speed;
	private float dx, dy;
	private float life_points = 5;
	private Texture life_state;
	private boolean invincible = false;
	private boolean shoot;
	private float tempInv_countdown;
	private float shoot_cooldown;
	private float shoot_constant_cooldown = 0.25f;
	
	//Animation
	private Animation<TextureRegion> forwardAnimation;
	private static final float SHIP_ANIMATION_SPEED = 0.1f;
	private static final int FRAME_COLS = 3, FRAME_ROWS = 1;
	private float stateTime;
	private float inv_stateTime;
	
	//Sprite
	private Sprite ship;
	
	Vector2 startLine;
	Vector2 endLine;
	
	public Player(float x, float y, State level, SpaceTreason game) {
		this.game = game;
		this.level = level;
		batch = game.batch;
		life_state = Listener.plife_energy1_texture;
		activeBullets = new ArrayList<Bullet>();
		poolBullets = new Pool<Bullet>() {
			@Override
			protected Bullet newObject() {
				return new Bullet(null);
			}
		};
		
		activeiBullets = new ArrayList<IntelligentBullet>();
		pooliBullets = new Pool<IntelligentBullet>() {
			@Override
			protected IntelligentBullet newObject() {
				return new IntelligentBullet(null);
			}
		};
		
		//Sprite
		ship = new Sprite(Listener.ship_texture);
		
		set(ship);
		setPosition(x, y);
		setAlive(true);
		
		//Animation
		startLine = new Vector2(getX() + (getWidth() / 2), getY() + 10);
		endLine = new Vector2(getX() + (getWidth() / 2), 0);
		TextureRegion[][] spriteSheet = TextureRegion.split(Listener.ship_forward_texture, Listener.SHIP_TILE_WIDTH,
				Listener.SHIP_TILE_HEIGHT);
		
		TextureRegion[] shipFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		int index = 0;
		for (int i = 0; i < FRAME_ROWS; i++) {
			for (int j = 0; j < FRAME_COLS; j++) {
				shipFrames[index++] = spriteSheet[i][j];
			}
		}
		
		forwardAnimation = new Animation<TextureRegion>(SHIP_ANIMATION_SPEED, shipFrames);
		stateTime = 0f;
	}
	
	public void render(float delta) {
		//fire animation
		//lerp end line
		/*float lerp = 5f;
		startLine.x = getX() + (getWidth() / 2);
		startLine.y = getY() + 10;
		endLine.x += (startLine.x - endLine.x) * lerp * delta;
		Utils.drawLine(startLine, endLine, new Color(255, 204, 0, 0.75f), 2, PlayScreen1.getCamera().combined);
		Utils.drawLine(startLine, endLine, new Color(255, 204, 0, 0.4f), 6, PlayScreen1.getCamera().combined);
		Utils.drawLine(startLine, endLine, new Color(255, 204, 0, 0.2f), 10, PlayScreen1.getCamera().combined);*/
		
		if (!PlayScreen.paused) {
			stateTime += delta; // Accumulate elapsed animation time
		}
		//draw
		batch.begin();
			if (isAlive()) {
				if (tempInv_countdown > 0) {
					if (inv_stateTime <= 0) {
						setRegion(forwardAnimation.getKeyFrame(stateTime, true));
						draw(batch);
						if (inv_stateTime <= -.25f)
							inv_stateTime = .25f;
					}
				} else {
					setRegion(forwardAnimation.getKeyFrame(stateTime, true));
					draw(batch);
				}
				if (!activeBullets.isEmpty()) {
					for (int i = 0; i < activeBullets.size(); i++) {
						Bullet item = activeBullets.get(i);
						item.render(batch);
					}
				}
				
				if (!activeiBullets.isEmpty()) {
					for (int i = 0; i < activeiBullets.size(); i++) {
						IntelligentBullet item = activeiBullets.get(i);
						item.render(batch);
					}
				}
			} else {
				draw(batch);
			}
			batch.draw(life_state, 10, 10);
		batch.end();
	}
	
	public void update(float delta) {
		if (isAlive()) {
			inputs(delta);
			//player position
			setX(getX() + dx);
			setY(getY() + dy);
			
			//shoot
			if (shoot) {
				shoot_cooldown -= delta;
				if (shoot_cooldown <= 0) {
					shoot_cooldown = shoot_constant_cooldown;
					shoot();
				}
			} else {
				if (shoot_cooldown != 0)
					shoot_cooldown = 0;
			}
			
			//player temporarily invincible
			if (tempInv_countdown > 0) {
				tempInv_countdown -= delta;
				inv_stateTime -= delta;
				if (!invincible) {
					invincible = true;
				}
			} else if (tempInv_countdown <= 0) {
				inv_stateTime = 0f;
				setTexture(Listener.ship_texture);
				if (invincible) {
					invincible = false;
				}
			}
			
			//player life
			lifeCheck();
			
			//level state
			levelCheck();
			
			if (getX() >= 360) setX(360);
			if (getX() <= 0) setX(0);
			if (getY() >= 550) setY(550);
			if (getY() <= 0) setY(0);
			
			//bullets
			Bullet item;
			
			for (int i = activeBullets.size(); --i >= 0;) {
				item = activeBullets.get(i);
				if (!item.isAlive()) {
					activeBullets.remove(item);
					poolBullets.free(item);
				}
				item.update(delta);
			}
			
			IntelligentBullet iitem;
			
			for (int i = activeiBullets.size(); --i >= 0;) {
				iitem = activeiBullets.get(i);
				if (!iitem.isAlive()) {
					activeiBullets.remove(iitem);
					pooliBullets.free(iitem);
				}
				iitem.update(delta);
			}
		}/* else {
			this.dispose();
		}*/
	}
	
	public void inputs(float delta) {
		dx = 0;
		dy = 0;
		
		/*if (!getTexture().equals(Listener.ship_texture))
			setTexture(Listener.ship_texture);*/
		
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			//sprite change
			/*if (!getTexture().equals(Listener.ship_right_texture))
				setTexture(Listener.ship_right_texture);*/
			dx = move_speed * delta;
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			//sprite change
			/*if (!getTexture().equals(Listener.ship_left_texture))
				setTexture(Listener.ship_left_texture);*/
			dx = -move_speed * delta;
		}
		if (Gdx.input.isKeyJustPressed(Keys.W)) {
			ishoot();
		}
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			dy = move_speed * delta;
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			dy = -move_speed * delta;
		}
		if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
			move_speed = acc_speed;
		} else {
			move_speed = base_speed;
		}
		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			if (shoot != true)
				shoot = true;
		} else {
			if (shoot != false)
				shoot = false;
		}
	}
	
	@Override
	public float getWidth() {
		return super.getWidth() - 5f;
	}
	
	public Vector2 getPosition() {
		return super.getPosition();
	}
	
	public void temporarilyInvincible() {
		if (tempInv_countdown <= 0) {
			tempInv_countdown = 2.5f;
			inv_stateTime = .25f;
		}
	}
	
	public void hit(float life_points) {
		this.life_points-=life_points;
		System.out.println("Player's life : " + this.life_points);
	}
	
	public boolean isInvincible() {
		return invincible;
	}

	public float getLife() {
		return life_points;
	}
	
	public boolean isDead() {
		return life_points <= 0;
	}
	
	private void lifeCheck() {
		/*if (10 <= life_points || life_points > 8) {
			life_state = Listener.plife_energy1_texture;
		} else if (8 <= life_points || life_points > 6) {
			life_state = Listener.plife_energy2_texture;
		} else if (6 <= life_points || life_points > 4) {
			life_state = Listener.plife_energy3_texture;
		} else if (4 <= life_points || life_points > 2) {
			life_state = Listener.plife_energy4_texture;
		} else if (2 <= life_points || life_points > 1) {
			life_state = Listener.plife_energy5_texture;
		} else if (life_points <= 0) {
			life_state = Listener.plife_energy6_texture;
			setAlive(false);
		}*/
		float life = life_points;
		if (life >= 5)
			life_state = Listener.plife_energy1_texture;
		else if (life < 5 && life >= 4)
			life_state = Listener.plife_energy2_texture;
		else if (life < 4 && life >= 3)
			life_state = Listener.plife_energy3_texture;
		else if (life < 3 && life >= 2)
			life_state = Listener.plife_energy4_texture;
		else if (life < 2 && life > 0)
			life_state = Listener.plife_energy5_texture;
		else {
			life_state = Listener.plife_energy6_texture;
			setAlive(false);
		}
	}
	
	private void levelCheck() {
		
	}
	
	public ArrayList<Bullet> getBullets() {
		return activeBullets;
	}
	
	public ArrayList<IntelligentBullet> getIntelligentBullets() {
		return activeiBullets;
	}
	
	public void shoot() {
		switch (level) {
			default :
				if (shoot_constant_cooldown != 0.25f)
					shoot_constant_cooldown = 0.25f;
				Bullet item = poolBullets.obtain();
				item.init(getX() + (getWidth() / 2), getY() + getHeight());
				activeBullets.add(item);
				break;
			case LEVEL_2 :
				if (shoot_constant_cooldown != 0.20f)
					shoot_constant_cooldown = 0.20f;
				for (int i = 1; i <= 3; i++) {
					//loop 3 times
					//boucle 3 fois
					Bullet item2 = poolBullets.obtain();
					item2.init(getX() + (getWidth() / 2), getY() + getHeight());
					switch (i) {
						case 1 :
							item2.setRotation(0f);
							break;
						case 2 :
							item2.setRotation(5f);
							break;
						case 3 :
							item2.setRotation(-5f);
							break;
					}
					activeBullets.add(item2);
				}
				break;
			case LEVEL_3 :
				if (shoot_constant_cooldown != 0.20f)
					shoot_constant_cooldown = 0.20f;
				for (int i = 1; i <= 5; i++) {
					//loop 3 times
					//boucle 3 fois
					Bullet item3 = poolBullets.obtain();
					item3.init(getX() + (getWidth() / 2), getY() + getHeight());
					switch (i) {
					case 1 :
						item3.setRotation(0f);
						break;
					case 2 :
						item3.setRotation(5f);
						break;
					case 3 :
						item3.setRotation(-5f);
						break;
					}
					activeBullets.add(item3);
				}
				break;
			case LEVEL_4 :
				if (shoot_constant_cooldown != 0.15f)
					shoot_constant_cooldown = 0.15f;
				for (int i = 1; i <= 5; i++) {
					//loop 3 times
					//boucle 3 fois
					Bullet item4 = poolBullets.obtain();
					item4.init(getX() + (getWidth() / 2), getY() + getHeight());
					switch (i) {
						case 1 :
							item4.setRotation(0f);
							break;
						case 2 :
							item4.setRotation(5f);
							break;
						case 3 :
							item4.setRotation(-5f);
							break;
						case 4 :
							item4.setRotation(10f);
							break;
						case 5 :
							item4.setRotation(-10f);
							break;
					}
					activeBullets.add(item4);
				}
				break;
			case LEVEL_5 :
				if (shoot_constant_cooldown != 0.10f)
					shoot_constant_cooldown = 0.10f;
				for (int i = 1; i <= 5; i++) {
					//loop 3 times
					//boucle 3 fois
					Bullet item4 = poolBullets.obtain();
					item4.init(getX() + (getWidth() / 2), getY() + getHeight());
					switch (i) {
						case 1 :
							item4.setRotation(0f);
							break;
						case 2 :
							item4.setRotation(5f);
							break;
						case 3 :
							item4.setRotation(-5f);
							break;
						case 4 :
							item4.setRotation(10f);
							break;
						case 5 :
							item4.setRotation(-10f);
							break;
					}
					activeBullets.add(item4);
				}
				break;
		}
	}
	
	public void setLevel(State level) {
		this.level = level;
	}
	
	public State getLevel() {
		return level;
	}
	
	public void addLevel() {
		switch (level) {
		case LEVEL_2:
			setLevel(State.LEVEL_3);
			break;
		case LEVEL_3 :
			setLevel(State.LEVEL_4);
			break;
		case LEVEL_4 :
			setLevel(State.LEVEL_5);
			break;
		case LEVEL_5 :
			System.out.println("Max level");
			break;
		default:
			setLevel(State.LEVEL_2);
			break;
		}
		return;
	}
	
	public void ishoot() {
		IntelligentBullet item = pooliBullets.obtain();
		item.init(getX() + (getWidth() / 2), getY() + getHeight());
		activeiBullets.add(item);
	}

	public void dispose() {
		Listener.plife_energy1_texture.dispose();
		Listener.plife_energy2_texture.dispose();
		Listener.plife_energy3_texture.dispose();
		Listener.plife_energy4_texture.dispose();
		Listener.plife_energy5_texture.dispose();
		Listener.plife_energy6_texture.dispose();
		getTexture().dispose();
		poolBullets.clear();
		pooliBullets.clear();
		for (int i = 0; i < activeBullets.size(); i++) {
			Bullet item = activeBullets.get(i);
			item.dispose();
		}
		for (int i = 0; i < activeiBullets.size(); i++) {
			IntelligentBullet item = activeiBullets.get(i);
			item.dispose();
		}
	}
	
}
