import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class RunBank {
	
	private Connection connection;
	PrintCommand pc;
	Scanner keyboard;
	int command;
	Administrator admin;
	User user;
	
	public RunBank(Connection connection) {
		this.connection = connection;
		pc = new PrintCommand();
		keyboard = new Scanner(System.in);
	}
	
	public void run() throws SQLException {
		pc.printWorkList();
		
		while(true) {
			command = Integer.parseInt(keyboard.nextLine());
			switch (command) {
			case 0:
				return;
			// case of administrator
			case 1:
				System.out.println("type your ssn");
				String admSsn = keyboard.nextLine().trim();
				admin = new Administrator(connection, admSsn);
				
				if (!admin.checkAdmin()) break;
				
				while(true) {
					pc.printAdminList();
					command = Integer.parseInt(keyboard.nextLine());
					switch (command) {
					case 0:
						break;
					case 1:
						admin.printBranchInfo();
						break;
					case 2:
						pc.printAdminAccount();
						command = Integer.parseInt(keyboard.nextLine());
						System.out.println("type user ssn");
						String usrSsn = keyboard.nextLine().trim();
						user = new User(connection, usrSsn);
						
						if (!user.checkUser()) break;
						switch(command) {
						case 0:
							break;
						case 1:
							System.out.println("type client's account");
							String usrAccount = keyboard.nextLine().trim();
							admin.checkAccount(usrAccount);
							admin.deleteAccount(usrAccount);
							break;
						case 2:
							admin.addAccount(keyboard);
							break;
						case 3:
							admin.showClientAccount(usrSsn);
							break;
						}
						break;
					}	
				}
			case 2:
				System.out.println("type your ssn");
				String usrSsn = keyboard.nextLine().trim();
				user = new User(connection, usrSsn);
				
				if (!user.checkUser()) break;
				
				while(true) {
					pc.printclientList();
					command = Integer.parseInt(keyboard.nextLine());
					switch (command) {
					case 0:
						break;
					case 1:
						user.showMyInfo();
						break;
					case 2:
						user.showMyAccount();
						break;
					case 3:
						user.showMyCard();
						break;
					case 4:
						// do with money
						System.out.println("Type your account");
						String usrAccount = keyboard.nextLine().trim();
						
						if(!user.checkAccount(usrAccount)) break;
						
						pc.printMoneyList();
						command = Integer.parseInt(keyboard.nextLine());
						switch (command) {
						case 0:
							break;
						case 1:
							System.out.println("Type amount of deposit");
							int depositAmount = Integer.parseInt(keyboard.nextLine());
							user.deposit(depositAmount, usrAccount);
							break;
						case 2:
							System.out.println("Type amount of withdraw");
							int withdrawAmount = Integer.parseInt(keyboard.nextLine());
							user.deposit(withdrawAmount, usrAccount);
							break;
						case 3:
							System.out.println("Type amount of remittance");
							int remittanceAmount = Integer.parseInt(keyboard.nextLine());
							System.out.println("Type account number of other person");
							String toAccount = keyboard.nextLine().trim();
							user.remittance(remittanceAmount, usrAccount, toAccount);
							break;
						}
						break;
					case 5:
						command = Integer.parseInt(keyboard.nextLine());
						switch (command) {
						case 0:
							break;
						case 1:
							user.showLoanInfo();
						case 2:
							System.out.println("type amount of loan");
							int amount = Integer.parseInt(keyboard.nextLine());
							user.getLoan(amount);
						}
					}
				}
			}
		}
	}
	
}
