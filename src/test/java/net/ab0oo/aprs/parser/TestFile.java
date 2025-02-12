package net.ab0oo.aprs.parser;

import java.io.BufferedReader;
import java.io.FileReader;

import org.junit.jupiter.api.Test;

public class TestFile {
    @Test
    public void testParser() {
        testParser("src/test/resources/objects.txt");
    }

    
    /** 
     * @param fileName
     */
    public void testParser(String fileName) {
        int linecount = 0;
        int badPackets = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String fromServer = br.readLine();
            APRSPacket packet = new APRSPacket("AB0OO", "APRS", null, new byte[20]);
            while (null != fromServer ) {
                linecount++;
                if (!fromServer.startsWith("#")) {
                    try {
                        packet = Parser.parse(fromServer);
                    } catch (Exception ex) {
                        System.err.println("Error parsing packet");
                    }
                    if (packet.hasFault()) {
                        System.out.println(fromServer);
                        System.err.println("This packet failed to parse:");
                        System.out.println(packet);
                        badPackets++;
                    }
                }
                fromServer = br.readLine();
                // System.out.println(packet.toString());
            }
        } catch (Exception ex) {
            System.err.println("Exception caught reading file: " + ex.toString());
            ex.printStackTrace();
            System.exit(1);
        }
        System.out.println("Of " + linecount + " packets received, " + badPackets + " were unparsable");
    }

    public static void main(String args[]) {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        String testFile = "src/test/resources/cwop.txt";
        if (args.length > 0) {
            testFile = args[0];
        }
        TestFile tf = new TestFile();
        tf.testParser(testFile);
    }
}