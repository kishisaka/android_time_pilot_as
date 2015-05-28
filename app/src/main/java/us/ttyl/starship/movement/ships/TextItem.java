package us.ttyl.starship.movement.ships;

import us.ttyl.starship.core.Constants;
import us.ttyl.starship.core.GameState;
import us.ttyl.starship.movement.FollowImmediateEngine;
import us.ttyl.starship.movement.MovementEngine;

public class TextItem extends FollowImmediateEngine
{
	private String mText = "";

	public TextItem(int direction, int currentDirection, double currentX,
					double currentY, String text) {
		super(direction, currentDirection, currentX, currentY, 8, 8,
				1, 0, Constants.TEXT,  GameState._weaponList.get(0),
				GameState._weaponList.get(0), -1);
		mText = text;
	}	
		
	public String getText()
	{
		return mText;
	}

	@Override
	public void onCollision(MovementEngine engine2)
	{
		if (engine2.getWeaponName() == Constants.PLAYER)
		{
			this.setEndurance(0);
			this.setDestroyedFlag(true);
		}
	}
}
