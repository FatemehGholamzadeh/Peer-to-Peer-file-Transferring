package ir.aut;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {


        Scanner scanner = new Scanner(System.in);
        System.out.println("enter your mode: ");
        String mode = scanner.next();

        if (mode.equals("1")) {
            System.out.println("file Request : ");
            String fileName = scanner.next();
            Client client = new Client(fileName);
            client.start();
        } else {
            Server server = new Server();
            server.start();
        }


    }
}
