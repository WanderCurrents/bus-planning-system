//   Geo Class
//----------------------
//Description: static class that handles geographic related calculations
//Attributes: n/a
//Methods:	calcDist(latitude1 : double, longitude1 : double, latitude2 : double, longitude2 : double) : double
//			calcHeading(latitude1 : double, longitude1 : double, latitude2 : double, longitude2 : double) : double
//			calcTime(startStation : Station, endStation : Station, bus : Bus) : double

package model;


public class Geo 
{
	//Method for calculating the distance between two points in coordinates
	public static double calcDist(double latitude1, double longitude1, double latitude2, double longitude2) 
	{
		//Computer the raw differences in degrees
		double dLat = latitude2 - latitude1;
		double dLon = longitude2 - longitude1;

		//Convert degrees of longitude to miles
		double latAvg = (latitude1 + latitude2) / 2.0;
		double milesPerLon = 69.0 * Math.cos(Math.toRadians(latAvg));	//69 * shrinking factor which is cos(latitude)
		double x = dLon * milesPerLon;

		
		//Convert degrees of latitude into miles
		double y = dLat * 69.0;	//69 because 69 miles anywhere on earth is one degree of latitude roughly

		double distance = Math.sqrt(x*x + y*y);	//Treat the result like a right triangle
		return distance;
	}
	
	//Method for calculating the heading between two points in coordinates
	public static double calcHeading(double latitude1, double longitude1, double latitude2, double longitude2)
	{		
		double differenceLon = longitude2 - longitude1;
		double differenceLat = latitude2 - latitude1; 

	    // Convert degrees to miles
	    double latitudeAvg = (latitude1 + latitude2) / 2.0;
	    double milesPerLong = 69.0 * Math.cos(Math.toRadians(latitudeAvg));

	    double uLong = differenceLon * milesPerLong;
	    double uLat = differenceLat * 69.0;  
		
	    double cosVal = (uLat/(Math.sqrt(uLong * uLong + uLat *uLat)));	//This calculates the cosine value
	    
	    double theta = Math.toDegrees(Math.acos(cosVal));	//This calculates theta (cosine inverse of cosine value)
	    
	    //Checks if to other side of north, subtract 360
	    double headingDegrees;
	    if(uLong >= 0) //X is right of North
	    {
	    	headingDegrees = theta;
	    }
	    else //X is left of North
	    {
	    	headingDegrees = 360-theta;
	    }
	    
	    return headingDegrees;
	}
	
	//"If you could pull the lever to carry on forever / Would your life even matter anymore?" - The Rare Occasions ("Notion")
	
	//Method for calculating time from one station to another
	public static double calcTime(Station startStation, Station endStation, Bus bus)
	{
		//Get the distance between the two stations
		double distance = calcDist(startStation.getLatitude(), startStation.getLongitude(), endStation.getLatitude(), endStation.getLongitude());
		
		//Use the distance between the two stations and the bus's cruise speed to calculate the time
		double time = (distance/bus.getCruiseSpeed());
		
		return time;
	}
}

