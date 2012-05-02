package jobs;

import models.User;
import play.Logger;
import play.jobs.Job;

/**
 * @author <a href="mailto:alexander.hanschke@gmail.com">Alexander Hanschke</a>
 */
public class RageHealer extends StatusJob {

    public RageHealer(Long id) {
        super(id);
    }

    @Override
    protected final void heal(User user) {
        user.rage++;

        if (user.rage > user.maxrage) {
            user.rage = user.maxrage;
        }

        if (user.needsRage()) {
            new RageHealer(user.id).in(user.rageCooldown);
        } else {
            user.rageJobRunning = Boolean.FALSE;
        }
    }

    @Override
    protected final boolean needsHeal(User user) {
        return user.needsRage();
    }

}
