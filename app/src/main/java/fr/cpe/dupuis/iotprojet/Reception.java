package fr.cpe.dupuis.iotprojet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;

public class Reception extends Thread {
    private String IP = "192.168.1.24";
    private int port = 10000;
    private InetAddress adresse;
    private DatagramSocket UDPSocket;
    private MyThreadEventListener listener;
    private BlockingQueue<String> queue;
    private int size;


    public Reception(MyThreadEventListener listener) {
        this.listener = listener;
        try {
            adresse = InetAddress.getByName(IP);
            UDPSocket = new DatagramSocket(port);
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }
    public void run(){
        byte[] data = new byte[1024];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        byte[] autorisation = "getValues()".getBytes(StandardCharsets.UTF_8);
        DatagramPacket packet2 = new DatagramPacket(autorisation, autorisation.length, adresse, port);
        try {
            UDPSocket.send(packet2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            UDPSocket.receive(packet);
            size = packet.getLength();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (size>0){
            listener.onEventInMyThread(new String(data, 0, size));
        }
    }
}

