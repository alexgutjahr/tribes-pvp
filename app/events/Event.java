package events;

/**
 * @author <a href="mailto:alexander.hanschke@gmail.com">Alexander Hanschke</a>
 */
public abstract class Event {

    public final Long timestamp;

    public Event() {
        this.timestamp = System.currentTimeMillis();
    }

    public abstract String toJSON();
}
