# <center>Project 4: DBMS Program Development</center>

 

**2017029589 컴퓨터소프트웨어학부 류지범**



## #0 DB Introduction

- 은행의 DB를 관리하기 위한 DBMS이다.
- 은행의 DB는 DB Project 1, 2, 3에서 한 요구사항 분석, Er-diagram, Relational-model을 기반으로 스키마를 만들었다. 스키마는 아래의 Relational-model을 따라 만들었으며, 이 모델은 Project3에서 중복된 Attribute를 제거하고 만든 모델이다(Functional Dependency를 고려하여)
- <img src="https://github.com/llordly/DatabaseSystem/blob/master/img/schema.png?raw=true" alt="img" style="zoom:60%;" />



## #1 Program Introduction

- 이 프로그램은 관리자와 사용자 입장에서 은행의 DB를 이용하기 위한 것이다.
- 아래의 설명은 프로그램에 대한 대략적인 묘사이며 실질적인 설명은 코드와, SQL 그리고 프로그램 실행 사진을 통해 하도록 하겠다.
- 관리자는 사용자와, 사용자의 계좌를 관리할 수 있으며 이 관리에는 사용자 가입, 계좌 삭제, 계좌 추가 그리고 계좌 보기가 있다. 가입의 경우에는 관리자 권한으로 하기 위해서 사용자의 권한으로 넣지 않았다. 또한 관리자가 속해 있는 지점의 정보도 알아볼 수 있다.
- 사용자는 은행업무(예금, 출금, 송금)를 할 수 있으며 자신의 정보 보기, 대출 서비스 이용도 가능하다. 대출 서비스의 경우, 현재 자신의 대출 정보 보기와 대출받기를 제공하고 있다.



## #2 Code

- 크게 5개의 Class로 나누어 작업하였다. 프로그램을 돌리기 위한 Main, 전반적인 진행을 하기 위한 RunBank, 명령어를 설명하기 위한 PrintCommand, 관리자의 기능을 수행하기 위한 Administrator 그리고 사용자의 기능을 수행하기 위한 User가 있다.
- Main
  - driver는 mysql과 연결할 수 있도록 이름을 정해줬고, url의 경우 localhost의 3306번 포트를 통해 bank라는 DB에 접속하도록 했다.
  - DB에 접속할 UserName과 password를 받을 수 있도록 했고, connection을 통해 DB와의 연결을 설정한다.
  -  RunBank 객체를 생성하고, run()메소드를 통해 프로그램을 실제로 수행한다. 프로그램을 종료할 때는 connection을 닫아주도록 한다.

- PrintCommand
  - 프로그램이 실행되는 동안의 명령어를 제시해주기 위한 클래스

- RunBank

  - 은행 시스템의 서비스를 이용하기 위한 명령들을 수행하는 함수들로 이루어져있다.
  - `run()` - 이 함수를 통해 프로그램이 진행된다.
    - 0번은 종료하기, 1번은 관리자 모드, 2번은 사용자 모드이다.
    - 관리자 모드의 경우 관리자의 Ssn을 알맞게 입력해야 진입 가능하며, 사용자 모드도 사용자의 Ssn을 알맞게 입력해야 진입 가능하다.

  - `runAdmin()` - 관리자의 기능을 수행하도록 한다.
    - 0번 명령의 경우 이전 명령어 선택으로 돌아가고 1번의 경우 관리자가 속해있는 지점의 정보를 출력하며, 2번의 경우 사용자 계정 관리 모드로 들어가도록 한다.

  - `runClient()`
    -  0번의 경우 이전 명령어 선택으로 돌아가고, 1번은 자신의 정보 보기, 2번은 자신의 계좌 보기, 3번은 자신의 카드 보기, 4번은 은행 활동(입출금, 송금) 모드로 들어가기, 5번은 대출 서비스 모드로 들어가기의 기능을 수행한다.

  - `runAdminClientAccount()` - 이 함수는 관리자 모드로 사용자와 사용자의 계좌를 관리하기 위함이다.
    - 0번은 관리자 모드 나가기, 1번은 사용자 추가하기, 2번은 입력한 사용자의 계좌 삭제하기, 3번은 입력한 사용자의 계좌 보여주기 4번은 입력한 사용자의 계좌 추가하기의 기능을 한다.

  - `runMoney()` - 이 함수는 입출금 및 송금을 위한 함수이다. 
    - 금액을 입력하고 deposit()을 통해 입금, withdraw()를 통해 출금, remittance()를 통해 송금을 할 수 있도록 한다. 송금의 경우 받는 사람의 계좌도 입력해야 한다.

  - `runLoan()` - 이 함수는 대출 서비스를 위한 함수이다.
    - 0번은 대출 서비스 모드 종료, 1번은 자신의 대출 현황 보기, 2번은 입력한 금액을 대출하기이다.

