package fr.cpe.dupuis.iotprojet;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Envoi extends Thread {
    private String IP;
    private int port;
    private InetAddress adresse;
    private DatagramSocket socket;
    private MyThreadEventListener listener;
    private int size;



}
