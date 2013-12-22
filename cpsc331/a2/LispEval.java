/**
 *Assignment 2 CPSC 331 T01
 *@author Tom Crowfoot 10037477
 */

import java.io.*;

public class LispEval{

    /**
     *Main function for LispEval program.
     *infile = input file.
     *type = 0 then array implementation, else linked list implementation.
     *@param args Program arguements -> java LispEval type infile
     */
    public static void main(String[]args){
		try{
			if(args.length!=2){
				System.out.println("Wrong args.");
				System.exit(1);
			}
			if(args[0].equals("0"))
				arrayEval(args[1]);
			else
				listEval(args[1]);
		}catch(Exception e){
			System.out.println("error main");
		}
    }

    /**
     *Calculates each expression in the input file, using an array implementation.
	 *Follows the algorithm outlined in question 1 of the assignment.
     *@param Str the name of the file.
     */
    @SuppressWarnings("unchecked")
    public static void arrayEval(String str){
		try{
			BufferedReader br=new BufferedReader(new FileReader(str));
			String eq;
			BoundedArrayStack stack;
			int i,j,k,exps;
			double[] nums;
			while(true){
				try{
					eq=br.readLine();
					stack=new BoundedArrayStack(eq.length());
					i=0;
					j=0;
					k=0;
					exps=0;
					nums=new double[eq.length()];
					while(i<eq.length()){
						stack.push(eq.charAt(i++));
						if(Character.isLetter(String.valueOf(stack.top()).charAt(0)))
							throw new InvalidExpressionException();
						else if(String.valueOf(stack.top()).equals(" ")&&i<=eq.length())
							stack.pop();
						else if(Character.isDigit(String.valueOf(stack.top()).charAt(0))&&i<eq.length()&&Character.isDigit((char)eq.charAt(i)))
							stack.push(Double.parseDouble(String.valueOf(stack.pop())+String.valueOf(eq.charAt(i))));
					}
					while(!stack.isEmpty()){
						if(String.valueOf(stack.top()).equals("("))
							throw new InvalidExpressionException();
						else if(String.valueOf(stack.top()).equals(")")){
							stack.pop();
							if(stack.isEmpty())
								throw new InvalidExpressionException();
							exps++;
						}
						else if(String.valueOf(stack.top()).equals("+")){
							stack.pop();
							if(!stack.isEmpty()){
								if(String.valueOf(stack.top()).equals("(")){
									stack.pop();
								}
								else
									throw new InvalidExpressionException();
							}
							else
								throw new InvalidExpressionException();
							double temp=0;
							if(j==k){
								if(exps!=0)
									k=0;
							}
							if(j!=k){
								while(j>k){
									j--;
									temp=temp+nums[j];
								}
							}
							nums[j]=temp;
							k++;
							j=k;
							exps--;
						}
						else if(String.valueOf(stack.top()).equals("*")){
							stack.pop();
							if(!stack.isEmpty()){
								if(String.valueOf(stack.top()).equals("(")){
									stack.pop();
								}
								else
									throw new InvalidExpressionException();
							}
							else
								throw new InvalidExpressionException();
							double temp=1;
							if(j==k){
								if(exps!=0)
									k=0;
							}
							if(j!=k){
								while(j>k){
									j--;
									temp=temp*nums[j];
								}
							}
							nums[j]=temp;
							k++;
							j=k;
							exps--;
						}
						else if(String.valueOf(stack.top()).equals("-")){
							stack.pop();
							if(!stack.isEmpty()){
								if(String.valueOf(stack.top()).equals("(")){
									stack.pop();
									exps--;
								}
								else
									throw new InvalidExpressionException();
							}
							else
								throw new InvalidExpressionException();
							if(j==k){
								if(exps!=0)
									k=0;
								else
									throw new InvalidExpressionException();
							}
							double temp=0;
							if(j==k+1){
								j--;
								temp=temp-nums[j];
							}
							else{
								j--;
								temp=nums[j];
								while(j>k){
									j--;
									temp=temp-nums[j];
								}
							}
							nums[j]=temp;
							k++;
							j=k;
						}
						else if(String.valueOf(stack.top()).equals("/")){
							stack.pop();
							if(!stack.isEmpty()){
								if(String.valueOf(stack.top()).equals("(")){
									stack.pop();
									exps--;
								}
								else
									throw new InvalidExpressionException();
							}
							else
								throw new InvalidExpressionException();
							if(j==k){
								if(exps!=0)
									k=0;
								else
									throw new InvalidExpressionException();
							}
							double temp=1;
							if(j==k+1){
								j--;
								temp=temp/nums[j];
							}
							else{
								j--;
								temp=nums[j];
								while(j>k){
									j--;
									temp=temp/nums[j];
								}
							}
							nums[j]=temp;
							k++;
							j=k;
						}
						else{
							nums[j++]=Double.parseDouble(String.valueOf(stack.pop()));
						}
					}
					if(stack.isEmpty()&&exps==0)
						System.out.println(nums[--j]);
					else
						throw new InvalidExpressionException();
				}catch(InvalidExpressionException e){
					System.out.println("Invalid Expression");
				}
			}
		}catch(Exception e){
		}
    }

