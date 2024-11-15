/*
 * javAPRSlib - https://github.com/ab0oo/javAPRSlib
 *
 * Copyright (C) 2011, 2024 John Gorkos, AB0OO
 *
 * AVRS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * AVRS is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AVRS; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */
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
    private static final int port = 10152;
    private static final int MAX_LINES = 1000;
    private static String host = "192.168.1.11";
    private static Map<APRSTypes, Integer> typeCounts = new HashMap<>();
    private static Map<Character, Integer> dtiCounts = new HashMap<>();
    private static int linecount = 0;
    private static int badPackets = 0;

    public void testClient(String hostname) {
        Socket clientSocket = null;
        BufferedReader input = null;
        PrintWriter socketOut = null;
        FileWriter badPackets = null;
        String fromServer = "";
        try {
            // set up a file for bad packets
            badPackets = new FileWriter("badpackets.txt", true);
            clientSocket = new Socket(hostname, port);
            System.out.println(clientSocket.getLocalPort());
            socketOut = new PrintWriter(clientSocket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println(input.readLine());
            socketOut.println("user ab0oo-x pass 19951 vers JavAPRSLib 3.0.7");
            socketOut.println("filter t/poimqstunw");
            while ( (null != input ) && (fromServer = input.readLine() ) != null && linecount <= MAX_LINES ) {
                System.out.println(fromServer);
                if ( fromServer.startsWith("#") ) continue;
                linecount++;
                APRSPacket p = processPacket(fromServer);
                if ( p.hasFault() ) {
                    badPackets.write(p.getFaultReason()+":\t");
                    badPackets.write(fromServer);
                    badPackets.write("\n");
                }
            }
            System.out.println("Disconnected after receiving "+linecount+" lines.");
        } catch ( Exception ex ) {
            System.err.println("Exception caught during socket communications: " + ex.toString());
            System.err.println("Last input line was:  "+fromServer);
            ex.printStackTrace();
        } finally {
            try {
                if ( null != badPackets ) {
                    badPackets.flush();
                    badPackets.close();;
                } 
                if ( null != input ) input.close();
                if ( null != socketOut ) socketOut.flush();
                if ( null != socketOut ) socketOut.close();
                if ( null != clientSocket ) clientSocket.close();
            } catch ( Exception finalEx ) {}
        }
    }

    
    /** 
     * @param fileName
     */
    public void testFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String fromServer = br.readLine();
            while (null != fromServer && linecount <= MAX_LINES) {
                if (!fromServer.startsWith("#")) {
                    processPacket(fromServer);
                    linecount++;
                } else {
                    System.out.println(fromServer.toString());
                }
                fromServer = br.readLine();
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
        } catch ( Exception ex ) {
            packet.setOriginalString(packetString);
            InformationField infoField = new InformationField();
            APRSData data = new BadData();
            data.setFaultReason(ex.getMessage());
            infoField.addAprsData(APRSTypes.T_UNSPECIFIED, data);
            packet.setInfoField(new InformationField());
            return packet;
        }
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
        if ( null != packet && packet.hasFault() ) {
            System.err.println("This packet failed to parse:  " + packetString);
            System.err.println("Fault reason: "+ packet.getFaultReason());
            badPackets++;
        }
        if ( packet.getAprsInformation().containsType(APRSTypes.T_WX)) {
            // WeatherField wf = (WeatherField)packet.getAprsInformation().getAprsData(APRSTypes.T_WX);
            // System.out.println(packetString);
            // System.out.println(wf);
        }
        if ( null != packet.getAprsInformation().getExtension() ) {
            // System.out.println("Found extension "+ packet.getAprsInformation().getExtension().getType());
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
            if ( args.length > 1 ) {
                host = args[1];
            }
            tc.testClient(host);
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
