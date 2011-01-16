/*
 * AVRS - http://avrs.sourceforge.net/
 *
 * Copyright (C) 2011 John Gorkos, AB0OO
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

 * Please note that significant portions of this code were taken from the JAVA FAP
 * translation by Matti Aarnio at http://repo.ham.fi/websvn/java-aprs-fap/
 * 
 */

package net.ab0oo.aprs;

import java.util.ArrayList;
/**
 * 
 * @author johng
 *	This is the code parser for AX25 UI packets that are traditionally used in APRS networks, in TNC2
 * format.  TNC2 format is defined as:
 * SOURCE>DESTIN,VIA,VIA:payload
 * In APRS packets, the first character of the payload is the Data Type Identifier, which is the key for
 * further parsing of the message.  This class parses raw TNC2 packets and returns instances of APRSPackets
 */
public class Parser {
    
    public APRSPacket parsePacket(byte[] rawPacket) {
        //if ( packet.getDti() == '!' || packet.getDti() == '=' ) {
            // !3449.94N/08448.56W_203/000g000t079P133h85b10149OD1
        return new APRSPacket(null, null, null, null);
    }
    
    public APRSPacket parse(String packet) throws Exception {
        int cs = packet.indexOf('>');
        String source = packet.substring(0,cs).toUpperCase();
        int ms = packet.indexOf(':');
        String digiList = packet.substring(cs+1,ms);
        String[] digiTemp = digiList.split(",");
        String dest = digiTemp[0].toUpperCase();
        ArrayList<Digipeater> digis = new ArrayList<Digipeater>();
        if ( digiTemp.length > 0 ) {
            for ( int i=1; i<digiTemp.length; i++) {
                digis.add(new Digipeater(digiTemp[i]));
            }
        }
        String body = packet.substring(ms+1);
        byte[] bodyBytes = body.getBytes();
        byte dti = bodyBytes[0];
        InformationField infoField = null;
        APRSTypes type = APRSTypes.T_UNSPECIFIED;
        boolean hasFault = false;
        switch ( dti ) {
        	case '!':
        	case '=':
        	case '/':
        	case '@':
        	case '`':
        	case '\'':
        	case '$':
        		infoField = new PositionPacket(bodyBytes,dest);
        		break;
        	case ':':
        		infoField = new MessagePacket(bodyBytes,dest);
        		break;
    		case ';':
    			if (bodyBytes.length > 29) {
    				//System.out.println("Parsing an OBJECT");
					type = APRSTypes.T_OBJECT;
    				infoField = new ObjectPacket(bodyBytes);
    			} else {
    				hasFault = true; // too short for an object
    			}
    			break;
    		case '>':
    			type = APRSTypes.T_STATUS;
    			break;
    		case '<':
    			type = APRSTypes.T_STATCAPA;
    			break;
    		case '?':
    			type = APRSTypes.T_QUERY;
    			break;
    		case ')':
    			if (bodyBytes.length > 18) {
    				//System.out.println("Parsing an ITEM");
    				//parseItem(bodyBytes);
    			} else {
    				hasFault = true; // too short
    			}
    			break;
    		case 'T':
    			if (bodyBytes.length > 18) {
    				//System.out.println("Parsing TELEMETRY");
    				//parseTelem(bodyBytes);
    			} else {
    				hasFault = true; // too short
    			}
    			break;
    		case '#': // Peet Bros U-II Weather Station
    		case '*': // Peet Bros U-II Weather Station
    		case '_': // Weather report without position
    			type = APRSTypes.T_WX;
    			break;
    		case '{':
    			type = APRSTypes.T_USERDEF;
    			break;
    		case '}': // 3rd-party
    			type = APRSTypes.T_THIRDPARTY;
    			break;

    		default:
    			hasFault = true; // UNKNOWN!
    			break;

        }
        APRSPacket returnPacket = new APRSPacket(source,dest,digis,infoField);
        returnPacket.setType(type);
        returnPacket.setHasFault(hasFault);
        return returnPacket;
        
    }
    
}
