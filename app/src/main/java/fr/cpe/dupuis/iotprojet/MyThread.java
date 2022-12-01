package fr.cpe.dupuis.iotprojet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;

public class MyThread extends Thread{
    private final String IP="192.168.0.18"; // Remplacer par l'IP de votre interlocuteur
    private final int PORT=10000; // Constante arbitraire du sujet
    private InetAddress address; // Structure Java décrivant une adresse résolue
    private DatagramSocket UDPSocket; // Structure Java permettant d'accéder au réseau (UDP)
    private String message;
    private BlockingQueue<String> queue;
    private MyThreadEventListener listener = null;
        public MyThread(BlockingQueue<String> queue,MyThreadEventListener listener) throws UnknownHostException {
            this.listener = listener;
            try{
                address = InetAddress.getByName(IP);
                UDPSocket = new DatagramSocket();
            } catch (SocketException e) {
                e.printStackTrace();
            }

        }
        public void run() {
            while(true){
                try{
                    byte[] buffer = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, PORT);
                    UDPSocket.receive(packet);
                    message = new String(packet.getData());
                    listener.onEventInMyThread(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }