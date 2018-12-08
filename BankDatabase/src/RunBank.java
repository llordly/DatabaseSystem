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
		String usrAccount;
		String usrSsn;
		while(true) {
			pc.printWorkList();
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
						while(true) {
							pc.printAdminAccount();
							command = Integer.parseInt(keyboard.nextLine());
							
							switch(command) {
							case 0:
								break;
							case 1:
								try {
									admin.addClient(keyboard);
								} catch (Exception e) {
									System.out.println("User information is wrong or you didn't keep form");
								}
								break;
							case 2:
								System.out.println("type client's account");
								usrAccount = keyboard.nextLine().trim();
								if (admin.deleteAccount(usrAccount)) {
									System.out.println("Deleted");
								}
								else {
									System.out.println("Failed");
									admin.checkAccount(usrAccount);
								}
								break;
							case 3:
								System.out.println("type client's ssn");
								usrSsn = keyboard.nextLine().trim();
								if (!admin.checkUser(usrSsn)) break;
								admin.showClientAccount(usrSsn);
								break;
							case 4:
								System.out.println("type client's ssn");
								usrSsn = keyboard.nextLine().trim();
								if (!admin.checkUser(usrSsn)) break;
								System.out.println("type account type ex) Saving or Checking");
								String accountType = keyboard.nextLine().trim();
								try {
									admin.addClientAccount(usrSsn, accountType);
									System.out.println("Add client is successed");
								} catch (Exception e) {
									System.out.println("Failed for unknown reason");
								}
								break;
							}
						}
					}	
				}
			case 2:
				System.out.println("type your ssn");
				usrSsn = keyboard.nextLine().trim();
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
						usrAccount = keyboard.nextLine().trim();
						
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