- Administrator
  - 관리자가 존재하는지 체크하는 `checkAdmin()`, 사용자가 존재하는지 체크하는 `checkUser(String usrSsn)`, 특정 계좌가 존재하는지 체크하는 `checkAccount(String account)`, 관리자가 속해있는 지점의 정보를 출력하는 `printBranchInfo()`, 사용자의 계좌를 삭제하는 `deleteAccount(String usrAccount)`, 사용자를 추가하는 `addClient(Scanner sc)`, 사용자의 모든 계좌의 정보를 출력해주는 `showClientAccount(String usrSsn, String accountType)`, 사용자에게 계좌를 추가해주는 `addClientAccount()`, 오늘의 날짜를 연월일 단위로 리턴해주는 `todayDate()`로 이루어져 있다.
  - `todayDate()`를 제외하고는 모두 Query를 실행하여 결과를 추출해내거나, 데이터를 업데이트하는 함수이다. Query의 실행은 모두 PreparedStatement를 이용했으며, select query의 결과는 ResultSet에 담아서 처리하였다.

- User
  - 사용자가 존재하는지 체크하는 `checkUser()`, 사용자의 특정 계좌가 있는지 체크하는 `checkAccount(String account)`, 현재 접속한 사용자의 정보를 출력하는 `showMyInfo()`, 현재 접속한 사용자의 모든 계좌를 보여주는 `showMyAccount()`, 현재 접속한 사용자의 모든 카드를 보여주는 `showMyCard()`, 현재 접속한 사용자의 특정 계좌에 특정 금액을 예금하는 `deposit(int amount, String accountNum)`, 현재 접속한 사용자의 특정 계좌로부터 특정 금액을 출금하는 `withdraw(int amount, String accountNum)`, 현재 접속한 사용자의 특정 계좌로부터 다른 사용자의 계좌로 특정 금액을 송금하는 `remittance(int amount, String fromAccount, String toAccount)`, 현재 접속한 사용자가 대출 받을 수 있도록 해주는 `getLoan(int amount)`, 현재 접속한 사용자의 모든 대출 정보를 출력해주는 `showLoanInfo()` 함수들로 이루어져있다.
  - 모두 Query를 실행하여 결과를 추출해내거나, 데이터를 업데이트하는 함수이다. Query의 실행은 모두 PreparedStatement를 이용했으며, select query의 결과는 ResultSet에 담아서 처리하였다.



## #3 SQL 명세

- 위에서 설명한 Administrator와 User 클래스의 메소드에서 실질적인 Query가 실행되며, 각각의 메소드에 사용된 Query를 설명하도록 하겠다.
- `checkAdmin()`
  - Administrator로부터 관리자의 Ssn과 table의 Adm_ssn이 일치하는 것의 개수를 가져오는데 관리자가 존재할 경우 1이 반환되므로 이 경우 true를 리턴하게 했고 이외의 경우 관리자가 존재하지 않는다는 메시지를 출력하도록 했다.

