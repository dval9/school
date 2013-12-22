package assign4;
import java.util.ArrayList;

/*
 * Contains a list of available ticket classes, with the ability to add and remove.
 */
public class TicketClassList {
	private ArrayList<TicketClass> classes=new ArrayList<TicketClass>();
	
	/*
	 * A class of ticket.
	 */
	public class TicketClass {
		private String name;
		private double cost;
		
		TicketClass(String name,double cost){
			this.name=name;
			this.cost=cost;
		}
		
		public String getName(){
			return name;
		}
		
		public void setName(String name){
			this.name=name;
		}
		
		public void setCost(double cost){
			this.cost=cost;
		}
		public double getCost(){
			return cost;
		}
	}
	
	//adds a station to the list of stations
	public void addTicketClass(String name,double cost){
		classes.add(new TicketClass(name,cost));
	}
	
	public double getClassCost(String str){
		for(int i=0;i<classes.size();i++){
			if(classes.get(i).name.equals(str))
				return classes.get(i).cost;
		}
		return -1;
	}
	
	//removes a ticket class
	public boolean removeClass(String str){
		for(int i=0;i<classes.size();i++){
			if(classes.get(i).name.equals(str)){
				classes.remove(classes.get(i));
				return true;
			}
		}
		return false;
	}
	
	//rename class
	public boolean renameClass(String original,String newName){
		for(int i=0;i<classes.size();i++){
			if(classes.get(i).name.equals(original)){
				classes.get(i).setName(newName);
				return true;
			}
		}
		return false;
	}
		
	//change cost
	public boolean changeCost(String name,double cost){
		for(int i=0;i<classes.size();i++){
			if(classes.get(i).name.equals(name)){
				classes.get(i).setCost(cost);
				return true;
			}
		}
		return false;
	}
	
	
	//returns all classes
	public Object[] getTickets(){
		return (Object[])classes.toArray();
	}
}
