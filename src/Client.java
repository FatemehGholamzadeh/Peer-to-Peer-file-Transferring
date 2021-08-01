package ir.aut;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;

public class Client extends Thread {
    String fileName;

    public Client(String fileName) {
        this.fileName = fileName;
    }

    public void run() {
        try {
            String[] parts = fileName.split("\\.");     //parts[1] is the postfix
            MulticastSocket ms = new MulticastSocket(1234);
            ms.joinGroup(InetAddress.getByName("239.0.0.1"));


            DatagramSocket clientSocket = new DatagramSocket();
            int p = clientSocket.getLocalPort();
            //    InetAddress IPAddress = InetAddress.getByName("localhost");


            byte[] request = (p + fileName).getBytes();
            byte[] receiveData = new byte[36];
            byte[] fileSize = new byte[10];


            while (true) {
                DatagramPacket datagramPacket = new DatagramPacket(request, request.length, InetAddress.getByName("239.0.0.1"), 1234);
                ms.connect(InetAddress.getByName("239.0.0.1"), 1234);
                ms.send(datagramPacket);
                ms.close();


                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);
                String message = new String(receiveData, 0, receiveData.length, "UTF-8");
                System.out.println(message);


                FileOutputStream out = new FileOutputStream(new File("D:\\download\\" + parts[0] + "." + parts[1]), false);
                byte[] receive = new byte[128];
                byte[] storage = new byte[9000000];
                if (message.equals("Server says : I have the file!      ")) {
                    while (true) {
                        DatagramPacket packet = new DatagramPacket(receive, receive.length);

                        clientSocket.receive(packet);
                        String b = new String(receive, 0, receive.length, "UTF-8");
                        String bSub = b.substring(0, 8);
                        if (bSub.equals("finished"))
                            break;
                        int offset1 = (int) receive[0] & 0xff;
                        int offset2 = (int) receive[1] & 0xff;
//                        System.out.println(offset1);
//                        System.out.println(offset2);
                        int offset = offset1 * 256 + offset2;

//                        System.out.println("offset = " + offset);
                        for (int i = 0; i < 126; i++) {
                            storage[offset * 126 + i] = receive[i + 2];
                        }

//                        System.out.println(b);

                    }
                    System.out.println("File received!");
                }
                out.write(storage);
                out.close();

            }


            //  clientSocket.close();


        } catch (SocketException e) {

        } catch (UnknownHostException e) {

        } catch (IOException e) {

        }

    }


}
