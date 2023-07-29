package net.ab0oo.aprs.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TestClient {
    private static final String host = "205.233.35.46";
    private static final int port = 10152;
    private static final int MAX_LINES = 1000;
    private static Map<APRSTypes, Integer> typeCounts = new HashMap<>();
    private static Map<Character, Integer> dtiCounts = new HashMap<>();
    private static int linecount = 0;
    private static int badPackets = 0;

    public void testClient() {
        Socket clientSocket = null;
        BufferedReader input = null;
        PrintWriter out = null;
        FileWriter badPackets = null;
        try {
            // set up a file for bad packets
            badPackets = new FileWriter("badpackets.txt");
            clientSocket = new Socket(host, port);
            System.out.println(clientSocket.getLocalPort());
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println(input.readLine());
            System.out.println("Sending login.");
            out.println("user ab0oo pass 19951");
            String fromServer;
            while ( (null != input ) && (fromServer = input.readLine() ) != null && linecount <= MAX_LINES ) {
                linecount++;
                if ( fromServer.startsWith("#") ) { continue; }
                //System.out.println(fromServer);
                APRSPacket p = processPacket(fromServer);
                if ( p.getAprsInformation().containsType(APRSTypes.T_WX)) {
                    WeatherField wf = (WeatherField)p.getAprsInformation().getAprsData(APRSTypes.T_WX);
                    System.out.println( p.getSourceCall() +" temperature is "+ wf.getTemp());
                }
                if ( p.hasFault() ) {
                    badPackets.write(fromServer);
                    badPackets.write("\n");
                }
            }
            System.out.println("Disconnected after receiving "+linecount+" lines.");
        } catch ( Exception ex ) {
            System.err.println("Exception caught during socket communications: " + ex.toString());
            ex.printStackTrace();
            System.exit(1);
        } finally {
            try {
                if ( null != badPackets ) {
                    badPackets.flush();
                    badPackets.close();;
                } 
                if ( null != input ) input.close();
                if ( null != out ) out.flush();
                if ( null != out ) out.close();
                if ( null != clientSocket ) clientSocket.close();
            } catch ( Exception finalEx ) {}
        }
    }

    public void testFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String fromServer = br.readLine();
            while (null != fromServer && linecount <= MAX_LINES) {
                linecount++;
                if (!fromServer.startsWith("#")) {
                    processPacket(fromServer);
                }
                fromServer = br.readLine();
                //System.out.println(packet.toString());
            }
        } catch (Exception ex) {
            System.err.println("Exception caught reading file: " + ex.toString());
            ex.printStackTrace();

        }
        System.out.println("Of " + linecount + " packets received, " + badPackets + " were unparsable");
    }

    private APRSPacket processPacket(String packetString) {
        APRSPacket packet = new APRSPacket("N4NE-2","APRX29",null,";147.150SM*111111z3414.21N/08409.59Wr147.150MHz C141 R50m PHG7600 WB4GQX/R".getBytes());
        try {
            packet = Parser.parse( packetString );
            Set<APRSTypes> types = packet.getAprsInformation().getTypes();
            for ( APRSTypes type : types ) {
                if ( typeCounts.containsKey(type)) {
                    typeCounts.put(type, typeCounts.get(type) + 1);
                } else {
                    typeCounts.put(type, 1);
                }
            }
            Character dti = packet.getAprsInformation().getDataTypeIdentifier();
            if ( !packet.hasFault()) {
                if ( dtiCounts.containsKey(dti) ) {
                    dtiCounts.put(dti, dtiCounts.get(dti) + 1);
                } else {
                    dtiCounts.put(dti, 1);
                }
            }
        } catch ( Exception ex ) {
            System.err.println(ex.toString());
            ex.printStackTrace();
            packet.setHasFault(true);
        }
        if ( packet.hasFault() ) {
            System.err.println("This packet failed to parse:  " + packetString);
            badPackets++;
        }
        return packet;
    }

    public static void main(String args[]) {
        String fileName = "src/test/resources/aprs.txt";
        if (args.length > 0) {
            fileName = args[0];
        }
        TestClient tc = new TestClient();
        if ( fileName.equals("remote")) {
            tc.testClient();
        } else {
            tc.testFile(fileName);
        }
        System.out.println("Of "+linecount+" packets received, "+badPackets+" were unparsable"); 
        for ( APRSTypes type : typeCounts.keySet() ) {
            if ( type == null ) { continue; }
            System.out.println(type.name()+"\t"+typeCounts.get(type));
        }
        int totalDtis = 0;
        System.out.println("Counts by Data Type Identifier");
        for ( Character dti : dtiCounts.keySet() ) {
            System.out.println(dti+"\t"+dtiCounts.get(dti));
            totalDtis+=dtiCounts.get(dti);
        }
        System.out.println(totalDtis+" packets with identifiable DTIs");
    }
}