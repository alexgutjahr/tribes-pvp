package models;

import play.libs.F;
import events.Event;

/**
 * @author <a href="mailto:alexander.hanschke@gmail.com">Alexander Hanschke</a>
 */
public class Queues {
	
	private Queues() { }
	
	public static final Queues instance = new Queues();
	
	public final F.EventStream<Event> events = new F.EventStream<Event>();

}
