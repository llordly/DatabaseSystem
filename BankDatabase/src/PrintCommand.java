
public class PrintCommand {

	String returnPrev = "0. Return to previous";
	String input = "Input: ";
	
	// print who want to do
	public void printWorkList() {
		System.out.println("0. Exit");
		System.out.println("1. Administrator");
		System.out.println("2. Client");
		System.out.println(input);
	}

	// print administrator work
	public void printAdminList() {
		System.out.println(returnPrev);
		System.out.println("1. Branch information");
		System.out.println("2. Admin Client Account");
		System.out.println(input);
	}

	// print detail work of administrator
	public void printAdminAccount() {
		System.out.println(returnPrev);
		System.out.println("1. Delete Client Account");
		System.out.println("2. Add Client Account");
		System.out.println("3. show Client Account");
		System.out.println(input);
	}

	// print request of client
	public void printclientList() {
		System.out.println(returnPrev);
		System.out.println("1. show my information");
		System.out.println("2. show my account");
		System.out.println("3. show my card");
		System.out.println("4. do with money");
		System.out.println("5. loan service");
		System.out.println(input);
	}

	// print list of activity associated with money
	public void printMoneyList() {
		System.out.println(returnPrev);
		System.out.println("1. deposit");
		System.out.println("2. withdraw");
		System.out.println("3. remittance");
		System.out.println(input);
	}

	// print list of loan
	public void printLoanList() {
		System.out.println(returnPrev);
		System.out.println("1. show loan information");
		System.out.println("2. get loan");
		System.out.println(input);
	}
}
