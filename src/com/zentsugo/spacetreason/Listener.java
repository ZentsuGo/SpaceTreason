package com.zentsugo.spacetreason;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Listener {
	//constants
	public static final int SCREEN_WIDTH = 400;
	public static final int SCREEN_HEIGHT = 600;
	
	//textures
	public static Texture texture_splashScreen;
	public static Sprite splashScreen;
	public static Texture BG2_texture;
	public static Texture BG3_texture;
	public static Texture BG4_texture;
	public static Texture BG5_texture;
	public static Texture BG5_safe_texture;
	public static Texture enemy_texture;
	public static Texture asteroid_texture;
	public static Texture ship_texture;
	public static Texture ship_2_texture;
	public static Texture plife_energy1_texture;
	public static Texture plife_energy2_texture;
	public static Texture plife_energy3_texture;
	public static Texture plife_energy4_texture;
	public static Texture plife_energy5_texture;
	public static Texture plife_energy6_texture;
	public static Texture play_btn_texture;
	public static Texture play_btn2_texture;
	public static Texture explosion_texture;
	public static Texture balleffect_texture;
	
	//Animation
	public static Texture ship_forward_texture;
	public static int SHIP_TILE_WIDTH;
	public static int SHIP_TILE_HEIGHT;
	public static Texture ship_right_texture;
	public static Texture ship_left_texture;
	
	public static void loadAssets() {
		texture_splashScreen = new Texture("images/splashScreen.png");
		splashScreen = new Sprite(texture_splashScreen);
		splashScreen.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		BG2_texture = new Texture("images/background_2.png");
		BG3_texture = new Texture("images/background_3.png");
		BG4_texture = new Texture("images/background_4.png");
		BG5_texture = new Texture("images/background_5.png");
		BG5_safe_texture = new Texture("images/background_5_safe.png");
		ship_texture = new Texture("images/Model.png");
		ship_2_texture = new Texture("images/Model_dead.png");
		plife_energy1_texture = new Texture("images/life_energy/life_energy_1.png");
		plife_energy2_texture = new Texture("images/life_energy/life_energy_2.png");
		plife_energy3_texture = new Texture("images/life_energy/life_energy_3.png");
		plife_energy4_texture = new Texture("images/life_energy/life_energy_4.png");
		plife_energy5_texture = new Texture("images/life_energy/life_energy_5.png");
		plife_energy6_texture = new Texture("images/life_energy/life_energy_6.png");
		enemy_texture = new Texture("images/enemy.png");
		asteroid_texture = new Texture("images/asteroid.png");
		play_btn_texture = new Texture("images/play_btn.png");
		play_btn2_texture = new Texture("images/play_btn_2.png");
		
		//Animation
		ship_forward_texture = new Texture("images/animations/ship_forward.png");
		ship_right_texture = new Texture("images/animations/ship_right.png");
		ship_left_texture = new Texture("images/animations/ship_left.png");
		explosion_texture = new Texture("images/animations/explosion.png");
		balleffect_texture = new Texture("images/animations/explosion.png");
		SHIP_TILE_WIDTH = 40;
		SHIP_TILE_HEIGHT = 50;
		Gdx.app.debug("Assets", "Assets loaded.");
		System.out.println("Assets loaded.");
	}
}