    /**
     *Calculates each expression in the input file, using a linked list implementation.
	 *Follows the algorithm outlined in question 1 of the assignment.
     *@param Str the name of the file.
     */
    @SuppressWarnings("unchecked")
    public static void listEval(String str){
		try{
			BufferedReader br=new BufferedReader(new FileReader(str));
			String eq;
			BoundedLinkedListStack stack;
			int i,j,k,exps;
			double[] nums;
			while(true){
				try{
					eq=br.readLine();
					stack=new BoundedLinkedListStack(eq.length());
					i=0;
					j=0;
					k=0;
					exps=0;
					nums=new double[eq.length()];
					while(i<eq.length()){
						stack.push(eq.charAt(i++));
						if(Character.isLetter(String.valueOf(stack.top()).charAt(0)))
							throw new InvalidExpressionException();
						else if(String.valueOf(stack.top()).equals(" ")&&i<=eq.length())
							stack.pop();
						else if(Character.isDigit(String.valueOf(stack.top()).charAt(0))&&i<eq.length()&&Character.isDigit((char)eq.charAt(i)))
							stack.push(Double.parseDouble(String.valueOf(stack.pop())+String.valueOf(eq.charAt(i))));
					}
					while(!stack.isEmpty()){
						if(String.valueOf(stack.top()).equals("("))
							throw new InvalidExpressionException();
						else if(String.valueOf(stack.top()).equals(")")){
							stack.pop();
							if(stack.isEmpty())
								throw new InvalidExpressionException();
							exps++;
						}
						else if(String.valueOf(stack.top()).equals("+")){
							stack.pop();
							if(!stack.isEmpty()){
								if(String.valueOf(stack.top()).equals("(")){
									stack.pop();
								}
								else
									throw new InvalidExpressionException();
							}
							else
								throw new InvalidExpressionException();
							double temp=0;
							if(j==k){
								if(exps!=0)
									k=0;
							}
							if(j!=k){
								while(j>k){
									j--;
									temp=temp+nums[j];
								}
							}
							nums[j]=temp;
							k++;
							j=k;
							exps--;
						}
						else if(String.valueOf(stack.top()).equals("*")){
							stack.pop();
							if(!stack.isEmpty()){
								if(String.valueOf(stack.top()).equals("(")){
									stack.pop();
								}
								else
									throw new InvalidExpressionException();
							}
							else
								throw new InvalidExpressionException();
							double temp=1;
							if(j==k){
								if(exps!=0)
									k=0;
							}
							if(j!=k){
								while(j>k){
									j--;
									temp=temp*nums[j];
								}
							}
							nums[j]=temp;
							k++;
							j=k;
							exps--;
						}
						else if(String.valueOf(stack.top()).equals("-")){
							stack.pop();
							if(!stack.isEmpty()){
								if(String.valueOf(stack.top()).equals("(")){
									stack.pop();
									exps--;
								}
								else
									throw new InvalidExpressionException();
							}
							else
								throw new InvalidExpressionException();
							if(j==k){
								if(exps!=0)
									k=0;
								else
									throw new InvalidExpressionException();
							}
							double temp=0;
							if(j==k+1){
								j--;
								temp=temp-nums[j];
							}
							else{
								j--;
								temp=nums[j];
								while(j>k){
									j--;
									temp=temp-nums[j];
								}
							}
							nums[j]=temp;
							k++;
							j=k;
						}
						else if(String.valueOf(stack.top()).equals("/")){
							stack.pop();
							if(!stack.isEmpty()){
								if(String.valueOf(stack.top()).equals("(")){
									stack.pop();
									exps--;
								}
								else
									throw new InvalidExpressionException();
							}
							else
								throw new InvalidExpressionException();
							if(j==k){
								if(exps!=0)
									k=0;
								else
									throw new InvalidExpressionException();
							}
							double temp=1;
							if(j==k+1){
								j--;
								temp=temp/nums[j];
							}
							else{
								j--;
								temp=nums[j];
								while(j>k){
									j--;
									temp=temp/nums[j];
								}
							}
							nums[j]=temp;
							k++;
							j=k;
						}
						else{
							nums[j++]=Double.parseDouble(String.valueOf(stack.pop()));
						}
					}
					if(stack.isEmpty()&&exps==0)
						System.out.println(nums[--j]);
					else
						throw new InvalidExpressionException();
				}catch(InvalidExpressionException e){
					System.out.println("Invalid Expression");
				}
			}
		}catch(Exception e){
		}
	}

