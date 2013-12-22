package assign4;

public class Hardware {
	private static String pin;
	private static double funds;
	
	public Hardware(double funds,String pin){
		Hardware.funds=funds;
		Hardware.pin=pin;
	}
	
	public boolean getInputCash(String input){
		try{
			Double.parseDouble(input);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public boolean getInputCard(){
		return true;
	}
	
	//prints tickets
	public boolean printTickets(Object[] tickets){
		return true;
	}
	
	//returns money in amount told
	public boolean returnChange(double change){
		return true;
	}
	
	//assumed to contact bank, just returns true to pretend
	public boolean checkPin(String pin){
		if(Hardware.pin.equals(pin))
			return true;
		return false;
	}
	
	//get max money to spend from bank
	public double requestFunds(double max){
		if(Hardware.funds>=max)
			return max;
		return Hardware.funds;
	}
	
	public boolean returnCard(){
		return true;
	}
}
