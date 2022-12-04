package fr.cpe.dupuis.iotprojet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;

public class Envoi extends Thread {
    private String IP;
    private int port;
    private InetAddress adresse;
    private DatagramSocket UDPSocket;
    private MyThreadEventListener listener;
    private BlockingQueue<String> queue;
    private int size;

    public Envoi (MyThreadEventListener listener, String IP, int port, BlockingQueue<String> queue) {
        this.listener = listener;
        this.IP = IP;
        this.port = port;
        this.queue = queue;
        try {
            adresse = InetAddress.getByName(IP);
            UDPSocket = new DatagramSocket();
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
    }

    public void run() {
       String message = null;
        try {
            message=queue.take(); // Récupération du message à envoyer
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        byte[] data = message.getBytes(); // Conversion du message en tableau de bytes
        DatagramPacket socket = new DatagramPacket(data, data.length, adresse, port);
        try {
            UDPSocket.send(socket); // envoi HT ou TH en fonction de la position
        } catch (IOException e) {
            e.printStackTrace();
        }


    }




}
