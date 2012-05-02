package events;

import models.User;

/**
 * @author <a href="mailto:alexander.hanschke@gmail.com">Alexander Hanschke</a>
 */
public class Attack extends Event {

    public User offender;
    public User defender;

    public Attack(User offender, User defender) {
        this.offender = offender;
        this.defender = defender;
    }

    @Override
    public String toJSON() {
        String template = "{\"timestamp\":\"%s\",\"type\":\"attack\",\"offender\":\"%s\",\"defender\":\"%s\"}";
        return String.format(template, timestamp, offender, defender);
    }
}
