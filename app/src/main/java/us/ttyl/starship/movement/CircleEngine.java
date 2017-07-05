package us.ttyl.starship.movement;

import us.ttyl.starship.core.Constants;
import us.ttyl.starship.core.GameState;

/**
 * planets and base stations, enemy fighters use this
 * @author kurt ishisaka
 *
 */
public class CircleEngine extends MovementEngine
{
   protected CircleEngine(
   	int direction, 
   	int currentDirection, 
   	double currentX, 
   	double currentY, 
   	double currentSpeed, 
   	double maxSpeed, 
   	double acceleration, 
   	double turnMode,
	int name, 
	int endurance)
   {
    _name = name;
    _direction = direction;
    _currentDirection = currentDirection;
    _currentSpeed = currentSpeed;
    _maxSpeed = maxSpeed;
    _acceleration = acceleration;
    _currentX = currentX;
    _currentY = currentY;
    _destroyed = false;
    _turnMode = turnMode;
	_endurance = endurance;
  }

  public void doOther()
  {
  }

  public void updateDisplacement(int layer)
  {
    if (_currentSpeed > 0)
    {
		double xChange = (GameState._density * (Math.cos(Math.toRadians(_currentDirection)) * (_currentSpeed / layer)));
		double yChange = (GameState._density * (Math.sin(Math.toRadians(_currentDirection)) * (_currentSpeed / layer)));
		_currentX = _currentX + xChange;
		_currentY = _currentY + yChange;

		_currentMapX = _currentMapX + (int)xChange;
		if (_currentMapX < 0) {
			_currentMapX = _currentMapX + Constants.xMax;
		}
		if (_currentMapX > Constants.xMax) {
			_currentMapX = _currentMapX - Constants.xMax;
		}
		_currentMapY = _currentMapY + (int)yChange;
		if (_currentMapY < 0) {
			_currentMapY = _currentMapY + Constants.yMax;
		}
		if (_currentMapY > Constants.yMax) {
			_currentMapY = _currentMapY - Constants.yMax;
		}
    }
  }

  public void updateSpeed()
  {
    if (_currentSpeed > 0)
    {
      _currentSpeed = _currentSpeed + _acceleration;
      if (_currentSpeed > _maxSpeed)
      {
        _currentSpeed = _maxSpeed;
      }
    }
  }

  	public void updateDirection()
  	{

  		if (_currentTurnMode >= _turnMode)
  		{
  			_currentTurnMode = 0;
  			_currentDirection = _currentDirection + 1;
  			if (_currentDirection > 359)
  			{
  				_currentDirection = 0;
  			}
  		}
  		else
  		{
  			_currentTurnMode = _currentTurnMode + _currentSpeed;
  		}
  	}
	
	@Override
	public void updateFuelUsage() 
	{
		// TODO Auto-generated method stub		
	}

	@Override
	public void updateSpeedDecrease() 
	{
		// TODO Auto-generated method stub	
	}
	
	@Override
	public void updateSpeedIncrease() 
	{
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onCollision(MovementEngine engine2)
	{
	}

}
