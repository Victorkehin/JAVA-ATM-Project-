package BCS345;

import java.util.Scanner;

public class MyATM {

	public static void main(String[] args) {
		
		//initialize Scanner 
		Scanner sc = new Scanner(System.in);
		
		//init Bank 
		Bank theBank = new Bank("Machali Bank ");
		
		// add a customer , that also creates a savings account
		Customers aCustomer = theBank.addCustomers("Vic", "Rico", "1234");
		
		//add a checking account for the customer 
		Account newAccount = new Account("Checking", aCustomer, theBank);
		aCustomer.addAccount(newAccount);
		theBank.addAccount(newAccount);
		
		 Customers curCustomers;
		 while (true) {
		 
			 //stay in login prompt until successful login
		 curCustomers = MyATM.mainMenuPrompt(theBank, sc);
		 
		 //stay in main menu until user quits 
		 MyATM.printCustomersMenu(curCustomers, sc);
		 }
	}
	
	public static Customers mainMenuPrompt(Bank theBank, Scanner sc) {
		
		//initialize
		String CustomersID; 
		String pin;
		Customers authCustomer;
		
		// Prompt user for ID/Pin combo until right one has been reached 
		do { 
			System.out.printf("\n\nWelcome to %s\n\n", theBank.getName());
			System.out.print("Enter user ID: ");
			CustomersID = sc.nextLine();
			System.out.print("Enter pin: ");
			pin = sc.nextLine();
			
			//Try to get the user object corresponding to the ID and pin combination 
			authCustomer = theBank.customerLogin(CustomersID, pin);
			if(authCustomer == null) {
				System.out.println("Incorrect user ID/pin combination. " + 
							"Please try again.");
			
			}
			
		} while(authCustomer == null); //keep looping until it is a successful login 
		
		return authCustomer;
		
	}
	
	public static void printCustomersMenu(Customers theCustomer, Scanner sc) {
		
		//Print summary of customers account 
		theCustomer.printAccountsSummary();
		
		//initialize
		int choice;
		
		//User menu 
		do {
			System.out.printf("Welcome %s, How can I help you today?\n",
				theCustomer.getFirstName());
			System.out.println(" 1) Show the account transaction history");
			System.out.println(" 2) Withdrawl");
			System.out.println(" 3) Deposit");
			System.out.println(" 4) Transfer");
			System.out.println(" 5) Quit");
			System.out.println();
			System.out.print("Enter choice: ");
			choice = sc.nextInt();
			
			if (choice < 1 || choice > 5) {
			System.out.println("Invalid choice. Please choose 1-5");
			}
			
			} while (choice < 1 || choice > 5);
	
	//Process the choice
	switch (choice) {
	
	case 1: 
		MyATM.showTransHistory(theCustomer, sc);
		break;
	case 2: 
		MyATM.withdrawlFunds(theCustomer, sc);
		break;
	case 3:
		MyATM.depositFunds(theCustomer, sc);
		break;
	case 4: 
		MyATM.transferFunds(theCustomer, sc);
		break;
	}
	
	//Redisplay this menu unless customer wants to quit 
	if (choice !=5) {
		MyATM.printCustomersMenu(theCustomer, sc);
		}
	}
	
	/**
	 * Show the transaction history for an account
	 * @parameter theCustomer the logged-in Customer object
	 * @parameter sc		the Scanner object used for user input
	 */
	public static void showTransHistory(Customers theCustomer, Scanner sc) {
		
		int theAcct;
		
		// Get the account whose transaction history to look at
		do {
			System.out.printf("Enter the number (1-%d) of the account\n" + 
		"whose transactions you want to see ",
					theCustomer.numAccounts());
			theAcct = sc.nextInt()-1;
			if (theAcct < 0 || theAcct >= theCustomer.numAccounts()) {
				System.out.println("Invalid account. Please try again.");
			}
			
		} while (theAcct >= theCustomer.numAccounts());
		
		// Print the transaction history
		theCustomer.printAcctTransHistory(theAcct); 	
	}
	
