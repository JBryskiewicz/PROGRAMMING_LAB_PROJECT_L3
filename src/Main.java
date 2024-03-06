import classes.Bank;
import classes.Client;
import classes.Operation;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String[] OPTIONS = new String[]{
            ANSI_YELLOW + "Type in number to perform action according to list below:" + ANSI_RESET,
            "1. Check Balance",
            "2. Deposit",
            "3. Withdraw",
            "4. Transfer",
            "5. View Transactions",
            "0. Exit"
    };
    public static final String INCORRECT_INPUT = ANSI_RED + "Incorrect input, try again" + ANSI_RESET;
    public static final String NEGATIVE_AMOUNT = INCORRECT_INPUT + ANSI_RED + ". Smallest operation allowed: 0.01"+ ANSI_RESET;
    public static final String INPUT_MISMATCH = ANSI_RED + "Your input doesn't match any option, try again..." + ANSI_RESET;
    public static final String INSUFFICIENT_FUNDS = ANSI_RED + "Not enough funds to perform this operation!" + ANSI_RESET;
    public static final String OPERATION_SUCCESSFUL = "operation was successful";
    public static final String AMOUNT_QUESTION = "How much do you want to";
    public static final String DATE_QUESTION = "Find transactions by date (yyyy-MM-dd)";
    private static final File FILE_CLIENTS = new File("accounts.dat");

    public static void main(String[] args) {
        Bank bank = loadBankData();
        createMockClients(bank);

        Scanner scanner = new Scanner(System.in);

        System.out.println(ANSI_GREEN + "--- Simple banking app ---" + ANSI_RESET);
        String accountNumber = logIn(bank, scanner);

        while (true) {
            printOptions();

            int input;

            System.out.print(ANSI_YELLOW + "Choose an option: " + ANSI_RESET);
            try {
                input = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println(INPUT_MISMATCH);
                scanner.nextLine();
                continue;
            }

            switch (input) {
                case 1:
                    printBalance(bank, accountNumber);
                    break;
                case 2:
                    executeDeposit(bank, scanner, accountNumber);
                    break;
                case 3:
                    executeWithdrawal(bank, scanner, accountNumber);
                    break;
                case 4:
                    executeTransfer(bank, scanner, accountNumber);
                    break;
                case 5:
                    printTransactions(bank, scanner, accountNumber);
                    break;
                case 0:
                    saveBankData(bank);
                    System.out.println(ANSI_GREEN + "Thank you for using our banking app" + ANSI_RESET);
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println(INPUT_MISMATCH);
                    break;
            }
        }
    }

    public static void printOptions() {
        Arrays.stream(OPTIONS).forEach(System.out::println);
    }

    private static void createMockClients(Bank bank) {
        Client john = new Client("John Doe", "00-0001", 0);
        Client william = new Client("William Doe", "00-0002", 0);
        Client zoe = new Client("Zoe Doe", "00-0003", 0);

        if (!bank.getClientList().contains(john)) {
            bank.addClient(john);
            bank.addClient(william);
            bank.addClient(zoe);
        }
    }

    private static Client findClient(Bank bank, String accountNumber) {
        for (Client client : bank.getClientList()) {
            if (client.getAccountNumber().equals(accountNumber)) return client;
        }
        return null;
    }

    private static String logIn(Bank bank, Scanner scanner) {
        boolean condition = false;
        String correctNumber = null;

        while (!condition) {
            System.out.println(ANSI_YELLOW + "To cancel type: `exit`" + ANSI_RESET);
            System.out.print("Enter account number: ");
            String input = scanner.nextLine();

            if (input.equals("exit")) System.exit(0);

            Client client = findClient(bank, input);
            if (client != null) {
                correctNumber = client.getAccountNumber();
                condition = true;
                System.out.println(ANSI_GREEN + "Welcome " + client.getName() + ANSI_RESET);
            } else {
                System.out.println(ANSI_RED + "Account doesn't exist, try again" + ANSI_RESET);
            }
        }
        return correctNumber;
    }

    private static void printBalance(Bank bank, String accountNumber) {
        Client client = findClient(bank, accountNumber);

        if (client != null) {
            System.out.println(ANSI_GREEN + "--- YOUR BALANCE ---" + ANSI_RESET);
            System.out.print(client.getName() + "'s account balance: ");
            System.out.printf("%.2f",  client.getBalance());
            System.out.println();
            System.out.println(ANSI_GREEN + "--------------------" + ANSI_RESET);
        }
    }

    private static void executeDeposit(Bank bank, Scanner scanner, String accountNumber) {
        boolean validInput = false;
        do {
            try{
                System.out.print(AMOUNT_QUESTION + " deposit?: ");
                double amount = scanner.nextDouble();

                Client client = findClient(bank, accountNumber);
                if (client != null) {
                    boolean result = bank.makeDeposit(client, amount);
                    String message = result
                            ? ANSI_GREEN + "Deposit " + OPERATION_SUCCESSFUL + ANSI_RESET
                            : NEGATIVE_AMOUNT;
                    System.out.println(message);
                    validInput = true;
                }
            } catch (InputMismatchException e) {
                System.out.println(INCORRECT_INPUT);
                scanner.next();
            }
        } while (!validInput);
    }

    private static void executeWithdrawal(Bank bank, Scanner scanner, String accountNumber) {
        boolean validInput = false;
        do {
            try{
                System.out.print(AMOUNT_QUESTION + " withdraw?: ");
                double amount = scanner.nextDouble();

                Client client = findClient(bank, accountNumber);
                if (client != null) {
                    boolean result = bank.makeWithdrawal(client, amount);
                    String message = result
                            ? ANSI_GREEN + "Withdrawal " + OPERATION_SUCCESSFUL + ANSI_RESET
                            : INSUFFICIENT_FUNDS + " or " + NEGATIVE_AMOUNT;
                    System.out.println(message);
                    validInput = true;
                }
            } catch (InputMismatchException e) {
                System.out.println(INCORRECT_INPUT);
                scanner.next();
            }
        } while (!validInput);
    }

    private static void executeTransfer(Bank bank, Scanner scanner, String senderNumber) {
        boolean validInput = false;
        do {
            try{
                System.out.print("Enter recipient's account number: ");
                scanner.nextLine();
                String recipientNumber = scanner.nextLine();

                System.out.print(AMOUNT_QUESTION + " transfer?: ");
                double amount = scanner.nextDouble();

                Client sender = findClient(bank, senderNumber);
                Client recipient = findClient(bank, recipientNumber);

                if (sender != null && recipient != null) {
                    boolean result = bank.makeTransfer(sender, recipient, amount);
                    String message = result
                            ? ANSI_GREEN + "Transfer " + OPERATION_SUCCESSFUL + ANSI_RESET
                            : INSUFFICIENT_FUNDS + " or " + NEGATIVE_AMOUNT;
                    System.out.println(message);
                    validInput = true;
                } else {
                    System.out.println(ANSI_RED + "Recipient not found" + ANSI_RESET);
                }
            } catch (InputMismatchException e) {
                System.out.println(INCORRECT_INPUT);
                scanner.next();
            }
        } while (!validInput);
    }

    private static void printTransactions(Bank bank, Scanner scanner, String accountNumber) {
        System.out.print(DATE_QUESTION + " from: ");
        scanner.nextLine();
        String fromDate = scanner.nextLine();

        System.out.print(DATE_QUESTION + " to: ");
        String toDate = scanner.nextLine();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = dateFormat.parse(fromDate);
            Date endDate = dateFormat.parse(toDate);

            Client client = findClient(bank, accountNumber);

            if(client != null) {
                List<Operation> operationList = client.getDatedOperationList(startDate, endDate);

                System.out.println(ANSI_YELLOW + "---Transactions---" + ANSI_RESET);
                for (Operation operation : operationList) {
                    System.out.println(operation.getDate() + " - " + operation.getDescription());
                }
                System.out.println(ANSI_YELLOW + "--------------" + ANSI_RESET);
            }
        } catch ( ParseException e) {
            System.out.println(ANSI_RED + "Invalid date format" + ANSI_RESET);
        }
    }

    private static Bank loadBankData() {
        Bank bank = new Bank();
        try (ObjectInputStream clientsInputStream = new ObjectInputStream(new FileInputStream(FILE_CLIENTS))) {
            bank = (Bank) clientsInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println();
        }
        return bank;
    }

    private static void saveBankData(Bank bank) {
        try (ObjectOutputStream clientsOutputStream = new ObjectOutputStream(new FileOutputStream(FILE_CLIENTS))) {
            clientsOutputStream.writeObject(bank);
        } catch (IOException e) {
            System.out.println(ANSI_RED + "There was an unexpected error while saving data to storage" + ANSI_RESET);
        }
    }
}