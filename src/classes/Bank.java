package classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Bank implements Serializable {

    private List<Client> clientList;

    public Bank() {
        clientList = new ArrayList<>();
    }

    public List<Client> getClientList() {
        return clientList;
    }

    public void addClient(Client client) {
        clientList.add(client);
    }

    public boolean makeDeposit(Client client, double amount) {
        if (amount >= 0.01) {
            client.deposit(amount);
            List<Operation> operationList = client.getAllOperationList();
            operationList.add(
                    new Operation(new Date(), "Deposit for amount: " + amount)
            );
            client.setOperationList(operationList);
            return true;
        }
        return false;
    }

    public boolean makeWithdrawal(Client client, double amount) {
        if(amount >= 0.01 && client.getBalance() - amount >= 0){
            client.withdraw(amount);
            List<Operation> operationList = client.getAllOperationList();
            operationList.add(
                    new Operation(new Date(), "Withdrawal for amount: " + amount)
            );
            client.setOperationList(operationList);
            return true;
        }
        return false;
    }

    public boolean makeTransfer(Client sender, Client recipient, double amount){
        if(amount >= 0.01 && sender.getBalance() - amount >= 0) {
            String transferMessage = "Transfer from " + sender.getName() + " to " + recipient.getName() + " for amount of " + amount;
            sender.transfer(recipient, amount);

            List<Operation> senderOperationList = sender.getAllOperationList();
            senderOperationList.add(
                    new Operation(new Date(), transferMessage)
            );
            sender.setOperationList(senderOperationList);

            List<Operation> recipientOperationList = recipient.getAllOperationList();
            recipientOperationList.add(
                    new Operation(new Date(), transferMessage)
            );
            recipient.setOperationList(recipientOperationList);

            return true;
        }
        return false;
    }
}
