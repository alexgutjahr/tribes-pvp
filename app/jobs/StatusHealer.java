package jobs;

import models.User;
import play.Logger;
import play.jobs.Every;
import play.jobs.Job;

import java.util.List;

/**
 * @author <a href="mailto:alexander.hanschke@gmail.com">Alexander Hanschke</a>
 */
public class StatusHealer {

    private StatusHealer() { }

    private static final StatusHealer instance = new StatusHealer();

    public static StatusHealer get() {
        return instance;
    }

    public void heal(Long id) {
        User user = User.findById(id);

        if (user.needsRage() && !user.rageJobRunning) {
            new RageHealer(user.id).in(user.rageCooldown);
            user.rageJobRunning = Boolean.TRUE;
            user.save();
        }

        if (user.needsBlood() && !user.bloodJobRunning) {
            new BloodHealer(user.id).in(user.bloodCooldown);
            user.bloodJobRunning = Boolean.TRUE;
            user.save();
        }

        if (user.needsHealth() && !user.healthJobRunning) {
            new HealthHealer(user.id).in(user.healthCooldown);
            user.healthJobRunning = Boolean.TRUE;
            user.save();
        }
    }
}
