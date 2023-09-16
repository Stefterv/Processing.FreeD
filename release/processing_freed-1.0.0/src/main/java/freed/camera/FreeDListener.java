package freed.camera;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Dictionary;
import java.util.HashMap;

import processing.core.*;

public class FreeDListener extends Thread {
    DatagramSocket socket;
    PApplet parent;
    int port;
    boolean active = true;
    HashMap<Integer, FreeDBuffer> cameras;

    public void run() {
        try {
            socket = new DatagramSocket(port);
            parent.println("FreeD listener started at "+port);
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (active) {
            byte[] buffer = new byte[32];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            try {
                socket.receive(packet);
                FreeDBuffer freeDData = FreeDBuffer.decode(buffer);
                cameras.put(freeDData.id, freeDData);
//                parent.println("Received FreeD data: " + freeDData.toString());
            } catch (Exception e) {
                parent.println("Failed to decode FreeD data: " + e.getMessage());
            }
        }
    }
}
