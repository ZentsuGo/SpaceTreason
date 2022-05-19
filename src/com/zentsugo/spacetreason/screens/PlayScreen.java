package com.zentsugo.spacetreason.screens;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pool;
import com.zentsugo.spacetreason.Listener;
import com.zentsugo.spacetreason.SpaceTreason;
import com.zentsugo.spacetreason.Utils;
import com.zentsugo.spacetreason.entities.BallEffect;
import com.zentsugo.spacetreason.entities.Bullet;
import com.zentsugo.spacetreason.entities.Explosion;
import com.zentsugo.spacetreason.entities.IntelligentBullet;
import com.zentsugo.spacetreason.entities.Player;
import com.zentsugo.spacetreason.entities.Player.State;
import com.zentsugo.spacetreason.entities.enemies.Asteroid;
import com.zentsugo.spacetreason.entities.enemies.Destroyer;
import com.zentsugo.spacetreason.entities.enemies.Enemy;

public class PlayScreen implements Screen {
	
	//game
	private SpaceTreason game;
	private PlayScreen lvl;
	private SpriteBatch batch;
	private Sprite background;
	private static OrthographicCamera camera;
	public static boolean paused = false;
	//entities
	public static Player player;
	private ArrayList<Enemy> activeEnemies;
	private ArrayList<Explosion> explosions;
	//private ArrayList<BallEffect> ballexplosions;
	private Pool<Asteroid> poolAsteroids;
	private Pool<Destroyer> poolDestroyers;
	private int score;
	private BitmapFont font;
	private float monster_cooldown = 1f; 
	private float monster_cooldown2 = 4f; 
	private float asteroid_cooldown = 3f; 
	private float waiting_cooldown = 3f;
	private float score_cooldown = 0.3f;
	private float bg_speed;
	
	public PlayScreen(SpaceTreason game) {
		this.game = game;
		this.lvl = this;
		batch = game.batch;
		camera = game.camera;
	}
	
	@Override
	public void show() {
		score = 0;
		bg_speed = 400f;
		player = new Player((Listener.SCREEN_WIDTH / 2) - (Listener.SHIP_TILE_WIDTH / 2), 10, State.LEVEL_1, game);
		activeEnemies = new ArrayList<Enemy>();
		explosions = new ArrayList<Explosion>();
		poolAsteroids = new Pool<Asteroid>() {
			@Override
			protected Asteroid newObject() {
				return new Asteroid(game, lvl);
			}
		};
		poolDestroyers = new Pool<Destroyer>() {
			@Override
			protected Destroyer newObject() {
				return new Destroyer(game, lvl);
			}
		};
		background = new Sprite(Listener.BG5_safe_texture);
		background.setX(-100);
		font = new BitmapFont(Gdx.files.internal("fonts/joystix.fnt"));
		font.getData().setScale(0.8f);
	}
	
