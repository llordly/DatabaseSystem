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
	
	public void run() throws SQLException {
		while (true) {
			pc.printWorkList();
			command = Integer.parseInt(keyboard.nextLine());
			switch (command) {
			case 0:
				System.out.println("Exit");
				return;
			// case of administrator
			case 1:
				try {
					System.out.println("type your ssn");
					String admSsn = keyboard.nextLine().trim();
					admin = new Administrator(connection, admSsn);
					if (!admin.checkAdmin())
						break;
					while(true) {
						if (!runAdmin()) break;
					}
				} catch (Exception e) {
					System.out.println("Failed for unknown reason");
				}
				break;
			case 2:
				try {
					System.out.println("type your ssn");
					String usrSsn = keyboard.nextLine().trim();
					user = new User(connection, usrSsn);
					if (!user.checkUser())
						break;
					while(true) {
						if (!runClient()) break;
					}
				} catch (Exception e) {
					System.out.println("Failed for unknown reason");
				}
				break;
			default:
				System.out.println("wrong command");
				break;
			}
		}
	}

	public RunBank(Connection connection) {
		this.connection = connection;
		pc = new PrintCommand();
		keyboard = new Scanner(System.in);
	}

	public boolean runAdmin() throws SQLException {
		pc.printAdminList();
		command = Integer.parseInt(keyboard.nextLine());
		switch (command) {
		case 0:
			return false;
		case 1:
			admin.printBranchInfo();
			break;
		case 2:
			while (true) {
				if (!runAdminClientAccount()) break;
			}
			break;
		default:
			System.out.println("wrong command");
			break;
		}
		return true;
	}

	public boolean runClient() throws SQLException {
		pc.printclientList();
		command = Integer.parseInt(keyboard.nextLine());
		switch (command) {
		case 0:
			return false;
		case 1:
			try {
				user.showMyInfo();
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Failed for unknown reason");
			}
			break;
		case 2:
			try {
				user.showMyAccount();
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Failed for unknown reason");
			}
			break;
		case 3:
			try {
				user.showMyCard();
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Failed for unknown reason");
			}
			break;
		case 4:
			// do with money
			System.out.println("Type your account");
			String usrAccount = keyboard.nextLine().trim();

			try {
				if (!user.checkAccount(usrAccount))
					break;
			} catch (Exception e) {
				System.out.println("Failed");
			}

			while (true) {
				if (!runMoney(usrAccount)) break;
			}
			break;
		case 5:
			// Loan service
			while (true) {
				if (!runLoan()) break;
			}
			break;
		default:
			System.out.println("wrong command");
		}
		return true;
	}
	
	public boolean runAdminClientAccount() throws SQLException {
		String usrAccount;
		String usrSsn;
		pc.printAdminAccount();
		command = Integer.parseInt(keyboard.nextLine());

		switch (command) {
		case 0:
			return false;
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
			try {
				if (admin.deleteAccount(usrAccount)) {
					System.out.println("Deleted");
				} else {
					System.out.println("Failed");
					admin.checkAccount(usrAccount);
				}
			} catch (Exception e) {
				System.out.println("Failed");
			}
			break;
		case 3:
			System.out.println("type client's ssn");
			usrSsn = keyboard.nextLine().trim();
			try {
				if (!admin.checkUser(usrSsn))
					break;
				admin.showClientAccount(usrSsn);
			} catch (Exception e) {
				System.out.println("Failed");
			}
			break;
		case 4:
			System.out.println("type client's ssn");
			usrSsn = keyboard.nextLine().trim();
			if (!admin.checkUser(usrSsn))
				break;
			System.out.println("type account type ex) Saving or Checking");
			String accountType = keyboard.nextLine().trim();
			try {
				admin.addClientAccount(usrSsn, accountType);
				System.out.println("Add client is successed");
			} catch (Exception e) {
				System.out.println("Failed for unknown reason");
			}
			break;
		default:
			System.out.println("wrong command");
		}
		return true;
	}
	
	public boolean runMoney(String usrAccount) throws SQLException {
		try {
			if (!user.checkAccount(usrAccount))
				return false;
		} catch (Exception e) {
			System.out.println("Failed");
		}

		pc.printMoneyList();
		command = Integer.parseInt(keyboard.nextLine());
		switch (command) {
		case 0:
			return false;
		case 1:
			System.out.println("Type amount of deposit");
			int depositAmount = Integer.parseInt(keyboard.nextLine());
			try {
				user.deposit(depositAmount, usrAccount);
				System.out.println("money of " + depositAmount + " has been deposited");
			} catch (Exception e) {
				System.out.println("deposit failed");
			}
			break;
		case 2:
			System.out.println("Type amount of withdraw");
			int withdrawAmount = Integer.parseInt(keyboard.nextLine());
			try {
				user.withdraw(withdrawAmount, usrAccount);
				System.out.println("money of " + withdrawAmount + " has been withdrawn");
			} catch (Exception e) {
				System.out.println("withdraw failed");
			}
			break;
		case 3:
			System.out.println("Type amount of remittance");
			int remittanceAmount = Integer.parseInt(keyboard.nextLine());
			System.out.println("Type account number of other person");
			String toAccount = keyboard.nextLine().trim();
			try {
				user.remittance(remittanceAmount, usrAccount, toAccount);
				System.out.println("money of " + remittanceAmount + " has been remittanced");
			} catch (Exception e) {
				System.out.println("remittance failed");
			}
			break;
		default:
			System.out.println("wrong command");
		}
		return true;
	}
	
	public boolean runLoan() throws SQLException {
		pc.printLoanList();
		command = Integer.parseInt(keyboard.nextLine());
		switch (command) {
		case 0:
			return false;
		case 1:
			try {
				user.showLoanInfo();
			} catch (Exception e) {
				System.out.println("Failed for unknown reason");
			}
			break;
		case 2:
			System.out.println("type amount of loan");
			int amount = Integer.parseInt(keyboard.nextLine());
			try {
				user.getLoan(amount);
				System.out.println("you got a loan of " + amount);
			} catch (Exception e) {
				System.out.println("Loan failed");
			}
			break;
		default:
			System.out.println("wrong command");
		}
		return true;
	}
	
}