package controllers;

import events.Attack;
import jobs.RageHealer;
import jobs.StatusHealer;
import models.Queues;
import models.User;
import play.mvc.Before;
import play.mvc.Controller;

/**
 * @author <a href="mailto:alexander.hanschke@gmail.com">Alexander Hanschke</a>
 */
public class Users extends Controller {

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
	
	public static void healthUp() {
		if (!Game.player().hasSkillpoints()) {
			flash.error("You don't have enough skillpoints.");
			flash.keep();
			Game.vault();
		}
		
		Game.player().maxhealth++;
		Game.player().health++;
		Game.player().skillpoints--;
		Game.player().save();
		
		flash.success("Health increased!");
		
		Game.vault();
	}
	
	public static void bloodUp() {
		if (!Game.player().hasSkillpoints()) {
            flash.error("You don't have enough skillpoints.");
			flash.keep();
			Game.vault();
		}
		
		Game.player().maxblood++;
		Game.player().blood++;
		Game.player().skillpoints--;
		Game.player().save();
		
		flash.success("Blood increased!");
		
		Game.vault();
	}
	
	public static void rageUp() {
		if (!Game.player().hasSkillpoints()) {
            flash.error("You don't have enough skillpoints.");
			flash.keep();
			Game.vault();
		}
		
		Game.player().maxrage++;
		Game.player().rage++;
		Game.player().skillpoints--;
		Game.player().save();
		
		flash.success("Rage increased!");
		
		Game.vault();
	}

    public static void sightUp() {
        if (!Game.player().hasSkillpoints()) {
            flash.error("You don't have enough skillpoints.");
            flash.keep();
            Game.vault();
        }

        Game.player().sight = Game.player().sight + 0.1;
        Game.player().skillpoints--;
        Game.player().save();

        flash.success("Sight increased!");

        Game.vault();
    }

    public static void trace(Double latitude, Double longitude) {
        Game.player().location.latitude = latitude;
        Game.player().location.longitude = longitude;

        Game.player().save();
    }

    public static void attack(User user) {
        user = User.findById(user.id);

        if (Game.player().rage < 1) {
            flash.error("You don't have enough rage!");
            flash.keep();
            Game.fight();
        }

        if (user.health < 1) {
            flash.error("Enemy fainted.");
            Game.fight();
        }

        Queues.instance.events.publish(new Attack(Game.player(), user));

        long level = Game.player().level;
        Battle battle = Battle.simulate(Game.player(), user);
        Game.player().rage--;
        battle.loser.health = battle.loser.health - 10;
        if (battle.loser.health < 0) {
            battle.loser.health = Long.valueOf(0);
        }
        battle.winner.gain(battle.winner.level);

        Game.player().save();
        user.save();

        StatusHealer.get().heal(battle.winner.id);
        StatusHealer.get().heal(battle.loser.id);

        if (Game.player().level > level) {
            flash.put("info", "Level up!");
        }

        flash.success("You defeated %s and gained %s experience!", user.name, battle.winner.level);

        Game.fight();
    }

    public static class Battle {
        public User winner;
        public User loser;

        private Battle(User winner, User loser) {
            this.winner = winner;
            this.loser = loser;
        }

        public static Battle simulate(User one, User two) {
            return new Battle(one, two);
        }
    }

}
