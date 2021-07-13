package net.ab0oo.aprs.parser;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.DisplayName;
 

@DisplayName("Callsign Tests")
class CallsignTest {
	/* TODO Test SSID 0 */
	/* TODO Test serialization */

	@Nested
	@DisplayName("Given regular APRS callsign with SSID")
	class GivenRegularCallsignSsid {
		final String callsign = "AB1CDE-12";

		@Nested
		@DisplayName("When instantiated as a class")
		class WhenInstantiated {
			Callsign object;

			@BeforeEach
			void setUp() {
				object = new Callsign(callsign);
			}

			@Test
			@DisplayName("Then it should return the proper callsign")
			void thenReturnCallsign() {
				assertEquals("AB1CDE", object.getCallsign());
			}

			@Test
			@DisplayName("Then it should return the proper SSID")
			void thenReturnSsid() {
				assertEquals("12", object.getSsid());
			}

			@Test
			@DisplayName("Then it should return the proper string")
			void thenReturnProperString() {
				assertEquals("AB1CDE-12", object.toString());
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
					(byte)(0x60 | (12 << 1)),
				};
				assertEquals(7, object.toAX25().length);
				assertArrayEquals(expected, object.toAX25());
			}
		}

		@Nested
		@DisplayName("When converted to bytes and back")
		class WhenConverted {
			Callsign object;

			@BeforeEach
			void setUp() {
				object = new Callsign(
					new Callsign(callsign).toAX25(), 0);
			}

			@Test
			@DisplayName("Then it should return the proper callsign")
			void thenReturnCallsign() {
				assertEquals("AB1CDE", object.getCallsign());
			}

			@Test
			@DisplayName("Then it should return the proper SSID")
			void thenReturnSsid() {
				assertEquals("12", object.getSsid());
			}

			@Test
			@DisplayName("Then it should return the proper string")
			void thenReturnProperString() {
				assertEquals("AB1CDE-12", object.toString());
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
					(byte)(0x60 | (12 << 1)),
				};
				assertEquals(7, object.toAX25().length);
				assertArrayEquals(expected, object.toAX25());
			}
		}
	}

	@Nested
	@DisplayName("Given lowercase APRS callsign")
	class GivenLowercaseCallsign {
		final String callsign = "ab1cde";

		@Nested
		@DisplayName("When instantiated as a class")
		class WhenInstantiated {
			Callsign object;

			@BeforeEach
			void setUp() {
				object = new Callsign(callsign);
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
			@DisplayName("Then it should return the proper string")
			void thenReturnProperString() {
				assertEquals("AB1CDE", object.toString());
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
					(byte)(0x60 | (0 << 1)),
				};
				assertEquals(7, object.toAX25().length);
				assertArrayEquals(expected, object.toAX25());
			}
		}

		@Nested
		@DisplayName("When converted to bytes and back")
		class WhenConverted {
			Callsign object;

			@BeforeEach
			void setUp() {
				object = new Callsign(
					new Callsign(callsign).toAX25(), 0);
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
			@DisplayName("Then it should return the proper string")
			void thenReturnProperString() {
				assertEquals("AB1CDE", object.toString());
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
					(byte)(0x60 | (0 << 1)),
				};
				assertEquals(7, object.toAX25().length);
				assertArrayEquals(expected, object.toAX25());
			}
		}
	}

	@Nested
	@DisplayName("Given an APRS callsign")
	class GivenCallsign {
		Callsign object;

		@BeforeEach
		void setUp() {
			object = new Callsign("W1AW-5");
		}

		@Nested
		@DisplayName("When changing callsign")
		class WhenCallsignChanged {
			@BeforeEach
			void setUp() {
				object.setCallsign("NJ7P");
			}

			@Test
			@DisplayName("Then it should return the new callsign")
			void thenReturnNewCallsign() {
				assertEquals("NJ7P", object.getCallsign());
			}

			@Test
			@DisplayName("Then it should return the original SSID")
			void thenReturnOriginalSsid() {
				assertEquals("5", object.getSsid());
			}

			@Test
			@DisplayName("Then it should return the proper string")
			void thenReturnProperString() {
				assertEquals("NJ7P-5", object.toString());
			}

			@Test
			@DisplayName("Then it should return correct AX25 data")
			void thenReturnAX25() {
				final byte[] expected = {
					(byte)('N' << 1),
					(byte)('J' << 1),
					(byte)('7' << 1),
					(byte)('P' << 1),
					(byte)(' ' << 1),
					(byte)(' ' << 1),
					(byte)(0x60 | (5 << 1)),
				};
				assertEquals(7, object.toAX25().length);
				assertArrayEquals(expected, object.toAX25());
			}
		}

		@Nested
		@DisplayName("When changing ssid")
		class WhenSsidChanged {
			@BeforeEach
			void setUp() {
				object.setSsid("9");
			}

			@Test
			@DisplayName("Then it should return the original callsign")
			void thenReturnOriginalCallsign() {
				assertEquals("W1AW", object.getCallsign());
			}

			@Test
			@DisplayName("Then it should return the new SSID")
			void thenReturnNewSsid() {
				assertEquals("9", object.getSsid());
			}

			@Test
			@DisplayName("Then it should return the proper string")
			void thenReturnProperString() {
				assertEquals("W1AW-9", object.toString());
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
					(byte)(0x60 | (9 << 1)),
				};
				assertEquals(7, object.toAX25().length);
				assertArrayEquals(expected, object.toAX25());
			}
		}

		@Nested
		@DisplayName("When changing to mixed-case callsign")
		class WhenCallsignChangedMixedCase {
			@BeforeEach
			void setUp() {
				object.setCallsign("N7lEm");
			}

			@Test
			@DisplayName("Then it should return the new uppercase callsign")
			void thenReturnNewCallsign() {
				assertEquals("N7LEM", object.getCallsign());
			}

			@Test
			@DisplayName("Then it should return the original SSID")
			void thenReturnOriginalSsid() {
				assertEquals("5", object.getSsid());
			}

			@Test
			@DisplayName("Then it should return the proper string")
			void thenReturnProperString() {
				assertEquals("N7LEM-5", object.toString());
			}

			@Test
			@DisplayName("Then it should return correct AX25 data")
			void thenReturnAX25() {
				final byte[] expected = {
					(byte)('N' << 1),
					(byte)('7' << 1),
					(byte)('L' << 1),
					(byte)('E' << 1),
					(byte)('M' << 1),
					(byte)(' ' << 1),
					(byte)(0x60 | (5 << 1)),
				};
				assertEquals(7, object.toAX25().length);
				assertArrayEquals(expected, object.toAX25());
			}
		}
	}
}
