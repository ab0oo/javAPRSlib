package net.ab0oo.aprs.parser;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.DisplayName;
 

@DisplayName("Position Packet Tests")
class PositionPacketTest {
	/* TODO Test serialization */
	/* TODO Fix code duplication */

	@Nested
	@DisplayName("Given a valid APRS location message body")
	class GivenValidAPRSLocationMessage {
		final String body = "@120845z3111.02NI08122.52W&V=12.3";

		@Nested
		@DisplayName("When parsed as a packet")
		class WhenParsed {
			PositionPacket packet;

			@BeforeEach
			void setUp() {
				try {
					packet = new PositionPacket(body.getBytes(), null);
				} catch(Exception ex) {
					packet = null;
				}
			}

			@Test
			@DisplayName("Then it should not have raised an exception")
			void thenReturnValid() {
				assertNotNull(packet, "Exception raised during packet parsing");
			}

			@Test
			@DisplayName("Then it should not be compressed")
			void thenReturnUncompressed() {
				assertFalse(packet.getCompressedFormat());
			}

			@Test
			@DisplayName("Then it should return the proper position sourcce")
			void thenReturnSource() {
				assertEquals("Uncompressed", packet.getPositionSource());
			}

			@Test
			@DisplayName("Then it should return the proper location")
			void thenReturnLocation() {
				Position pos = packet.getPosition();
				assertEquals( (31. + 11.02/60.), pos.getLatitude(),  0.001);
				assertEquals(-(81. + 22.52/60.), pos.getLongitude(), 0.001);
				assertEquals(0, pos.getPositionAmbiguity());
			}

			@Test
			@DisplayName("Then it should return the proper timestamp")
			void thenReturnTimestamp() {
				Position pos = packet.getPosition();
				Date time = pos.getTimestamp();
				assertEquals(12, time.getDate());
				assertEquals(8, time.getHours());
				assertEquals(45, time.getMinutes());
			}

			@Test
			@DisplayName("Then it should return the original raw bytes")
			void thenReturnRawBytes() {
				assertArrayEquals(body.getBytes(), packet.getRawBytes());
			}

			@Test
			@DisplayName("Then it should return the original string")
			void thenReturnString() {
				assertEquals(body, packet.toString());
			}
		}

		/*
		@Nested
		@DisplayName("When converted to bytes and back")
		class WhenConverted {
			Digipeater object;

			@BeforeEach
			void setUp() {
				object = new Digipeater(
					new Digipeater(digipeater).toAX25(), 0);
			}

			@Test
			@DisplayName("Then it should return the proper callsign")
			void thenReturnCallsign() {
				assertEquals("W1AW", object.getCallsign());
			}

			@Test
			@DisplayName("Then it should return the proper SSID")
			void thenReturnSsid() {
				assertEquals("15", object.getSsid());
			}

			@Test
			@DisplayName("Then it should return as unused")
			void thenReturnUsed() {
				assertFalse(object.isUsed(), "Should not be used");
			}

			@Test
			@DisplayName("Then it should return the proper string")
			void thenReturnProperString() {
				assertEquals("W1AW-15", object.toString());
			}

			@Test
			@DisplayName("Then it should return correct AX25 data")
			void thenReturnAX25() {
				final byte[] expected = {
					(byte)('W' << 1),
					(byte)('1' << 1),
					(byte)('A' << 1),
					(byte)('W' << 1),
					(byte)(' ' << 1),
					(byte)(' ' << 1),
					(byte)(0*0x80 | 0x60 | (15 << 1)),
				};
				assertEquals(7, object.toAX25().length);
				assertArrayEquals(expected, object.toAX25());
			}
		}
		*/
	}

