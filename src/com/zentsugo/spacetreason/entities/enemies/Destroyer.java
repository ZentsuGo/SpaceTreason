package com.zentsugo.spacetreason.entities.enemies;

import com.zentsugo.spacetreason.SpaceTreason;
import com.zentsugo.spacetreason.screens.PlayScreen;

public class Destroyer extends Enemy {
	public Destroyer(SpaceTreason game, PlayScreen lvl) {
		super(game, lvl);
		setSpeed(200f);
		//setLife(2);
		setBulletDamage(0.5f);
		setShoot(true);
	}
	
	@Override
	public void reset() {
		super.reset();
		setSpeed(200f);
		setBulletDamage(0.5f);
		setShoot(true);
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
	}
}
