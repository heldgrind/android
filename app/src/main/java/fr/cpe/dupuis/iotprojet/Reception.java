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
    private String IP = "";//"192.168.1.24";
    private int port = 0;//10000;
    private InetAddress adresse;
    private DatagramSocket UDPSocket;
    private MyThreadEventListener listener;
    private BlockingQueue<String> queue;
    private int size;


    public Reception(MyThreadEventListener listener,String IP, int port, BlockingQueue<String> queue ) {  {
        this.queue = queue;
        this.listener = listener;
        this.IP = IP;
        this.port = port;
        try {
            adresse = InetAddress.getByName(IP);
            UDPSocket = new DatagramSocket(); // create a socket
            UDPSocket.setSoTimeout(2000); // set a timeout of 2 seconds
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }
    }

    public void run(){

    Envoi envoi = new Envoi(listener,IP,port,queue); // Création d'un thread d'envoi
    envoi.start();

        while (true) {

            try {
                sleep(2000); // Pause de 2 secondes évite le spam
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            byte[] autorisation = "getValues()".getBytes(StandardCharsets.UTF_8); // getValues() est la commande envoyée au serveur pour obtenir les valeurs des capteurs
            DatagramPacket packet2 = new DatagramPacket(autorisation, autorisation.length, adresse, port); // Création du paquet à envoyer

            try {
                sleep(2000);
                if (UDPSocket != null) {
                    UDPSocket.send(packet2); // Envoi du paquet
                }

                sleep(10);
                try {


                    byte[] data = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(data, data.length); // Création du paquet à recevoir

                    if (UDPSocket != null) {
                        UDPSocket.receive(packet); // Réception du paquet
                    }
                    size = packet.getLength();

                    if (size > 0) { // Si le paquet reçu n'est pas vide
                        listener.onEventInMyThread(new String(data, 0, size));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
