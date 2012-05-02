package jobs;

import models.User;
import play.Play;
import play.db.DB;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.libs.Crypto;

import java.util.Date;
import java.util.List;

@OnApplicationStart
public class Bootstrap extends Job {

	@Override
	public void doJob() throws Exception {
        scheduleJobs();
	}

    private void scheduleJobs() throws Exception {
        DB.getConnection().createStatement().execute("update User set rageJobRunning=0, bloodJobRunning=0, healthJobRunning=0 where true");

        List<User> users = User.findAll();
        for (User user : users) {
            StatusHealer.get().heal(user.id);
        }
    }

}