    /**
     *Testing method, used by LispTester class.
     *@param S, interface of type string, with methods to calculate result.
     *@param expr, the expression to evaluate.
     *@return The answer to the expression.
     */
	@SuppressWarnings("unchecked")
    public static double evaluate(BoundedStack<String> S, String expr) throws InvalidExpressionException{
		String eq=expr;
		BoundedStack stack=S;
		int i,j,k,exps;
		double[] nums;
		while(true){
			i=0;
			j=0;
			k=0;
			exps=0;
			nums=new double[eq.length()];
			while(i<eq.length()){
				stack.push(eq.charAt(i++));
				if(Character.isLetter(String.valueOf(stack.top()).charAt(0)))
					throw new InvalidExpressionException();
				else if(String.valueOf(stack.top()).equals(" ")&&i<=eq.length())
					stack.pop();
				else if(Character.isDigit(String.valueOf(stack.top()).charAt(0))&&i<eq.length()&&Character.isDigit((char)eq.charAt(i)))
					stack.push(Double.parseDouble(String.valueOf(stack.pop())+String.valueOf(eq.charAt(i))));
			}
			while(!stack.isEmpty()){
				if(String.valueOf(stack.top()).equals("("))
					throw new InvalidExpressionException();
				else if(String.valueOf(stack.top()).equals(")")){
					stack.pop();
					if(stack.isEmpty())
						throw new InvalidExpressionException();
					exps++;
				}
				else if(String.valueOf(stack.top()).equals("+")){
					stack.pop();
					if(!stack.isEmpty()){
						if(String.valueOf(stack.top()).equals("(")){
							stack.pop();
						}
						else
							throw new InvalidExpressionException();
					}
					else
						throw new InvalidExpressionException();
					double temp=0;
					if(j==k){
						if(exps!=0)
							k=0;
					}
					if(j!=k){
						while(j>k){
							j--;
							temp=temp+nums[j];
						}
					}
					nums[j]=temp;
					k++;
					j=k;
					exps--;
				}
				else if(String.valueOf(stack.top()).equals("*")){
					stack.pop();
					if(!stack.isEmpty()){
						if(String.valueOf(stack.top()).equals("(")){
							stack.pop();
						}
						else
							throw new InvalidExpressionException();
					}
					else
						throw new InvalidExpressionException();
					double temp=1;
					if(j==k){
						if(exps!=0)
							k=0;
					}
					if(j!=k){
						while(j>k){
							j--;
							temp=temp*nums[j];
						}
					}
					nums[j]=temp;
					k++;
					j=k;
					exps--;
				}
				else if(String.valueOf(stack.top()).equals("-")){
					stack.pop();
					if(!stack.isEmpty()){
						if(String.valueOf(stack.top()).equals("(")){
							stack.pop();
							exps--;
						}
						else
							throw new InvalidExpressionException();
					}
					else
						throw new InvalidExpressionException();
					if(j==k){
						if(exps!=0)
							k=0;
						else
							throw new InvalidExpressionException();
					}
					double temp=0;
					if(j==k+1){
						j--;
						temp=temp-nums[j];
					}
					else{
						j--;
						temp=nums[j];
						while(j>k){
							j--;
							temp=temp-nums[j];
						}
					}
					nums[j]=temp;
					k++;
					j=k;
				}
				else if(String.valueOf(stack.top()).equals("/")){
					stack.pop();
					if(!stack.isEmpty()){
						if(String.valueOf(stack.top()).equals("(")){
							stack.pop();
							exps--;
						}
						else
							throw new InvalidExpressionException();
					}
					else
						throw new InvalidExpressionException();
					if(j==k){
						if(exps!=0)
							k=0;
						else
							throw new InvalidExpressionException();
					}
					double temp=1;
					if(j==k+1){
						j--;
						temp=temp/nums[j];
					}
					else{
						j--;
						temp=nums[j];
						while(j>k){
							j--;
							temp=temp/nums[j];
						}
					}
					nums[j]=temp;
					k++;
					j=k;
				}
				else{
					nums[j++]=Double.parseDouble(String.valueOf(stack.pop()));
				}
			}
			if(stack.isEmpty()&&exps==0)
				return nums[--j];
			else
				throw new InvalidExpressionException();
		}
    }
}
