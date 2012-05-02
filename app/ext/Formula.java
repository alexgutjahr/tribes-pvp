package ext;

/**
 * @author <a href="mailto:alexander.hanschke@gmail.com">Alexander Hanschke</a>
 */
public class Formula {
	
	public static Long levelThreshold(Long level) {
		return (long) (Math.pow(level, 2) * 0.3 + 3 * level);
	}

}
