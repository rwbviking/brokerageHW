import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.text.SimpleDateFormat;

class PortfolioManager {
    private ArrayList<TransactionHistory> portfolioList = new ArrayList<>();
    private double cashBalance = 0;

    public void depositCash(double amount) {
        cashBalance += amount;
        portfolioList.add(new TransactionHistory("CASH", new Date(), "DEPOSIT", amount, 1.0));
    }

    public void withdrawCash(double amount) {
        if (amount > cashBalance) {
            System.out.println("Error: Insufficient cash balance!");
            return;
        }
        cashBalance -= amount;
        portfolioList.add(new TransactionHistory("CASH", new Date(), "WITHDRAW", -amount, 1.0));
    }

    public void buyStock(String ticker, double quantity, double costPerStock) {
        double totalCost = quantity * costPerStock;
        if (totalCost > cashBalance) {
            System.out.println("Error: Insufficient cash to buy the stock!");
            return;
        }
        cashBalance -= totalCost;
        portfolioList.add(new TransactionHistory(ticker, new Date(), "BUY", quantity, costPerStock));
        portfolioList.add(new TransactionHistory("CASH", new Date(), "WITHDRAW", -totalCost, 1.0));
    }

    public void sellStock(String ticker, double quantity, double sellPrice) {
        double totalEarnings = quantity * sellPrice;
        double currentStockQuantity = getStockQuantity(ticker);
        if (quantity > currentStockQuantity) {
            System.out.println("Error: Insufficient stocks to sell!");
            return;
        }
        cashBalance += totalEarnings;
        portfolioList.add(new TransactionHistory(ticker, new Date(), "SELL", -quantity, sellPrice));
        portfolioList.add(new TransactionHistory("CASH", new Date(), "DEPOSIT", totalEarnings, 1.0));
    }

    private double getStockQuantity(String ticker) {
        double quantity = 0;
        for (TransactionHistory t : portfolioList) {
            if (t.getTicker().equals(ticker)) {
                quantity += t.getQty();
            }
        }
        return quantity;
    }

    public void displayTransactionHistory() {
        System.out.println("Transaction History:");
        System.out.println("Date\tTicker\tQuantity\tCost Basis\tTrans Type");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        for (TransactionHistory transaction : portfolioList) {
            System.out.println(dateFormat.format(transaction.getTransDate()) + "\t" + transaction.getTicker() + "\t" +
                    transaction.getQty() + "\t$" + transaction.getCostBasis() + "\t" + transaction.getTransType());
        }
    }

    public void displayPortfolio() {
        System.out.println("Portfolio:");
        System.out.println("Ticker\tQuantity");
        // Calculate quantities for each ticker
        for (TransactionHistory transaction : portfolioList) {
            if (!transaction.getTicker().equals("CASH")) {
                double quantity = getStockQuantity(transaction.getTicker());
                System.out.println(transaction.getTicker() + "\t" + quantity);
            }
        }
    }

    public static void main(String[] args) {
        PortfolioManager manager = new PortfolioManager();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ashish Gulati Brokerage Account");
        System.out.println("===============================");
        System.out.println("0 - Exit");
        System.out.println("1 - Deposit Cash");
        System.out.println("2 - Withdraw Cash");
        System.out.println("3 - Buy Stock");
        System.out.println("4 - Sell Stock");
        System.out.println("5 - Display Transaction History");
        System.out.println("6 - Display Portfolio");

        int option;
        do {
            System.out.print("Enter option (0 to 6): ");
            option = scanner.nextInt();
            switch (option) {
                case 1:
                    System.out.print("Enter amount to deposit: ");
                    double depositAmount = scanner.nextDouble();
                    manager.depositCash(depositAmount);
                    break;
                case 2:
                    System.out.print("Enter amount to withdraw: ");
                    double withdrawAmount = scanner.nextDouble();
                    manager.withdrawCash(withdrawAmount);
                    break;
                case 3:
                    System.out.print("Enter ticker, quantity, and cost per stock separated by spaces: ");
                    String buyTicker = scanner.next();
                    double buyQuantity = scanner.nextDouble();
                    double buyCostPerStock = scanner.nextDouble();
                    manager.buyStock(buyTicker, buyQuantity, buyCostPerStock);
                    break;
                case 4:
                    System.out.print("Enter ticker, quantity, and sell price per stock separated by spaces: ");
                    String sellTicker = scanner.next();
                    double sellQuantity = scanner.nextDouble();
                    double sellPricePerStock = scanner.nextDouble();
                    manager.sellStock(sellTicker, sellQuantity, sellPricePerStock);
                    break;
                case 5:
                    manager.displayTransactionHistory();
                    break;
                case 6:
                    manager.displayPortfolio();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option! Please enter a number between 0 and 6.");
            }
        } while (option != 0);
        scanner.close();
    }
}