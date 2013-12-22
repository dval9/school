package assign4;
import java.util.ArrayList;

/*
 * Contains a list of stations, with the distance between each station.
 */
public class StationList {
	private ArrayList<Station> stations=new ArrayList<Station>();
	
	/*
	 * Contains station name, and distance to other stations.
	 */
	public class Station {
		private String name;
		private double distance=0;
		private ArrayList<Station> distances=new ArrayList<Station>();
		
		Station(String name){
			this.name=name;
		}
		
		Station(String name,double distance){
			this.name=name;
			this.distance=distance;
		}
		
		public String getName(){
			return name;
		}
		
		public void setName(String name){
			this.name=name;
		}
		
		public void setDistance(double distance){
			this.distance=distance;
		}
	}
	
	public void setDistance(String station1,String station2,double distance){
		for(int i=0;i<stations.size();i++){
			boolean flag=false;
			if(stations.get(i).name.equals(station1)){
				for(int j=0;j<stations.get(i).distances.size();j++){
					if(stations.get(i).distances.get(j).name.equals(station2)){
						stations.get(i).distances.get(j).setDistance(distance);
						j=stations.get(i).distances.size();
						flag=true;
					}
				}
				if(!flag)
					stations.get(i).distances.add(new Station(station2,distance));
				i=stations.size();
			}
		}
		for(int i=0;i<stations.size();i++){
			if(stations.get(i).name.equals(station2)){
				for(int j=0;j<stations.get(i).distances.size();j++){
					if(stations.get(i).distances.get(j).name.equals(station1)){
						stations.get(i).distances.get(j).setDistance(distance);
						return;
					}
				}			
				stations.get(i).distances.add(new Station(station1,distance));
				return;
			}
		}
	}
	
	public double getDistance(String station1,String station2){
		for(int i=0;i<stations.size();i++){
			if(stations.get(i).name.equals(station1)){
				for(int j=0;j<stations.get(i).distances.size();j++){
					if(stations.get(i).distances.get(j).name.equals(station2))
						return stations.get(i).distances.get(j).distance;
				}
			}
		}
		return -1;
	}
	
	//adds a station to the list of stations
	public void addStation(String str){
		stations.add(new Station(str));
	}
	
	//removes a station
	public boolean removeStation(String str){
		for(int i=0;i<stations.size();i++){
			if(stations.get(i).name.equals(str)){
				stations.remove(stations.get(i));
				return true;
			}
		}
		return false;
	}
	
	//renames a station
	public void renameStation(String original,String newName){
		for(int i=0;i<stations.size();i++){
			if(stations.get(i).name.equals(original)){
				stations.get(i).setName(newName);
				return;
			}
		}
	}
	
	//returns all stations, for the browse use case
	public Object[] getStations(){
		return (Object[])stations.toArray();
	}
	
	//returns a subset of stations, that start with the specified string
	public Object[] getStations(String str){
		ArrayList<Station> temp=new ArrayList<Station>();
		for(int i=0;i<stations.size();i++){
			if(stations.get(i).name.startsWith(str))
				temp.add(stations.get(i));
		}
		return (Object[])temp.toArray();
	}	
}
