package net.ab0oo.aprs.parser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.junit.jupiter.api.Test;

public class TestClient {
    private static final String host = "205.233.35.46";
    private static final int port = 10152;
    private static final int lineCount = 1000;
    private Socket clientSocket;
    private BufferedReader input;
    private PrintWriter out;

@Test
    public void testClient() {
        int linecount = 0;
        int badPackets = 0;
        try {
            clientSocket = new Socket(host, port);
            System.out.println(clientSocket.getLocalPort());
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println(input.readLine());
            System.out.println("Sending login.");
            out.println("user ab0oo pass 19951");
            String fromServer;
            APRSPacket packet = new APRSPacket("AB0OO", "APRS", null, new byte[20]);
            while ( (null != input ) && (fromServer = input.readLine() ) != null && linecount < lineCount ) {
                linecount++;
                if ( fromServer.startsWith("#") ) { continue; }
                //System.out.println(fromServer);
                try {
                    packet = Parser.parse( fromServer );
                } catch ( Exception ex ) {
                    packet.setHasFault(true);
                }
                if ( packet.hasFault() ) {
                    System.out.println(fromServer);
                    System.err.println("This packet failed to parse:");
                    System.out.println(packet);
                    badPackets++;
                }
            }
            System.out.println("Disconnected after receiving "+linecount+" lines.");
        } catch ( Exception ex ) {
            System.err.println("Exception caught during socket communications: " + ex.toString());
            ex.printStackTrace();
            System.exit(1);
        } finally {
            try {
                input.close();
                out.flush();
                out.close();
                clientSocket.close();
            } catch ( Exception finalEx ) {}
        }
        System.out.println("Of "+linecount+" packets received, "+badPackets+" were unparsable");
    }

    public static void main(String args[]) {
        TestClient tc = new TestClient();
        tc.testClient();
    }
}