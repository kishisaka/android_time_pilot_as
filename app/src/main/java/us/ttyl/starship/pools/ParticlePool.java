package us.ttyl.starship.pools;

import androidx.core.util.Pools;

import us.ttyl.starship.movement.MovementEngine;
import us.ttyl.starship.movement.ships.ExplosionParticle;

public class ParticlePool {
    private static final Pools.SynchronizedPool<ExplosionParticle> particlePool = new Pools.SynchronizedPool<ExplosionParticle>(1000);

    public static ExplosionParticle obtain(int direction, int currentDirection, double currentX,
                                double currentY, double currentSpeed, double maxSpeed,
                                double acceleration, int turnMode, int name, MovementEngine origin,
                                int endurance, int hitpoints) {
        ExplosionParticle b = particlePool.acquire();
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
            b.setHitPoints(hitpoints);
            b.setDestroyed(false);
        } else {
            b = new ExplosionParticle(direction, currentDirection, currentX, currentY,
                    currentSpeed, maxSpeed, acceleration, turnMode, name, origin,
                    endurance, hitpoints);
        }
        return b;
    }

    public static void recycle(ExplosionParticle explosionParticle) {
        if (explosionParticle != null) {
            try {
                particlePool.release(explosionParticle);
            } catch(Exception e) {
            }
        }
    }
}
