package fr.cpe.dupuis.iotprojet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;

public class MyThread extends Thread{
    private final String IP="192.168.0.18"; // Remplacer par l'IP de votre interlocuteur
    private final int PORT=10000; // Constante arbitraire du sujet
    private InetAddress address; // Structure Java décrivant une adresse résolue
    private DatagramSocket UDPSocket; // Structure Java permettant d'accéder au réseau (UDP)
    private BlockingQueue<String> queue;
    public MyThread(BlockingQueue<String> queue) {
        this.queue = queue;
        try {
            UDPSocket = new DatagramSocket();
            address = InetAddress.getByName(IP);
        } catch (IOException e) { e.printStackTrace(); }
    }
    public void run() {

        while (true) {
            String message = null;
            try {
                message = queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                byte[] data = message.getBytes();
                DatagramPacket packet = new DatagramPacket(data,data.length, address, PORT);
                UDPSocket.send(packet);
            } catch (IOException e) { e.printStackTrace(); }
            try {
                byte[] data = new byte[1024];
                DatagramPacket packet = new DatagramPacket(data,data.length);
                UDPSocket.receive(packet);//bloquant
                int size = packet.getLength();
            }catch (IOException e) { e.printStackTrace(); }

        }
    }
}