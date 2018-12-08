
public class PrintCommand {

	String returnPrev = "0. Return to previous";
	String input = "Input: ";
	
	// print who want to do
	public void printWorkList() {
		System.out.println();
		System.out.println("0. Exit");
		System.out.println("1. Administrator");
		System.out.println("2. Client");
		System.out.println(input);
	}

	// print administrator work
	public void printAdminList() {
		System.out.println();
		System.out.println(returnPrev);
		System.out.println("1. Branch information");
		System.out.println("2. Admin Client Account");
		System.out.println(input);
	}

	// print detail work of administrator
	public void printAdminAccount() {
		System.out.println();
		System.out.println(returnPrev);
		System.out.println("1. Add Client");
		System.out.println("2. Delete Client Account");
		System.out.println("3. Show Client Account");
		System.out.println("4. Add Client Account");
		System.out.println(input);
	}

	// print request of client
	public void printclientList() {
		System.out.println();
		System.out.println(returnPrev);
		System.out.println("1. Show my information");
		System.out.println("2. Show my account");
		System.out.println("3. Show my card");
		System.out.println("4. Do with money");
		System.out.println("5. Loan service");
		System.out.println(input);
	}

	// print list of activity associated with money
	public void printMoneyList() {
		System.out.println();
		System.out.println(returnPrev);
		System.out.println("1. Deposit");
		System.out.println("2. Withdraw");
		System.out.println("3. Remittance");
		System.out.println(input);
	}

	// print list of loan
	public void printLoanList() {
		System.out.println();
		System.out.println(returnPrev);
		System.out.println("1. Show loan information");
		System.out.println("2. Get loan");
		System.out.println(input);
	}
}
