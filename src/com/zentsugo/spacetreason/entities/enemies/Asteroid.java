package com.zentsugo.spacetreason.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.zentsugo.spacetreason.Listener;
import com.zentsugo.spacetreason.SpaceTreason;
import com.zentsugo.spacetreason.Utils;
import com.zentsugo.spacetreason.screens.PlayScreen;

public class Asteroid extends Enemy {
	
	private SpaceTreason game;

	public Asteroid(SpaceTreason game, PlayScreen lvl) {
		super(game, lvl);
		this.game = game;
		set(new Sprite(Listener.asteroid_texture));
		setSpeed(700f);
		setLife(1);
		setShoot(false);
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
	}
	
	
	@Override
	public void render(float delta) {
		super.render(delta);
		Vector2 start = new Vector2(getX() + (getWidth() /2), getY());
		Vector2 end = new Vector2(getX() + (getWidth() / 2), getY() + 50);
		rotate(50f * delta);
		Utils.drawLine(start, end, new Color(255, 0, 0, 0.4f), 6, game.camera.combined);
		Utils.drawLine(start, end, new Color(255, 153, 0, 0.2f), 10, game.camera.combined);
	}

}
