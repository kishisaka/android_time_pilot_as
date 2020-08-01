package us.ttyl.starship.pools;

import androidx.core.util.Pools;

import us.ttyl.starship.movement.MovementEngine;
import us.ttyl.starship.movement.ships.Bullet;

public class BulletPool {
    private static final Pools.SynchronizedPool<Bullet> bulletPool = new Pools.SynchronizedPool<Bullet>(1000);

    public static Bullet obtain(int direction, int currentDirection, double currentX,
                                double currentY, double currentSpeed, double maxSpeed,
                                double acceleration, int turnMode, int name, MovementEngine origin,
                                int endurance) {
        Bullet b = bulletPool.acquire();
        if (b != null) {
            b.setDirection(direction);
            b.setCurrentDirection(currentDirection);
            b.setCurrentX(currentX);
            b.setCurrentY(currentY);
            b.setCurrentSpeed(currentSpeed);
            b.setMaxSpeed(maxSpeed);
            b.setAcceleration(acceleration);
            b.setTurnMode(turnMode);
            b.setName(name);
            b.setOrigin(origin);
            b.setEndurance(endurance);
            b.setHitPoints(1);
            b.setDestroyed(false);
        } else {
            b = new Bullet(direction, currentDirection, currentX, currentY,
                    currentSpeed, maxSpeed, acceleration, turnMode, name, origin,
                    endurance, 1);
        }
        return b;
    }

    public static void recycle(Bullet bullet) {
        bulletPool.release(bullet);
    }
 }