- `checkUser()`
  - 관리자의 경우와 마찬가지로 사용자가 존재할 경우 1이 반환되므로 true를 리턴, 이외의 경우 사용자가 존재하지 않는다는 메시지를 출력하도록 했다.

- `checkAccount()`
  - 위의 경우들과 마찬가지로 계좌가 존재할 경우 1이 반환되므로 true를 리턴, 이외의 경우 계좌가 존재하지 않는다는 메시지를 출력하도록 했다.

- `printBranchInfo()`
  - Branch와 Administrator로부터 Branch의 이름과 주소를 뽑아내는데, join 조건으로 Branch_num = Bnum을 줬고 현재 접속한 관리자와 일치하는지 여부를 나타내는 Adm_ssn = admSsn을 줬다.

- `deleteAccount()`
  - 계좌 테이블로부터 사용자의 계좌와 일치하는 계좌를 지우는 Query이다.

- `addClient()`
  - 사용자로부터 정보를 읽고, 해당하는 정보를 User 테이블에 insert하는 Query이다.

- `showClientAccount()`
  - 계좌와 지점 테이블로부터 지점 이름, 계좌번호, 계좌종류, 생성일자, 예금액을 알아내기 위한 Query이며 지점과 계좌를 연결하기 위해 관리자를 FROM절에 넣었으며 join 조건으로 Adm_ssn = Assn, Bnum = Branch_num을 줬다.

- `addClientAccount()`
  - 사용자에게 계좌를 추가해주는 Query이며 관리자는 현재 접속한 관리자, 사용자는 선택한 사용자로 설정하고 계좌를 만들어준다.
  - 세 가지 Query로 이루어져 있는데 첫번째와 두번째 query는 계좌번호를 정해주기 위함이며 모든 계좌를 내림차순으로 정렬하여 뽑아낸 후, 제일 번호가 큰 계좌번호에서 1을 더하는 방식으로 계좌를 생성했다. 계좌의 자리수가 9자리이므로 적당 인원을 수용 가능할 것이라 생각했다.
  - 세번째 Query는 실질적으로 계좌를 추가하는 query이며, 관리자, 사용자, 계좌종류, 현재날짜를 정보로 넣는다.

- `showMyInfo()`
  - 사용자의 정보를 뽑아내는 query이며, User 테이블로부터 모든 이름, 전화번호, 성별, 생년월일의 정보를 가져와 출력한다.

- `showMyAccount()`
  - 접속한 사용자의 모든 계좌정보를 출력하는 query이며, 관리자의 이름, 계좌번호, 계좌종류, 계좌생성일, 예금액을 Account와 Administrator 테이블로부터 뽑아온다. 관리자의 이름을 출력하기 위해 관리자 테이블도 넣은 것이며, join 조건으로는 Adm_ssm = Assn을 넣었다.

- `showMyCard()`
  - 사용자의 모든 card를 출력하기 위한 query이며 카드번호, 유효년도, 유효월, 카트 종류, 한도를 카드와 사용자 테이블로부터 뽑아낸다. Join 조건으로는 Usr_ssn = Ussn을 사용했다.

- `deposit()`
  - 입금하는 query이며, 첫번째 쿼리는 사용자의 계좌에 입금액만큼 추가해서 update해준다.
  - money 테이블에 그 내역을 추가할 수 있도록 하는 두번째와 세번째 쿼리가 함께 있다.

- `withdraw()`
  - 입금과 마찬가지로 기본적인 3개의 쿼리로 이루어져 있다. 다만 sql이라는 쿼리를 통해 계좌의 잔액 이상으로 돈을 출금하지 못하도록 했다.
  - Query1은 해당 금액만큼 빼서 update하고, query2는 해당 계좌의 내역 번호를 들고와서 1을 추가한 후 출금에 대한 내역을 내역 번호와 출금액, 계좌번호와 함께 insert한다.

