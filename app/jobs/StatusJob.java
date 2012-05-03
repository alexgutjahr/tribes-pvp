package jobs;

import models.User;
import play.jobs.Job;

/**
 * @author <a href="mailto:alexander.hanschke@gmail.com">Alexander Hanschke</a>
 */
public abstract class StatusJob extends Job {

    private final Long id;

    public StatusJob(Long id) {
        this.id = id;
    }

    @Override
    public final void doJob() throws Exception {
        User user = User.findById(id);

        if (needsHeal(user)) {
            heal(user);
            user.save();
        }
    }

    protected abstract boolean needsHeal(User user);

    protected abstract void heal(User user);

    @Override
    public final String toString() {
        return String.format("%s -> %s", this.getClass().getSimpleName(), id);
    }
}
