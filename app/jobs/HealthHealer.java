package jobs;

import models.User;
import play.Logger;

/**
 * @author <a href="mailto:alexander.hanschke@gmail.com">Alexander Hanschke</a>
 */
public class HealthHealer extends StatusJob {

    public HealthHealer(Long id) {
        super(id);
    }

    @Override
    protected final void heal(User user) {
        user.health = user.health + 10;

        if (user.health > user.maxhealth) {
            user.health = user.maxhealth;
        }

        if (user.needsHealth()) {
            new HealthHealer(user.id).in(user.healthCooldown);
        } else {
            user.healthJobRunning = Boolean.FALSE;
        }
    }

    @Override
    protected final boolean needsHeal(User user) {
        return user.needsHealth();
    }
}
