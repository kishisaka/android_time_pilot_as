package us.ttyl.starship.movement;

import us.ttyl.starship.core.GameState;
import us.ttyl.starship.core.GameUtils;

public class TextEngine extends MovementEngine
{
	MovementEngine _target = null;
  
	protected TextEngine(
		  int direction, 
		  int currentDirection, 
		  double currentX, 
		  double currentY, 
		  double currentSpeed, 
		  double maxSpeed, 
		  double acceleration, 
		  int turnMode, 
		  int name, 
		  MovementEngine target, 
		  MovementEngine origin, 
		  int endurance)
	{
		_target = target;
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
		_origin = origin;
	}

	public void doOther()
	{
	}

	public void updateDisplacement()
	{
	    if (_currentSpeed > 0)
	    {
	    	_currentX = _currentX + (GameState._density * (Math.cos(Math.toRadians(_currentDirection)) * _currentSpeed));
		    _currentY = _currentY + (GameState._density * (Math.sin(Math.toRadians(_currentDirection)) * _currentSpeed));
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
    //get direction
    _direction = ((int)GameUtils.track(_currentX, _currentY, _target.getX(), _target.getY()));
    _currentDirection = _direction;
    
  }
	
	@Override
	public void updateFuelUsage() 
	{
		if (_endurance > 0)
		{
			_endurance = _endurance - 1;
		}
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
	public void onCollision(MovementEngine engine2) {
		// TODO Auto-generated method stub
		
	}
	
	public MovementEngine getTarget()
	{
		return _target;
	}
}
