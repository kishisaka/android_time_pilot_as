package us.ttyl.starship.movement;

import us.ttyl.starship.core.GameUtils;

/**
 * Created by test2 on 9/2/2015.
 */
public class OrbitEngine extends MovementEngine {

    private int mCurrentDegree = 1;
    private int mRadius = 100;
    private int mSpeed = 10;

    protected OrbitEngine(MovementEngine origin, int name, int hitPoints, int startingDegree,
                          int radius, int speed) {
        _origin = origin;
        _name = name;
        _hitPoints = hitPoints;
        mCurrentDegree = startingDegree;
        mRadius = radius;
        mSpeed = speed;
    }
    public void updateSpeed() {

    }

    @Override
    public void updateSpeedIncrease() {

    }

    @Override
    public void updateSpeedDecrease() {

    }

    @Override
    public void updateDirection() {
    }

    @Override
    public void updateDisplacement(int layer) {
        double[] coords = GameUtils.getCoordsGivenTrackAndDistance(mCurrentDegree, mRadius);
        _currentX = _origin._currentX + coords[0];
        _currentY = _origin._currentY + coords[1];
        if (mCurrentDegree > 360) {
            mCurrentDegree = 1;
        }
        else {
            mCurrentDegree = mCurrentDegree + mSpeed;
        }
    }

    @Override
    public void updateFuelUsage() {
        _endurance = -1;
    }

    @Override
    public void doOther() {
    }

    @Override
    public void onCollision(MovementEngine engine2) {

    }
}
