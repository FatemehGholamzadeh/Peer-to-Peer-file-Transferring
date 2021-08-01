package ir.aut;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;

public class Server extends Thread {

    public void run() {
        try {
            MulticastSocket ms = new MulticastSocket(1234);
            ms.joinGroup(InetAddress.getByName("239.0.0.1"));
            //ms.connect(InetAddress.getByName("239.0.0.1"),1234);


            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[100];
            for (int i = 0; i < 100; i++) {
                receiveData[i] = (byte) ' ';
            }
            //   byte[] fileSize = new byte[1];
            int counter = 0;

            while (true) {

                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                ms.receive(receivePacket);
                String receive = new String(receiveData, 0, receiveData.length, "UTF-8");

                int clinetPort = Integer.parseInt(receive.substring(0, 5));
                System.out.println(receive.toString().substring(5));

                ArrayList<Character> fileName = new ArrayList<>();
                int j = 5;
                char[] chars = receive.toCharArray();
                while (chars[j] != ' ' && j < 100) {
                    fileName.add(chars[j]);
                    j++;
                }


                InetAddress address = receivePacket.getAddress();
                Random random = new Random();
                int p = (int) (2000 + Math.random() * 5000 + 1);
                DatagramSocket serverSocket = new DatagramSocket(p);


                StringBuilder builder = new StringBuilder(fileName.size());
                for (Character ch : fileName) {
                    builder.append(ch);
                }

                File f = new File("C:\\OurNetWork\\" + builder);

                if (f.exists()) {

                    String sentence = "Server says : I have the file!      ";
                    sendData = sentence.getBytes();
                    DatagramPacket datagramPacket = new DatagramPacket(sendData, sendData.length, address, clinetPort);
                    serverSocket.send(datagramPacket);


                    FileInputStream in = new FileInputStream(f);

                    byte[] buffer = new byte[126];
                    byte[] buffer2 = new byte[128];
                    //  Thread.sleep(3000);
                    while (in.read(buffer) != -1) {


                        String hexOffset = Integer.toBinaryString(0x10000 | counter).substring(1);
                        String s1 = hexOffset.substring(0, 8);
                        String s2 = hexOffset.substring(8, 16);
                        int sum1 = 0;
                        int sum2 = 0;
                        for (int i = 0; i < 8; i++) {
                            int b = Character.getNumericValue(s1.charAt(i));
                            sum1 += b * (int) (Math.pow((double) 2, (double) (7 - i)));
                        }
                        for (int i = 0; i < 8; i++) {
                            int b = Character.getNumericValue(s2.charAt(i));
                            sum2 += b * (int) (Math.pow((double) 2, (double) (7 - i)));
                        }

                        byte b1 = (byte) sum1;
                        byte b2 = (byte) sum2;

                        buffer2[0] = b1;
                        buffer2[1] = b2;
                        for (int i = 0; i < 126; i++) {
                            buffer2[i + 2] = buffer[i];
                        }
                        String a = new String(buffer2, 0, buffer2.length, "UTF-8");
//                        System.out.println(a);
                        DatagramPacket packet = new DatagramPacket(buffer2, buffer2.length, address, clinetPort);
                        serverSocket.send(packet);

                        //  Thread.sleep(1);
//                        System.out.println("Packet " + counter + " was sent.");
//                        System.out.println(a);
                        counter++;
                        for (int i = 0; i < buffer.length; i++) {
                            buffer[i] = ' ';
                        }
                    }
                    String stopSending = "finished";
                    DatagramPacket finishedPacket = new DatagramPacket(stopSending.getBytes(), stopSending.getBytes().length, address, clinetPort);
                    serverSocket.send(finishedPacket);


                } else {
                    String sentence = "Server says : I don't have the file!";
                    sendData = sentence.getBytes();
                    DatagramPacket datagramPacket = new DatagramPacket(sendData, sendData.length, address, clinetPort);
                    serverSocket.send(datagramPacket);
                    serverSocket.close();

                }


            }

        } catch (SocketException e) {

        } catch (IOException e) {

        }

    }

}