	/**
	 * Process transferring funds from one account to another
	 * @parameter theCustomer	the logged-in Customer object
	 * @parameter sc			the Scanner object used for customer input 
	 */
	public static void transferFunds(Customers theCustomer, Scanner sc) {
		
		//inits 
		int fromAcct;
		int toAcct;
		double amount;
		double acctBal;
		
		//get the account to transfer from 
		do {
			System.out.printf("Enter the number (1-%d) of the account\n" + "to transfer from: ", theCustomer.numAccounts());
			fromAcct = sc.nextInt()-1;
			if (fromAcct < 0 || fromAcct >= theCustomer.numAccounts()) {
				System.out.println("Invalid account. Please try agian.");
			}
		} while(fromAcct < 0 || fromAcct >= theCustomer.numAccounts()); 
		acctBal = theCustomer.getAcctBalance(fromAcct);
		
		// get the account to transfer to
		do {
			System.out.printf("Enter the number (1-%d) of the account\n" + "to transfer to: ");
			toAcct = sc.nextInt()-1;
			if (toAcct < 0 || toAcct >= theCustomer.numAccounts()) {
				System.out.println("Invalid account. Please try agian.");
			}
		} while(toAcct < 0 || toAcct >= theCustomer.numAccounts()); 
		
		// Get the amount to transfer
		do {
			System.out.printf("Enter the amount to transfer (max $%.02f): $", acctBal);
			amount = sc.nextDouble();
			if (amount < 0) {
				System.out.println("Amount must be greater than zero. ");
			} else if (amount > acctBal) {
				System.out.printf("Amount must not be greater than\n" + "balance of $%.02f.\n", acctBal);
			}
		} while (amount < 0 || amount > acctBal);
		
		// finally, do te transfer
		theCustomer.addAcctTransaction(fromAcct, -1*amount, String.format(
				"Transfer to account %s", theCustomer.getAcctUUID(toAcct)));
		theCustomer.addAcctTransaction(toAcct, -1*amount, String.format(
				"Transfer to account %s", theCustomer.getAcctUUID(fromAcct)));
	}
	
	/**
	 * Process a fund withdraw from an account
	 * @parameter theCustomer	the logged-in Customer object
	 * @parameter sc			the scanned object for user input
	 */
	public static void withdrawlFunds(Customers theCustomer, Scanner sc) {
		
	//initialize
			int fromAcct;
			double amount;
			double acctBal;
			String memo;
			//get the account to transfer from 
			do {
				System.out.printf("Enter the number (1-%d) of the account\n" + "to withdraw from: ",theCustomer.numAccounts());
				fromAcct = sc.nextInt()-1;
				if (fromAcct < 0 || fromAcct >= theCustomer.numAccounts()) {
					System.out.println("Invalid account. Please try agian.");
				}
			} while(fromAcct < 0 || fromAcct >= theCustomer.numAccounts()); 
			acctBal = theCustomer.getAcctBalance(fromAcct);
			
			// Get the amount to transfer
			do {
				System.out.printf("Enter the amount to withdraw (max $%.02f): $", acctBal);
				amount = sc.nextDouble();
				if (amount < 0) {
					System.out.println("Amount must be greater than zero. ");
				} else if (amount > acctBal) {
					System.out.printf("Amount must not be greater than\n" + "balance of $%.02f.\n", acctBal);
				}
			} while (amount < 0 || amount > acctBal);
			
			//Gobble the rest  of the previous input
			sc.nextLine();
			
			//get a memo
			System.out.println("Enter a memo: ");
			memo = sc.nextLine();
			
			// The withdrawl
			theCustomer.addAcctTransaction(fromAcct, -1*amount, memo);
	}
	
	/**
	 * Proces a fund deposit to account
	 * @parameter theCustomer the logged-in Customers object 
	 * @parameter sc		  the Scanner object used for user input
	 */
	public static void depositFunds(Customers theCustomer, Scanner sc) {
		//initialize
			int toAcct;
			double amount;
			double acctBal;
			String memo;
			//get the account to transfer from 
			do {
				System.out.printf("Enter the number (1-%d) of the account\n" + "to deposit in: ", theCustomer.numAccounts());
				toAcct = sc.nextInt()-1;
				if (toAcct < 0 || toAcct >= theCustomer.numAccounts()) {
					System.out.println("Invalid account. Please try agian.");
				}
			} while(toAcct < 0 || toAcct >= theCustomer.numAccounts()); 
			acctBal = theCustomer.getAcctBalance(toAcct);
			
			// Get the amount to transfer
			do {
				System.out.printf("Enter the amount to transfer (max $%.02f): $", acctBal);
				amount = sc.nextDouble();
				if (amount < 0) {
					System.out.println("Amount must be greater than zero. ");
				} 
			} while (amount < 0 );
			
			//Gobble the rest  of the previous input
			sc.nextLine();
			
			//get a memo
			System.out.println("Enter a memo: ");
			memo = sc.nextLine();
			
			// The withdrawl
			theCustomer.addAcctTransaction(toAcct, amount, memo);
	}
}

