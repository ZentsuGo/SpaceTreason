package com.zentsugo.tween;

import com.badlogic.gdx.graphics.g2d.Sprite;

import aurelienribon.tweenengine.TweenAccessor;

public class SpriteAccessor implements TweenAccessor<Sprite> {

	public static final int ALPHA = 0;
	
	@Override
	public int getValues(Sprite target, int type, float[] values) {
		if (type == ALPHA) {
			values[0] = target.getColor().a;
			return 1;
		} else {
			assert false;
			return -1;
		}
	}

	@Override
	public void setValues(Sprite target, int type, float[] values) {
		if (type == ALPHA) {
			target.setColor(target.getColor().a, target.getColor().g,
					target.getColor().b, values[0]);
		} else {
			assert false;
		}
	}

}
