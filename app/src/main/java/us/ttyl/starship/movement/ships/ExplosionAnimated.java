package us.ttyl.starship.movement.ships;

import us.ttyl.starship.core.Constants;
import us.ttyl.starship.core.GameState;
import us.ttyl.starship.movement.LineEngine;
import us.ttyl.starship.movement.MovementEngine;

public class ExplosionAnimated extends LineEngine
{
	public ExplosionAnimated(double currentX, double currentY, double speed, int currentDirection) {
		super(currentDirection, currentDirection, currentX, currentY, speed, speed,
				0, 0, Constants.ANIMATED_EXPLOSION, null, GameState._explosionSprites.size() - 1, 1);
		// TODO Auto-generated constructor stub
	}
}
