/*CPSC 449 Project- Java 
 *October 10, 2013
 *Group: Mac Haffey, Tom Crowfoot, Christian Daniel, Sukhdeep Bratch, Subhodeep Ray-Chaudhuri
 */

//Io class
import java.io.*;
import java.util.ArrayList;

public class io{

    // write final solution in appropiate format
    void write_file(String file_name, Datastruct data){
	try{
	    //write solution array into scheduled_tasks list, because i dont feel like changing this code
	    data.scheduled_tasks= new ArrayList<Datastruct.mach_task_tuple>();
	    for(int i=0; i<8; i++){
		Datastruct.scheduled_tasks.add(data.solution[i]);
	    }
	    BufferedWriter br = new BufferedWriter(new FileWriter(file_name));
	    char[] solution = new char[8];
	    if(Datastruct.scheduled_tasks.size() != 8){// since the list doesnt have 8 things, we must not have found solution, or something
		br.write("No valid solution possible!",0,27);
		//System.out.println("\n\n\nNo valid solution possible!");
		br.close();
		System.exit(1);
	    }
	    for(Datastruct.mach_task_tuple n: Datastruct.scheduled_tasks){
		if(n==null){
		    br.write("No valid solution possible!",0,27);
		    //System.out.println("\n\n\nNo valid solution possible!");
		    br.close();
		    System.exit(1);
		}
	    }
	    for(Datastruct.mach_task_tuple n:Datastruct.scheduled_tasks){// fill out an array in sorted order with solutions
		if(n.getMach() == '1')
		    solution[0]=(char)n.getTask();
		if(n.getMach() == '2')
		    solution[1]=(char)n.getTask();
		if(n.getMach() == '3')
		    solution[2]=(char)n.getTask();
		if(n.getMach() == '4')
		    solution[3]=(char)n.getTask();
		if(n.getMach() == '5')
		    solution[4]=(char)n.getTask();
		if(n.getMach() == '6')
		    solution[5]=(char)n.getTask();
		if(n.getMach() == '7')
		    solution[6]=(char)n.getTask();
		if(n.getMach() == '8')
		    solution[7]=(char)n.getTask();
	    }
	    String sol = "Solution "+solution[0]+" "+solution[1]+" "+solution[2]+" "+solution[3]+" "+solution[4]+" "
		+solution[5]+" "+solution[6]+" "+solution[7]+"; Quality:"+data.best_pen;
	    br.write(sol,0,sol.length());
	    //System.out.println("\n\n\n"+sol);
	    br.close();
	    //"Solution" *task*1 *task*2 *task*3 *task*4 *task*5 *task*6 *task*7 *task*8"; Quality:" *qual*
	}catch(Exception e){
	    System.out.println(e);
	    e.printStackTrace();
	    //shouldnt happen
	}
    }

