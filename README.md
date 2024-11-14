# javAPRSlib

javAPRSlib is a Java library to parse and create [APRS](http://www.aprs.org/)
packets. APRS (Automatic Packet Reporting System) is a digital amateur radio
mode used for broadcasting local tactical information, position tracking and
much more.

javAPRSlib is licensed under the GNU Lesser General Public License. The aim of
the library is to become an easy-to-use APRS backend both for open and for
closed APRS applications. If you are using javAPRSlib in your project, please
let us know and contribute patches, fixes and improvements.

[![Java CI](../../actions/workflows/javaci.yml/badge.svg?branch=v2.0&event=push)](../../actions/workflows/javaci.yml)

## Usage instructions

### When using `Maven`

Executing 'mvn compile' will build the library, executing 'mvn package' will create a JAR package.
All compiled code winds up in the "target" directory.

### Examples

You can invoke the parser from the command line:

    $ PKT="DO1GL-5>APDR11,TCPIP*,qAC,T2SP:=5206.  N/01138.  E$ Georg APRSdroid http://aprsdroid.org/"
    $ java -classpath target/javAPRSlib-<VERSION>.jar net.ab0oo.aprs.parser.Parser $PKT

```
From:   DO1GL-5
To:     APDR11
Via:    TCPIP*,QAC,T2SP
DTI:    =
Valid:  true
Data:   Raw Bytes:      =5206.  N/01138.  E$ Georg APRSdroid http://aprsdroid.org/
Data Type Identifier: =
Create Timestamp:       Thu Nov 14 13:38:32 PST 2024
Comment:
Class net.ab0oo.aprs.parser.PositionField
---POSITION---
Position Source Uncompressed
Is Compressed:  false
Latitude:       52.10833
Longitude:      11.64167
Comment:  E$ Georg APRSdroid http://aprsdroid.org/

    Type:       net.ab0oo.aprs.parser.InformationField
    Messaging:  false
    Comment:
    Extension:  null
```

## Features

So far, the following APRS packet types can be decoded and encoded:

- Position (standard, Mic-E, NMEA)
- Object
- Message

Further, it is possible to encode and decode the AX.25 MAC format.

## Acknowledgement

The following people have contributed to javAPRSlib:

- Matti Aarnio, OH2MQK (original java-aprs-fap code)
- John Gorkos, AB0OO
- Georg Lukas, DO1GL
- Jens (jetrit)