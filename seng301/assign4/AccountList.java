package assign4;
import java.util.ArrayList;

public class AccountList {
		private ArrayList<Account> accounts=new ArrayList<Account>();
		
		public class Account {
			private String name;
			private String password;
			
			Account(String name,String password){
				this.name=name;
				this.password=password;
			}
		}
		
		//adds an account to the list of accounts
		public void addAccount(String name,String password){
			accounts.add(new Account(name,password));
		}
				
		//returns true if account exists, false otherwise
		public boolean verifyLogin(String name,String password){
			for(int i=0;i<accounts.size();i++){
				if(accounts.get(i).name.equals(name)){
					if(accounts.get(i).password.equals(password))
						return true;
				}						
			}
			return false;
		}	
}
