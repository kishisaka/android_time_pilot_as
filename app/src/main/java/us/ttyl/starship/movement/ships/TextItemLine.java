package us.ttyl.starship.movement.ships;

import us.ttyl.starship.core.Constants;
import us.ttyl.starship.core.GameState;
import us.ttyl.starship.movement.LineEngine;

/**
 * Created by test on 7/7/2015.
 */
public class TextItemLine extends LineEngine {

    private String mText;

    public TextItemLine(String text, double speed, int endurance, double x, double y, int direction) {
        super(direction, direction, x, y, speed, speed,
                0, 0, Constants.TEXT_ITEM_LINE, GameState._weaponList.get(0), endurance, -1);
        mText = text;
    }

    public String getText()
    {
        return mText;
    }
}