	public void inputs(float delta) {
		//inputs content
		if (Gdx.input.justTouched()) {
			paused = !paused;
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.T)) {
			player.addLevel();
			System.out.println("Level up, player level : " + player.getLevel());
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.P)) {
			game.setScreen(new MainMenuScreen(game));
		}
	}
	
	public void spawnEnemyY() {
		Enemy item = poolDestroyers.obtain();
		item.init(Utils.randomX(), 650);
		activeEnemies.add(item);
	}
	
	public void spawnEnemyX() {
		Enemy item = poolDestroyers.obtain();
		item.init(-50, Utils.randomY());
		item.setShoot(false);
		item.setDirx(true, true);
		//item.setSpeed(100f);
		item.setSpeed(300f);
		activeEnemies.add(item);
	}
	
	public void spawnEnemyX2() {
		Enemy item = poolDestroyers.obtain();
		item.init(500, Utils.randomY());
		item.setShoot(false);
		item.setDirx(true, false);
		item.setSpeed(300f);
		activeEnemies.add(item);
	}	
	
	public void spawnAsteroid() {
		Asteroid item = poolAsteroids.obtain();
		item.init(Utils.randomX(), 650);
		activeEnemies.add(item);
	}
	
	public void update(float delta) {
		inputs(delta);
		if (!paused) {
			player.update(delta);
			//rumbleCheck
			Utils.rumbleCheck(camera, player, delta);
			
			//player death check
			if (player.isDead()) {
				background.setTexture(null);
				player.setTexture(Listener.ship_2_texture);
				player.setPosition((Listener.SCREEN_WIDTH / 2) - (player.getWidth() / 2), 10);
				killAll();
				Utils.rumble(2f, 6f);
			}
			
			if (!(player.isDead())) {
				//score
				score_cooldown -= delta;
				if (score_cooldown <= 0) {
					score++;
					score_cooldown = 0.3f;
				}
					
	 			//collisions
				for (int i = 0; i < activeEnemies.size(); i++) {
					Enemy e = activeEnemies.get(i);
					if (!(e instanceof Asteroid)) {
						for (int y = 0; y < e.getBullets().size(); y++) {
							IntelligentBullet b = e.getBullets().get(y);
							if (player.getIntelligentBullets().size() > 0) {
								for (int j = 0; j < player.getIntelligentBullets().size(); j++) {
									IntelligentBullet c = player.getIntelligentBullets().get(j);
										if (c.getBoundingRectangle().overlaps(b.getBoundingRectangle())) {
											b.setAlive(false);
											c.setAlive(false);
											break;
										}
								}
							}
							
							//bullet - bullet collision
							if (player.getBullets().size() > 0) {
								for (int j = 0; j < player.getBullets().size(); j++) {
									Bullet c = player.getBullets().get(j);
									if (c.getBoundingRectangle().overlaps(b.getBoundingRectangle())) {
										b.setAlive(false);
										c.setAlive(false);
										break;
									}
								}
							}
							
							if (player.getBoundingRectangle().overlaps(b.getBoundingRectangle())) {
								if (!player.isInvincible()) {
									//collaps
									System.out.println("-1 life bullet.");
									Utils.rumble(2.5f, .5f);
									player.hit(b.getDamage());
									if (!(player.getLife() <= 0))
										player.temporarilyInvincible();
									b.setAlive(false);
								}
								break;
							}
						}
					}
					
					//player - enemy collision
					if (player.getBoundingRectangle().overlaps(e.getBoundingRectangle())) {
						if (!player.isInvincible()) {
							//explosion of enemy
							explosions.add(new Explosion(e.getX(), e.getY(), e.getWidth(), e.getHeight()));
							e.setAlive(false);
							//
							System.out.println("-1 life.");
							Utils.rumble(2.5f, .5f);
							player.hit(0.5f);
							if (!(player.getLife() <= 0))
								player.temporarilyInvincible();
						}
						break;
					}
					//enemy - bullet collision
					for (int y = 0; y < player.getIntelligentBullets().size(); y++) {
						IntelligentBullet ib = player.getIntelligentBullets().get(y);
						if (!ib.hasEnemy())
							ib.follow(e);
							if (e.getBoundingRectangle().overlaps(ib.getBoundingRectangle())) {
								if (e.getLife() -1 <= 0) {
									//explosion of enemy
									Utils.rumble(2f, .3f);
									explosions.add(new Explosion(e.getX(), e.getY(), e.getWidth(), e.getHeight()));
									e.setAlive(false);
									score += 100;
								}
								e.hit(ib.getDamage());
								ib.setAlive(false);
								break;
							}
					}
					for (int y = 0; y < player.getBullets().size(); y++) {
						Bullet b = player.getBullets().get(y);
						if (e.getBoundingRectangle().overlaps(b.getBoundingRectangle())) {
							if (e.getLife() - 1 <= 0) {
								//explosion of enemy
								Utils.rumble(2f, .3f);
								explosions.add(new Explosion(e.getX(), e.getY(), e.getWidth(), e.getHeight()));
								e.setAlive(false);
								score += 100;
							}
							e.hit(b.getDamage());
							b.setAlive(false);
							break;
						}
					}
				}
				//temp player level manager lol
				if (score < 1100 && score > 1000) {
					score = 1101;
					player.addLevel();
					bg_speed += 50f;
				} else if (score < 2200 && score > 2000) {
					score = 2201;
					player.addLevel();
					bg_speed += 50f;
				} else if (score < 3300 && score > 3000) {
					score = 3301;
					player.addLevel();
					bg_speed += 50f;
				} else if (score < 6600 && score > 6000) {
					score = 6601;
					player.addLevel();
					bg_speed += 50f;
				} else if (score < 11000 && score > 10000) {
					score = 11001;
					player.addLevel();
					bg_speed += 50f;
				}
				
				//entities spawner
				if (waiting_cooldown > 0) waiting_cooldown -= delta;
				if (waiting_cooldown <= 0) {
					monsterSpawner(delta);
					if (score >= 3000)
						monsterSpawner2(delta);
					if (score >= 6000)
						asteroidSpawner(delta);
				}
				
				//enemy pool
				if (!activeEnemies.isEmpty()) {
					for (int i = 0; i < activeEnemies.size(); i++) {
						Enemy item = activeEnemies.get(i);
						if (item instanceof Asteroid) {
							if (!item.isAlive()) {
								activeEnemies.remove(item);
								poolAsteroids.free((Asteroid) item);
							}
						}
						
						if (item instanceof Destroyer) {
							if (!item.isAlive()) {
								activeEnemies.remove(item);
								poolDestroyers.free((Destroyer) item);
							}
						}
						item.update(delta);
					}
				}
				
				//explosion pool
				if (!explosions.isEmpty()) {
					for (int i = 0; i < explosions.size(); i++) {
						Explosion item = explosions.get(i);
						if (!item.isAlive()) {
							explosions.remove(item);
						}
						item.update(delta);
					}
				}
			}
		} else {
			pause();
		}
	}
	
	private void killAll() {
		if (!activeEnemies.isEmpty()) {
			for (int i = 0; i < activeEnemies.size(); i++) {
				Enemy item = activeEnemies.get(i);
				item.dispose();
			}
			activeEnemies.clear();
			poolDestroyers.clear();
			poolAsteroids.clear();
		}
	}
	
	private void asteroidSpawner(float delta) {
		//asteroid spawner
		asteroid_cooldown -= delta;
		if (asteroid_cooldown <= 0) {
			Random r = new Random();
			float spawn_delay = r.nextFloat();
			asteroid_cooldown = spawn_delay;
			spawnAsteroid();
		}
	}
	
	//y monster spawner
	private void monsterSpawner(float delta) {
		//monster spawner
		monster_cooldown -= delta; //time passed
		if (monster_cooldown <= 0) {
			Random r = new Random();
			float spawn_delay = r.nextFloat() + 0.2f;
			monster_cooldown = spawn_delay;
			spawnEnemyY();
		}
	}
	
	//x monster spawner
	private void monsterSpawner2(float delta) {
		//monster spawner
		monster_cooldown2 -= delta;
		if (monster_cooldown2 <= 0) {
			Random r = new Random();
			float spawn_delay = r.nextFloat() + 0.6f;
			monster_cooldown2 = spawn_delay;
			spawnEnemyX();
			spawnEnemyX2();
		}
	}
	private float sx = 100;
	private float sy = 580;
	private float ss = 1f;
	@Override
	public void render(float delta) {
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
		update(delta);
		
		batch.begin();
			if (!(player.isDead()))
				cycleBackground(batch, delta);
			if (!paused) {
				if (!(player.isDead())) {
					font.draw(batch, "SCORE " + score, sx, sy);
				} else {
					font.setColor(Color.RED);
					sy-=100*delta;
					ss+=0.005f;
					sx-=20*delta;
					font.getData().setScale(ss);
					font.draw(batch, "SCORE\n" + score + "\nNOT BAD\n" + System.getProperty("user.name"), sx, sy);
					System.out.println("SY : " + sy);
					if (sy <= 30) {
						dispose();
						Gdx.app.exit();
					}
				}
				if (!explosions.isEmpty()) {
					for (int i = 0; i < explosions.size(); i++) {
						Explosion item = explosions.get(i);
						item.render(batch);
					}
				}
			} else
				font.draw(batch, "PAUSED", 100, 580);
		batch.end();
		if (!activeEnemies.isEmpty()) {
			for (int i = 0; i < activeEnemies.size(); i++) {
				Enemy item = activeEnemies.get(i);
				item.render(delta);
			}
		}
		player.render(delta);
	}
	
	public static OrthographicCamera getCamera() {
		return camera;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void cycleBackground(SpriteBatch batch, float delta) {
		if (!paused) {
			if (background.getY() > -1200)
				background.setY(background.getY() - bg_speed * delta);
			else if (background.getY() <= -1200)
				background.setY(0);
		}
		background.draw(batch);
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = 400f;
		camera.viewportHeight = 400f * height/width;
		camera.update();
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
		if (background.getTexture() != null)
			background.getTexture().dispose();
		if (!activeEnemies.isEmpty()) {
			for (int i = 0; i < activeEnemies.size(); i++) {
				Enemy item = activeEnemies.get(i);
				item.dispose();
			}
		}
		activeEnemies.clear();
		poolDestroyers.clear();
		poolAsteroids.clear();
		if (!explosions.isEmpty()) {
			for (int i = 0; i < explosions.size(); i++) {
				Explosion item = explosions.get(i);
				item.dispose();
			}
		}
		explosions.clear();
		font.dispose();
		player.dispose();
	}

}
