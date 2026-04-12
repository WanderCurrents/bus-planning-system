package model;

public class Geo 
{

	public static double calcDist(double latitude1, double longitude1, double latitude2, double longitude2) 
	{
		double dLat = latitude2 - latitude1;
		double dLon = longitude2 - longitude1;

		double latAvg = (latitude1 + latitude2) / 2.0;
		double milesPerLon = 69.0 * Math.cos(Math.toRadians(latAvg));

		double x = dLon * milesPerLon;
		double y = dLat * 69.0;

		double distance = Math.sqrt(x*x + y*y);
		return distance;
	}
	
	public static double calcHeading(double longitude1, double latitude1, double longitude2, double latitude2) 
	{		
		double differenceLon = longitude2 - longitude1;
		double differenceLat = latitude2 - latitude1; 

	    // Convert degrees to miles
	    double latitudeAvg = (latitude1 + latitude2) / 2.0;
	    double milesPerLong = 69.0 * Math.cos(Math.toRadians(latitudeAvg));

	    double uLong = differenceLon * milesPerLong;  // east-west
	    double uLat = differenceLat * 69.0;  
		
	    double cosVal = (uLat/(Math.sqrt(uLong * uLong + uLat *uLat)));
	    
	    double theta = Math.toDegrees(Math.acos(cosVal));
	    
	    double headingDegrees;
	    if(uLong >= 0) //X is right of North
	    	headingDegrees = theta;
	    else //X is left of North
	    	headingDegrees = 360-theta;
	    
	    return headingDegrees;
	}
	
	public static double calcTime(Station startStation, Station endStation, Bus bus)
	{
		double distance = calcDist(startStation.getLongitude(), startStation.getLatitude(), endStation.getLongitude(), endStation.getLatitude());
		
		double time = (distance/bus.getCruiseSpeed());
		
		return time;
	}
}