- `remittance()`
  - 송금하는 query이며, 보내는 사람은 출금과 동일한 transaction을 수행하고, 받는 사람은 입금과 동일한 transaction을 수행한다. 중복되는 내용이므로 출금과 입금에 대한 쿼리는 다시 적지 않겠다.
  - 송금한 사람의 내역은 “Remittance”라고 찍힐 수 있도록 했으며 내역에는 송금받는 사람의 계좌는 따로 넣지 않았다.
  - 송금 받은 사람의 내역은 “Remittanced”라고 찍힐 수 있도록 했으며 내역에는 송금한 사람의 계좌는 따로 넣지 않았다.

- `getLoan()`
  - 대출의 경우 개인별 번호를 부여하지 않고 전체적인 관점에서의 번호를 부여했다. 그러므로 마지막 번호 다음 번호가 새로 입력되는 대출의 정보이다. 대출 받은 사람의 Ssn도 함께 넣었다.

- `showLoanInfo()`
  - 접속한 사용자가 대출한 정보를 보여주는 query이며 대출번호, 대출 금액을 loan과 user테이블로부터 뽑아낸다. Join 조건은 Usr_ssn = Ussn이다.
  - 대출한 이력이 없을 경우엔 “You don’t have any loan”을 출력한다.

- `showTransactionInfo()`
  - 거래 내역을 보여주는 query이며, 거래 번호, 금액, 입출금(송금)을 계좌와 돈 테이블로부터 뽑아내며 join 조건은 Anum = Account_num으로 했고, number를 기준으로 오름차순으로 출력했다.



##   #4 프로그램 실행

- 프로그램 실행을 위해 각 테이블별로 5~15개정도의 데이터를 제약조건에 위배되지 않도록 넣었다.

