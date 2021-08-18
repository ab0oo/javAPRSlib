package net.ab0oo.aprs.parser;

import java.util.List;
import java.util.Set;

//import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
//import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.DisplayName;
 

@DisplayName("APRS Packet Tests")
public class APRSPacketTests {
	@BeforeEach
	public void setUp() {
	}

	@AfterEach
	public void tearDown() {
	}

	@Nested
	@DisplayName("Given an empty APRS packet")
	public class GivenEmptyAPRSPacket {
		APRSPacket packet, packet2;

		@BeforeEach
		void setUp() {
			String testBody = "!4903.50N/07201.75W-Test 001234";
			String testHeader = "A1BC>APRS01,TCPIP*:";
			String testCompletePacket = testHeader+testBody;
			byte[] msgBody = testBody.getBytes();
			packet = new APRSPacket("A1BC", "APRS01", null, msgBody);
			try {
				packet2 = Parser.parse(testCompletePacket);
			} catch ( Exception ex ) {
				System.err.println("Unable to parse test packet");
			}

		}

		@Nested
		@DisplayName("When questioned")
		public class WhenQuestioned {
			@Test
			@DisplayName("Then it should have no fault")
			public void thenHasNoFault() {
				System.out.println("Success!");
				assertFalse(packet.hasFault());
			}

			@Test
			@DisplayName("Then it should have one TCPIP digipeater")
			public void thenHasOneDigipeater() {
				List<Digipeater> digipeaters = packet.getDigipeaters();
				assertEquals(1, digipeaters.size());
			}

			@Test
			@DisplayName("Then it should have a correct lattitude and longitude")
			public void thenHasPosition() {
				Set<APRSData> dataFields = packet2.getAprsInformation().getAprsData();
				for ( APRSData dataField : dataFields ) {
					if (dataField.type == APRSTypes.T_POSITION) {
						PositionField pf = (PositionField)dataField;
						assertEquals(49.058330, pf.getPosition().getLatitude());
						assertEquals(-72.029170, pf.getPosition().getLongitude());
					} else {
						System.err.println("Miscalculated APRSType");
						assertEquals(0, 1);
					}
				}
			}
		}
	}
}
