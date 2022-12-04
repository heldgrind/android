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
            UDPSocket = new DatagramSocket();
            UDPSocket.setSoTimeout(2000);
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }
    }

    public void run(){



        while (true) {



            byte[] autorisation = "getValues()".getBytes(StandardCharsets.UTF_8);
            DatagramPacket packet2 = new DatagramPacket(autorisation, autorisation.length, adresse, port);

            try {
                sleep(2000);
                if (UDPSocket != null) {
                    UDPSocket.send(packet2);
                }

                sleep(10);
                try {


                    byte[] data = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(data, data.length);

                    if (UDPSocket != null) {
                        UDPSocket.receive(packet);
                    }
                    size = packet.getLength();

                    if (size > 0) {
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


            String message = null;
            try {
                message = queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            byte[] data = message.getBytes(StandardCharsets.UTF_8);

            DatagramPacket packetenvoiparam = new DatagramPacket(data, data.length, adresse, port);

            try {
                UDPSocket.send(packetenvoiparam);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}