- 실행 화면  

  - 사용자와 비밀번호를 받으면 연결되고 처음 화면이 나온다.
    - <img src="https://github.com/llordly/DatabaseSystem/blob/master/img/exe1.png?raw=true" alt="img" style="zoom:50%;" />
  - Administrator로 접속한 경우이다. 관리자 Ssn이 틀렸을 경우 접속할 수 없고 다시 메인 화면으로 돌아온다. 접속에 성공했을 경우 관리자 메뉴가 나오고 커맨드를 받을 수 있도록 했다.
    - <img src='https://github.com/llordly/DatabaseSystem/blob/master/img/exe2.png?raw=true' style="zoom:50%;" >

  - 관리자가 속해 있는 지점의 정보를 보고 싶을 경우 1을 입력하면 되고 그 결과가 나온다.
    - <img src='https://github.com/llordly/DatabaseSystem/blob/master/img/exe3.png?raw=true' style="zoom:50%;" >
  - 사용자를 관리하고 싶을 경우 2를 입력한다. 이 경우 사용자 관리 모드로 들어가게 된다.
    - <img src='https://github.com/llordly/DatabaseSystem/blob/master/img/exe4.png?raw=true' style="zoom:50%;" >
  - 사용자를 추가하고 싶을 경우 1을 입력하고 양식에 맞게 사용자의 정보를 입력하면 추가가 완료된다.
    - <img src='https://github.com/llordly/DatabaseSystem/blob/master/img/exe5.png?raw=true' style="zoom:50%;" >

  - 2를 입력하면 사용자의 계좌를 삭제할 수 있고 올바르지 않은 계좌를 입력할 경우 없는 계좌라는 경고를 띄워준다. 삭제가 완료되면 Deleted라는 메시지가 출력된다.
    - <img src='https://github.com/llordly/DatabaseSystem/blob/master/img/exe6.png?raw=true' style="zoom:50%;" >

  - 3을 입력하면 사용자의 계좌를 볼 수 있고, 존재하는 사용자를 입력했을 때 사용자가 가진 모든 계좌의 정보를 보여준다. 계좌가 없을 경우엔 출력하지 않는다. 잘못된 계좌일 경우 경고 메시지를 띄운다.
    - <img src='https://github.com/llordly/DatabaseSystem/blob/master/img/exe7.png?raw=true' style="zoom:50%;" >

  - 4를 입력할 경우 사용자의 ssn을 입력하라고 하고 Saving계좌인지 Checking계좌인지 여부를 묻고 해당 계좌를 생성해준다. 이 경우 예금액은 0원으로 설정된다. 완료되면 완료 메시지를 출력한다. 다음은 생성하고, 그 결과를 확인한 것이다.
    - <img src='https://github.com/llordly/DatabaseSystem/blob/master/img/exe8.png?raw=true' style="zoom:50%;" >

  - 다음은 사용자에 접속하는 화면이다. 올바른 사용자의 Ssn을 입력했을 경우에만 사용자 모드를 실행할 수 있다.
    - <img src='https://github.com/llordly/DatabaseSystem/blob/master/img/exe9.png?raw=true' style="zoom:50%;" >

  - 1번을 입력하면 접속한 사용자의 정보를 출력한다.
    - <img src="https://github.com/llordly/DatabaseSystem/blob/master/img/exe10.png?raw=true" style="zoom:50%;" >

  - 2번을 입력하면 접속한 사용자의 모든 계좌의 정보를 출력한다.
    - <img src='https://github.com/llordly/DatabaseSystem/blob/master/img/exe11.png?raw=true' style="zoom:50%;" >

  - 3번을 입력하면 접속한 사용자의 모든 카드 정보를 출력한다.
    - <img src="https://github.com/llordly/DatabaseSystem/blob/master/img/exe12.png?raw=true" style="zoom:50%;" >

  - 4번을 입력하면 예금, 출금, 송금, 거래 내역과 관련된 서비스를 이용할 수 있는 모드로 넘어간다. 이 경우 내가 가지고 있는 계좌를 입력해야만 서비스를 이용할 수 있다.
    - <img src='https://github.com/llordly/DatabaseSystem/blob/master/img/exe13.png?raw=true' style="zoom:50%;" >

  - 1번을 입력하면 입금을 할 수 있고, 원하는 금액을 입금할 수 있다. 입금이 완료되면 완료 메시지가 출력된다.
    - <img src="https://github.com/llordly/DatabaseSystem/blob/master/img/exe14.png?raw=true" style="zoom:50%;" >

  - 2번을 입력하면 출금을 할 수 있고, 원하는 금액을 출금할 수 있다. 출금이 완료되면 완료 메시지가 출력된다.
    - <img src='https://github.com/llordly/DatabaseSystem/blob/master/img/exe15.png?raw=true' style="zoom:50%;" >

  - 3번을 입력하면 송금을 할 수 있고, 원하는 금액을 원하는 계좌에 송금할 수 있다. 송금이 완료되면 완료 메시지가 출력된다.
    - <img src="https://github.com/llordly/DatabaseSystem/blob/master/img/exe16.png?raw=true" style="zoom:50%;" >

  - 4번을 입력하면 사용자 계좌의  모든 거래 내역을 출력한다.
    - <img src="https://github.com/llordly/DatabaseSystem/blob/master/img/exe17.png?raw=true" style="zoom:50%;" >

  - 이전 메뉴로 돌아와서 5번을 입력하면 대출 서비스를 이용할 수 있다. 그 후 1번을 입력하면 사용자의 대출 정보를 보여준다.
    - <img src='https://github.com/llordly/DatabaseSystem/blob/master/img/exe18.png?raw=true' style="zoom:50%;" >

  - 2번을 입력하면 대출을 받을 수 있고 입력한 금액만큼 대출받을 수 있다.
    - <img src="https://github.com/llordly/DatabaseSystem/blob/master/img/exe19.png?raw=true" style="zoom:50%;" >

  - 다시 메인 메뉴로 돌아와 0번을 누르면 프로그램이 Exit 메시지와 함께 종료된다.
    - <img src="https://github.com/llordly/DatabaseSystem/blob/master/img/exe20.png?raw=true" style="zoom:50%;" >