	/*
	@Nested
	@DisplayName("Given used, lower-case APRS digipeater")
	class GivenUsedLowercaseDigipeater {
		final String digipeater = "ab1cde*";

		@Nested
		@DisplayName("When instantiated as a class")
		class WhenInstantiated {
			Digipeater object;

			@BeforeEach
			void setUp() {
				object = new Digipeater(digipeater);
			}

			@Test
			@DisplayName("Then it should return the proper callsign")
			void thenReturnCallsign() {
				assertEquals("AB1CDE", object.getCallsign());
			}

			@Test
			@DisplayName("Then it should return the proper SSID")
			void thenReturnSsid() {
				assertEquals("", object.getSsid());
			}

			@Test
			@DisplayName("Then it should return as used")
			void thenReturnUsed() {
				assertTrue(object.isUsed(), "Should be used");
			}

			@Test
			@DisplayName("Then it should return the proper string")
			void thenReturnProperString() {
				assertEquals("AB1CDE*", object.toString());
			}

			@Test
			@DisplayName("Then it should return correct AX25 data")
			void thenReturnAX25() {
				final byte[] expected = {
					(byte)('A' << 1),
					(byte)('B' << 1),
					(byte)('1' << 1),
					(byte)('C' << 1),
					(byte)('D' << 1),
					(byte)('E' << 1),
					(byte)(1*0x80 | 0x60 | (0 << 1)),
				};
				assertEquals(7, object.toAX25().length);
				assertArrayEquals(expected, object.toAX25());
			}
		}

		@Nested
		@DisplayName("When converted to bytes and back")
		class WhenConverted {
			Digipeater object;

			@BeforeEach
			void setUp() {
				object = new Digipeater(
					new Digipeater(digipeater).toAX25(), 0);
			}

			@Test
			@DisplayName("Then it should return the proper callsign")
			void thenReturnCallsign() {
				assertEquals("AB1CDE", object.getCallsign());
			}

			@Test
			@DisplayName("Then it should return the proper SSID")
			void thenReturnSsid() {
				assertEquals("", object.getSsid());
			}

			@Test
			@DisplayName("Then it should return as used")
			void thenReturnUsed() {
				assertTrue(object.isUsed(), "Should be used");
			}

			@Test
			@DisplayName("Then it should return the proper string")
			void thenReturnProperString() {
				assertEquals("AB1CDE*", object.toString());
			}

			@Test
			@DisplayName("Then it should return correct AX25 data")
			void thenReturnAX25() {
				final byte[] expected = {
					(byte)('A' << 1),
					(byte)('B' << 1),
					(byte)('1' << 1),
					(byte)('C' << 1),
					(byte)('D' << 1),
					(byte)('E' << 1),
					(byte)(1*0x80 | 0x60 | (0 << 1)),
				};
				assertEquals(7, object.toAX25().length);
				assertArrayEquals(expected, object.toAX25());
			}
		}
	}

	@Nested
	@DisplayName("Given an APRS digipeater")
	class GivenDigipeater {
		Digipeater object;

		@BeforeEach
		void setUp() {
			object = new Digipeater("W1AW-1");
		}
		@Nested
		@DisplayName("When changing ssid")
		class WhenUsedChanged {
			@BeforeEach
			void setUp() {
				object.setUsed(true);
			}

			@Test
			@DisplayName("Then it should return the proper callsign")
			void thenReturnCallsign() {
				assertEquals("W1AW", object.getCallsign());
			}

			@Test
			@DisplayName("Then it should return the proper SSID")
			void thenReturnNewSsid() {
				assertEquals("1", object.getSsid());
			}

			@Test
			@DisplayName("Then it should return as used")
			void thenReturnUsed() {
				assertTrue(object.isUsed(), "Should be used");
			}

			@Test
			@DisplayName("Then it should return the proper string")
			void thenReturnProperString() {
				assertEquals("W1AW-1*", object.toString());
			}

			@Test
			@DisplayName("Then it should return correct AX25 data")
			void thenReturnAX25() {
				final byte[] expected = {
					(byte)('W' << 1),
					(byte)('1' << 1),
					(byte)('A' << 1),
					(byte)('W' << 1),
					(byte)(' ' << 1),
					(byte)(' ' << 1),
					(byte)(1*0x80 | 0x60 | (1 << 1)),
				};
				assertEquals(7, object.toAX25().length);
				assertArrayEquals(expected, object.toAX25());
			}
		}
	}
	*/
}
