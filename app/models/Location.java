package models;

/**
 * @author <a href="mailto:alexander.hanschke@gmail.com">Alexander Hanschke</a>
 * @see    <a href="http://janmatuschek.de/LatitudeLongitudeBoundingCoordinates">
 *           Finding Points Within a Distance of a Latitude/Longitude Using Bounding Coordinates
 *         </a>
 */
public class Location {
	
	public Double latitude;
	public Double longitude;
	
	public static final Double EARTH_RADIUS_KM = 6371.01;
	
	private static final double MIN_LAT = Math.toRadians(-90d);  // -PI/2
	private static final double MAX_LAT = Math.toRadians(90d);   //  PI/2
	private static final double MIN_LON = Math.toRadians(-180d); // -PI
	private static final double MAX_LON = Math.toRadians(180d);  //  PI
	
	public static Location fromDegrees(Double latitude, Double longitude) {
		return new Location(Math.toRadians(latitude), Math.toRadians(longitude));
	}
	
	public static Location fromRadians(Double latitude, Double longitude) {
		return new Location(latitude, longitude);
	}
	
	private Location(Double latitude, Double longitude) {
		this.latitude  = latitude;
		this.longitude = longitude;
	}
	
	public Double distanceTo(Location other) {
        return haversineDistance(this, other);
	}
	
	public static Double distanceBetween(Location from, Location to) {
		return haversineDistance(from, to);
	}
	
	public Location[] boundingCoordinates(Double distance) {
		Double angularDistance = distance / EARTH_RADIUS_KM;
		
		Double minLatitude = latitude - angularDistance;
		Double maxLatitude = latitude + angularDistance;
		
		Double minLongitude, maxLongitude;
		if (minLatitude > MIN_LAT && maxLatitude < MAX_LAT) {
			Double deltaLongitude = Math.asin(Math.sin(angularDistance) / Math.cos(latitude));
			minLongitude = longitude - deltaLongitude;
			if (minLongitude < MIN_LON) {
				minLongitude += 2d * Math.PI;
			}
			maxLongitude = longitude + deltaLongitude;
			if (maxLongitude > MAX_LON) {
				maxLongitude -= 2d * Math.PI;
			}
		} else {
			minLatitude = Math.max(minLatitude, MIN_LAT);
			maxLatitude = Math.min(maxLatitude, MAX_LAT);
			minLongitude = MIN_LON;
			maxLongitude = MAX_LON;
		}
		
		return new Location[]{fromRadians(minLatitude, minLongitude), fromRadians(maxLatitude, maxLongitude)};
	}
	
	public static Double haversineDistance(Location from, Location to) {
		double lat1 = from.latitude;
		double lat2 = to.latitude;
		double lon1 = from.longitude;
		double lon2 = to.longitude;
		
		double lat = Math.toRadians(lat2 - lat1);
		double lon = Math.toRadians(lon2 - lon1);
		
		double a = Math.sin(lat/2) * Math.sin(lat/2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(lon/2) * Math.sin(lon/2);
		double c = 2 * Math.asin(Math.sqrt(a));
		
		return EARTH_RADIUS_KM * c;
	}
	
	@Override
	public String toString() {
		return String.format("{latitude: %s (degree: %s), longitude: %s (degree: %s)}",
			latitude, Math.toDegrees(latitude), longitude, Math.toDegrees(longitude));
	}

}
