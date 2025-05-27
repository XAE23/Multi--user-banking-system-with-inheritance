import java.io.*;
import java.util.*;

class User {
    String username;
    String password;
    double balance;
    List<String> transactions;

    User(String username, String password) {
        this.username = username;
        this.password = password;
        this.balance = 0.0;
        this.transactions = new ArrayList<>();
    }
}

public class BankingSystem {
    static final String USERS_FOLDER = "users";

    public static void main(String[] args) {
        new File(USERS_FOLDER).mkdir();  // Create users folder if not exists
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Welcome to Java Bank ---");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // clear buffer

            switch (choice) {
                case 1:
                    register(sc);
                    break;
                case 2:
                    login(sc);
                    break;
                case 3:
                    System.out.println("Thank you for using Java Bank!");
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    static void register(Scanner sc) {
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        File userFile = new File(USERS_FOLDER + "/" + username + ".txt");
        if (userFile.exists()) {
            System.out.println("Username already exists!");
            return;
        }

        System.out.print("Enter password: ");
        String password = sc.nextLine();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(userFile));
            writer.write(password + "\n");
            writer.write("0.0\n");  // balance
            writer.close();
            System.out.println("Account created successfully!");
        } catch (IOException e) {
            System.out.println("Error creating account.");
        }
    }

    static void login(Scanner sc) {
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        File userFile = new File(USERS_FOLDER + "/" + username + ".txt");
        if (!userFile.exists()) {
            System.out.println("User not found!");
            return;
        }

        System.out.print("Enter password: ");
        String password = sc.nextLine();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(userFile));
            String storedPassword = reader.readLine();
            if (!password.equals(storedPassword)) {
                System.out.println("Incorrect password!");
                reader.close();
                return;
            }

            double balance = Double.parseDouble(reader.readLine());
            List<String> transactions = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                transactions.add(line);
            }
            reader.close();

            User user = new User(username, password);
            user.balance = balance;
            user.transactions = transactions;

            userMenu(sc, user);

        } catch (IOException e) {
            System.out.println("Error logging in.");
        }
    }

    static void userMenu(Scanner sc, User user) {
        while (true) {
            System.out.println("\n--- Hello, " + user.username + " ---");
            System.out.println("1. View Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. View Transactions");
            System.out.println("5. Logout");
            System.out.print("Choose option: ");
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    System.out.println("Current balance: Rs." + user.balance);
                    break;
                case 2:
                    System.out.print("Enter deposit amount: ");
                    double deposit = sc.nextDouble();
                    user.balance += deposit;
                    user.transactions.add("Deposited Rs." + deposit);
                    saveUser(user);
                    System.out.println("Deposit successful.");
                    break;
                case 3:
                    System.out.print("Enter withdrawal amount: ");
                    double withdraw = sc.nextDouble();
                    if (withdraw > user.balance) {
                        System.out.println("Insufficient balance.");
                    } else {
                        user.balance -= withdraw;
                        user.transactions.add("Withdrew Rs." + withdraw);
                        saveUser(user);
                        System.out.println("Withdrawal successful.");
                    }
                    break;
                case 4:
                    System.out.println("--- Transaction History ---");
                    if (user.transactions.isEmpty()) {
                        System.out.println("No transactions yet.");
                    } else {
                        for (String t : user.transactions) {
                            System.out.println(t);
                        }
                    }
                    break;
                case 5:
                    System.out.println("Logged out successfully.");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    static void saveUser(User user) {
        try {
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(USERS_FOLDER + "/" + user.username + ".txt")
            );
            writer.write(user.password + "\n");
            writer.write(user.balance + "\n");
            for (String t : user.transactions) {
                writer.write(t + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving user data.");
        }
    }
}