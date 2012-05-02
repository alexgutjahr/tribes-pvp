package jobs;

import models.User;
import play.Logger;
import play.jobs.Job;

/**
 * @author <a href="mailto:alexander.hanschke@gmail.com">Alexander Hanschke</a>
 */
public class BloodHealer extends StatusJob {

    public BloodHealer(Long id) {
        super(id);
    }

    @Override
    protected final void heal(User user) {
        user.blood++;

        if (user.blood > user.maxblood) {
            user.blood = user.maxblood;
        }

        if (user.needsBlood()) {
            new BloodHealer(user.id).in(user.bloodCooldown);
        } else {
            user.bloodJobRunning = Boolean.FALSE;
        }
    }

    @Override
    protected final boolean needsHeal(User user) {
        return user.needsBlood();
    }

}
