package net.ab0oo.aprs.parser;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.DisplayName;
 

@DisplayName("Callsign Tests")
public class CallsignTest {
	@Nested
	@DisplayName("Given regular APRS callsign with SSID")
	public class GivenRegularCallsignSsid {
		final String callsign = "AB1CDE-12";

		@Nested
		@DisplayName("When instantiated as a class")
		public class WhenInstantiated {
			Callsign object;

			@BeforeEach
			void setUp() {
				object = new Callsign(callsign);
			}

			@Test
			@DisplayName("Then it should return the proper callsign")
			public void thenReturnCallsign() {
				assertEquals("AB1CDE", object.getCallsign());
			}

			@Test
			@DisplayName("Then it should return the proper SSID")
			public void thenReturnSsid() {
				assertEquals("12", object.getSsid());
			}

			@Test
			@DisplayName("Then it should return correct AX25 data")
			public void thenReturnAX25() {
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
		public class WhenConverted {
			Callsign object;

			@BeforeEach
			void setUp() {
				object = new Callsign(
					new Callsign(callsign).toAX25(), 0);
			}

			@Test
			@DisplayName("Then it should return the proper callsign")
			public void thenReturnCallsign() {
				assertEquals("AB1CDE", object.getCallsign());
			}

			@Test
			@DisplayName("Then it should return the proper SSID")
			public void thenReturnSsid() {
				assertEquals("12", object.getSsid());
			}

			@Test
			@DisplayName("Then it should return correct AX25 data")
			public void thenReturnAX25() {
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
	public class GivenLowercaseCallsign {
		final String callsign = "ab1cde";

		@Disabled
		@Nested
		@DisplayName("When instantiated as a class")
		public class WhenInstantiated {
			Callsign object;

			@BeforeEach
			void setUp() {
				object = new Callsign(callsign);
			}

			@Test
			@DisplayName("Then it should return the proper callsign")
			public void thenReturnCallsign() {
				assertEquals("AB1CDE", object.getCallsign());
			}

			@Test
			@DisplayName("Then it should return the proper SSID")
			public void thenReturnSsid() {
				assertEquals("", object.getSsid());
			}

			@Test
			@DisplayName("Then it should return correct AX25 data")
			public void thenReturnAX25() {
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
		public class WhenConverted {
			Callsign object;

			@BeforeEach
			void setUp() {
				object = new Callsign(
					new Callsign(callsign).toAX25(), 0);
			}

			@Disabled
			@Test
			@DisplayName("Then it should return the proper callsign")
			public void thenReturnCallsign() {
				assertEquals("AB1CDE", object.getCallsign());
			}

			@Test
			@DisplayName("Then it should return the proper SSID")
			public void thenReturnSsid() {
				assertEquals("", object.getSsid());
			}

			@Disabled
			@Test
			@DisplayName("Then it should return correct AX25 data")
			public void thenReturnAX25() {
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
}