    // read file given at command line to structures in main
    // handles errors: partial assignment error, invalid machine/task, machine penalty error, invalid task, invalid penalty, error while parsing input file
    void read_file(String file_name, Datastruct data, String out_name){
	try{
	    BufferedReader br = new BufferedReader(new FileReader(file_name));
	    BufferedWriter brr = new BufferedWriter(new FileWriter(out_name));
	    String line_read;//initial line read
	    String[] line_split;//line after split
	    int state=0;//0=nothing, 1=forced partial assignment, 2=forbidden machine, 3=too-near tasks, 4=machine penalties, 5=too-near penalities
	    boolean done1 = false;// make sure we read all sections, and only once
	    boolean done2 = false;
	    boolean done3 = false;
	    boolean done4 = false;
	    boolean done5 = false;
		boolean hotfix = false;
		boolean read_title = false;
	    int machine_penalties = 0;//counting the number of machine penalty lines
	    while((line_read = br.readLine()) != null){//read a line
		if(line_read.trim().equals("forced partial assignment:")){
		    if(done1 == true){//make sure hasnt been done before
			//System.out.println("Error while parsing input file");
			brr.write("Error while parsing input file\n",0,31);
			brr.close();
			System.exit(1);	
		    }
		    state = 1;//set state to read assumed input hopefully
		    done1 = true;
		    continue;//start loop over so read next line
		}if(line_read.trim().equals("forbidden machine:")){
		    if(done2 == true){
			//System.out.println("Error while parsing input file");
			brr.write("Error while parsing input file\n",0,31);
			brr.close();
			System.exit(1);	
		    }
		    state = 2;
		    done2 = true;
		    continue;
		}if(line_read.trim().equals("too-near tasks:")){
		    if(done3 == true){
			//System.out.println("Error while parsing input file");
			brr.write("Error while parsing input file\n",0,31);
			brr.close();
			System.exit(1);	
		    }
		    state = 3;
		    done3 = true;
		    continue;
		}if(line_read.trim().equals("machine penalties:")){
		    if(done4 == true){
			//System.out.println("Error while parsing input file");
			brr.write("Error while parsing input file\n",0,31);
			brr.close();
			System.exit(1);	
		    }
		    state = 4;
		    done4 = true;
		    continue;
		}if(line_read.trim().equals("too-near penalities")){
		    if(done5 == true){
			//System.out.println("Error while parsing input file");
			brr.write("Error while parsing input file\n",0,31);
			brr.close();
			System.exit(1);	
		    }
		    state = 5;
		    done5 = true;
		    continue;
		}// setting correct state done, get ready to read values into structs
		line_split=line_read.split("[ ,()\t\n\r]+"); // remove crap from the line
		if(line_split.length == 0){
			line_split = new String[1];
			line_split[0] = "";
		}
		// System.out.println("|"+line_read+"|");
		// System.out.println(line_split.length);
		// for(int z=0;z<line_split.length;z++)
			// System.out.println("TEST"+line_split[z]);
		if(state == 0){
			if(line_read.trim().equals("Name:")){
				continue;
			}
			else if(read_title == false && !line_read.trim().equals("")){
				read_title = true;
				continue;
			}
			else if(read_title == true && !line_read.trim().equals("")){
				// System.out.println("Error while parsing input file");
				brr.write("Error while parsing input file\n",0,31);
				brr.close();
				System.exit(1);	
			}
		}
		if(state == 1){
		    if(line_split.length == 1){//blank line, skip
			if(!line_split[0].equals("")){
			    //System.out.println("Error while parsing input file");
			    brr.write("Error while parsing input file\n",0,31);
			    brr.close();
			    System.exit(1);	
			}
			continue;
		    }
		    if(line_split.length != 3){//making sure input makes sense
			//System.out.println("Error while parsing input file");
			brr.write("Error while parsing input file\n",0,31);
			brr.close();
			System.exit(1);	
		    }
			boolean addin=true;
			for(Datastruct.mach_task_tuple n:Datastruct.forced_partial_assignment){
				if(n.getMach()==line_split[1].charAt(0) && n.getTask()==line_split[2].charAt(0)){
					addin=false;
				}
			}
			if(addin){
		    Datastruct.forced_partial_assignment.add(data.make_mtt(line_split[1].charAt(0),line_split[2].charAt(0)));
		    Datastruct.scheduled_tasks.add(data.make_mtt(line_split[1].charAt(0),line_split[2].charAt(0)));//adding final solutions
			}
			addin=true;
		}else if(state == 2){
		    if(line_split.length == 1){//blank line, skip
			if(!line_split[0].equals("")){
			    //System.out.println("Error while parsing input file");
			    brr.write("Error while parsing input file\n",0,31);
			    brr.close();
			    System.exit(1);	
			}
			continue;
		    }
		    if(line_split.length != 3){
			//System.out.println("Error while parsing input file");
			brr.write("Error while parsing input file\n",0,31);
			brr.close();
			System.exit(1);	
		    }
		    Datastruct.forbidden_machine.add(data.make_mtt(line_split[1].charAt(0),line_split[2].charAt(0)));
		}else if(state == 3){
		    if(line_split.length == 1){//blank line, skip
			if(!line_split[0].equals("")){
			    //System.out.println("Error while parsing input file");
			    brr.write("Error while parsing input file\n",0,31);
			    brr.close();
			    System.exit(1);	
			}
			continue;
		    }
		    if(line_split.length != 3){
			//System.out.println("Error while parsing input file");
			brr.write("Error while parsing input file\n",0,31);
			brr.close();
			System.exit(1);	
		    }
		    Datastruct.too_near_tasks.add(data.make_ttt(line_split[1].charAt(0),line_split[2].charAt(0)));
		}else if(state == 4){
		    if(line_split.length == 1){//blank line, skip
			if(!line_split[0].equals("")){
			    //System.out.println("Error while parsing input file");
			    brr.write("Error while parsing input file\n",0,31);
			    brr.close();
			    System.exit(1);	
			}
			continue;
		    }
		    // System.out.println(line_split.length);
			boolean flag = true;
			if(line_split.length == 9)
				flag=false;
			if(line_split.length != 8 && flag){
				if(line_split[0].equals("too-near")){
					//System.out.println("Error while parsing input file");
					brr.write("Error while parsing input file\n",0,31);
					brr.close();
					System.exit(1);	
				}
				//System.out.println("machine penalty error");
				brr.write("machine penalty error\n",0,22);
				brr.close();
				System.exit(1);	
		    }
			int i=0;
			int end=8;
			int dec = 0;
			if(line_split.length == 9){
				i=1;
				end=9;
				dec=1;
			}
		    for(; i<end; i++){
			if(machine_penalties == 8){
				// System.out.println("machine penalty error");
				brr.write("machine penalty error\n",0,22);
				brr.close();
				System.exit(1);	
			}
			for (int j=0; j<line_split[i].length(); j++)
			{
				if ((int)line_split[i].charAt(j) < 48 || (int)line_split[i].charAt(j)> 57)
				{
					hotfix = true;
				}
			}
			if(hotfix == true || Integer.parseInt(line_split[i])<0){// need natural numbers
			    //System.out.println("invalid penalty");
			    brr.write("invalid penalty\n",0,16);
			    brr.close();
			    System.exit(1);
			}
			Datastruct.machine_penalties[machine_penalties][i-dec]=Integer.parseInt(line_split[i]);
		    }
		    machine_penalties++;
		}else if(state == 5){
		    if(line_split.length == 1){//blank line, skip
			if(!line_split[0].equals("")){
			    //System.out.println("Error while parsing input file");
			    brr.write("Error while parsing input file\n",0,31);
			    brr.close();
			    System.exit(1);	
			}
			continue;
		    }
		    if(line_split.length != 4){
			//System.out.println("Error while parsing input file");
			brr.write("Error while parsing input file\n",0,31);
			brr.close();
			System.exit(1);	
		    }
			for (int j=0; j<line_split[3].length(); j++)
			{
				if ((int)line_split[3].charAt(j) < 48 || (int)line_split[3].charAt(j)> 57)
				{
					hotfix = true;
				}
			}
		    if(hotfix == true || Integer.parseInt(line_split[3])<0){
			//System.out.println("invalid penalty");
			brr.write("invalid penalty\n",0,16);
			brr.close();
			System.exit(1);
		    }
		    Datastruct.too_near_penalties.add(data.make_ttt(line_split[1].charAt(0),line_split[2].charAt(0),Integer.parseInt(line_split[3])));
		}
	    }
	    br.close();
	    //check that there is only valid entries
	    if(!(done1&&done2&&done3&&done4&&done5)){//if we didnt find all 5 catagories
		//System.out.println("Error while parsing input file");
		brr.write("Error while parsing input file\n",0,31);
		brr.close();
		System.exit(1);
	    }
	    if(machine_penalties != 8){//incorrect # of lines in machine penalties
		//System.out.println("machine penalty error");
		brr.write("machine penalty error\n",0,22);
		brr.close();
		System.exit(1);
	    }
	    {//checking forced assignments
		String mach_used="";
		String tasks_used="";
		for(Datastruct.mach_task_tuple n:Datastruct.forced_partial_assignment){
		    if(mach_used.contains(Integer.toString(n.getMach())) || tasks_used.contains(Integer.toString(n.getTask()))){
			//System.out.println("partial assignment error");
			brr.write("partial assignment error\n",0,25);
			brr.close();
			System.exit(1);
		    }else{
			mach_used+=n.getMach();
			tasks_used+=n.getTask();
		    }
		}
	    }
	    {//check that forced and forbidden dont conflict
		for(Datastruct.mach_task_tuple n:Datastruct.forced_partial_assignment){
		    for(Datastruct.mach_task_tuple m:Datastruct.forbidden_machine){
			if(n.getMach()==m.getMach() && n.getTask()==m.getTask()){
			    
			    brr.write("No valid solution possible!",0,27);
			    //System.out.println("\n\n\nNo valid solution possible!");
			    brr.close();
			    System.exit(1);
			}
		    }
		}
	    }
	    {//checking that forced and too-near tasks dont conflict
		for(Datastruct.task_task_tuple n:Datastruct.too_near_tasks){
		    int mach1 = '0';
		    int mach2 = '0';
		    for(Datastruct.mach_task_tuple m:Datastruct.forced_partial_assignment){
			if(n.getTask1() == m.getTask())
			    mach1 = m.getMach();
			if(n.getTask2() == m.getTask())
			    mach2 = m.getMach();
		    }
		    if((mach1 + 1 == mach2) || (mach1 == '8' && mach2 == '1')){
			brr.write("No valid solution possible!",0,27);
			//System.out.println("\n\n\nNo valid solution possible!");
			brr.close();
			System.exit(1);
		    }
		}
	    }
	    //checking that values are possible, ie 1-8 and A-H only
	    for(Datastruct.mach_task_tuple n:Datastruct.forced_partial_assignment){
		if(!(n.getMach()=='1'||n.getMach()=='2'||n.getMach()=='3'||n.getMach()=='4'
		     ||n.getMach()=='5'||n.getMach()=='6'||n.getMach()=='7'||n.getMach()=='8')){
		    //System.out.println("invalid machine/task");
		    brr.write("invalid machine/task\n",0,21);
		    brr.close();
		    System.exit(1);
		}
		if(!(n.getTask()=='A'||n.getTask()=='B'||n.getTask()=='C'||n.getTask()=='D'
		     ||n.getTask()=='E'||n.getTask()=='F'||n.getTask()=='G'||n.getTask()=='H')){
		    //System.out.println("invalid machine/task");
		    brr.write("invalid machine/task\n",0,21);
		    brr.close();
		    System.exit(1);
		}
	    }
	    for(Datastruct.mach_task_tuple n:Datastruct.forbidden_machine){
		if(!(n.getMach()=='1'||n.getMach()=='2'||n.getMach()=='3'||n.getMach()=='4'
		     ||n.getMach()=='5'||n.getMach()=='6'||n.getMach()=='7'||n.getMach()=='8')){
		    //System.out.println("invalid machine/task");
		    brr.write("invalid machine/task\n",0,21);
		    brr.close();
		    System.exit(1);
		}
		if(!(n.getTask()=='A'||n.getTask()=='B'||n.getTask()=='C'||n.getTask()=='D'
		     ||n.getTask()=='E'||n.getTask()=='F'||n.getTask()=='G'||n.getTask()=='H')){
		    //System.out.println("invalid machine/task");
		    brr.write("invalid machine/task\n",0,21);
		    brr.close();
		    System.exit(1);
		}
	    }
	    for(Datastruct.task_task_tuple n:Datastruct.too_near_tasks){
		if(!(n.getTask1()=='A'||n.getTask1()=='B'||n.getTask1()=='C'||n.getTask1()=='D'
		     ||n.getTask1()=='E'||n.getTask1()=='F'||n.getTask1()=='G'||n.getTask1()=='H')){
		    //System.out.println("invalid machine/task");
		    brr.write("invalid machine/task\n",0,21);
		    brr.close();
		    System.exit(1);
		}
		if(!(n.getTask2()=='A'||n.getTask2()=='B'||n.getTask2()=='C'||n.getTask2()=='D'
		     ||n.getTask2()=='E'||n.getTask2()=='F'||n.getTask2()=='G'||n.getTask2()=='H')){
		    //System.out.println("invalid machine/task");
		    brr.write("invalid machine/task\n",0,21);
		    brr.close();
		    System.exit(1);
		}
	    }
	    for(Datastruct.task_task_tuple n:Datastruct.too_near_penalties){
		if(!(n.getTask1()=='A'||n.getTask1()=='B'||n.getTask1()=='C'||n.getTask1()=='D'
		     ||n.getTask1()=='E'||n.getTask1()=='F'||n.getTask1()=='G'||n.getTask1()=='H')){
		    //System.out.println("invalid task");
		    brr.write("invalid task\n",0,13);
		    brr.close();
		    System.exit(1);
		}
		if(!(n.getTask2()=='A'||n.getTask2()=='B'||n.getTask2()=='C'||n.getTask2()=='D'
		     ||n.getTask2()=='E'||n.getTask2()=='F'||n.getTask2()=='G'||n.getTask2()=='H')){
		    //System.out.println("invalid task");
		    brr.write("invalid task\n",0,13);
		    brr.close();
		    System.exit(1);
		}
	    }			
	}catch(Exception e){
	    //System.out.println("Error while parsing input file");
	    /*BufferedWriter brr = new BufferedWriter(new FileWriter(out_name));
	    brr.write("Error while parsing input file\n",0,31);
	    brr.close();*/
	    System.out.println("something wrong");
	    System.exit(1);
	}
    }
}














