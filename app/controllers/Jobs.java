package controllers;

import events.Event;
import jobs.StatusHealer;
import models.Job;
import models.Queues;
import models.User;
import play.Logger;
import play.mvc.Before;
import play.mvc.With;

/**
 * @author <a href="mailto:alexander.hanschke@gmail.com">Alexander Hanschke</a>
 */
public class Jobs extends CRUD {

    @Before
    static void before() {
        if (!Security.isConnected()) {
            Game.index();
        }

        if (Game.player().logout) {
            Game.player().logout = Boolean.FALSE;
            Game.player().save();

            Security.logout();
        }
    }
	
	public static void perform(Job job) {
		User player = Game.player();
		job = Job.findById(job.id);

		if (player.blood < job.blood) {
			flash.error("You don't have enough blood!");
			flash.keep();
			Game.jobs();
		}
		
		long level = player.level;
		player.perform(job);
		player.save();

        StatusHealer.get().heal(player.id);
		
		if (player.level > level) {
			flash.put("info", "Level up!");
		}
		
		flash.success("Job completed: %s", job.name);
		Game.jobs();
	}

}
