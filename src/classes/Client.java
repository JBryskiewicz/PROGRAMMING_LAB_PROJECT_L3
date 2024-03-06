package classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Client implements Serializable {

    private final String name;
    private final String accountNumber;
    private double balance;
    private List<Operation> operationList;

    public Client (String name, String accountNumber, double balance) {
        this.name = name;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.operationList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        this.balance += amount;
    }

    public void withdraw(double amount) {
        this.balance -= amount;
    }

    public void transfer(Client recipient, double amount) {
        this.balance -= amount;
        recipient.deposit(amount);
    }

    public List<Operation> getAllOperationList() {
        return operationList;
    }

    public List<Operation> getDatedOperationList(Date startDate, Date endDate) {
        List<Operation> result = new ArrayList<>();
        for (Operation operation : operationList) {
            if (operation.getDate().after(startDate) && operation.getDate().before(endDate)) {
                result.add(operation);
            }
        }
        return result;
    }

    public void setOperationList(List<Operation> operationList) {
        this.operationList = operationList;
    }
}
