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
 */
package net.ab0oo.aprs;
/**
 * 
 * @author johng
 * This class represents a single digipeater in a TNC2-format VIA string.
 * 
 */
public class Digipeater {
    private String callsign;
    private String ssid;
    private boolean used;
    
    public Digipeater(String call) {
        if ( call.indexOf("-") > 0 ) {
            ssid = call.substring(call.indexOf("-")+1, call.length());
            this.callsign = call.substring(0,call.indexOf("-"));
        } else {
            this.callsign=call;
            this.ssid="";
        }
    }

    /**
     * @return the callsign
     */
    public String getCallsign() {
        return callsign;
    }

    /**
     * @param callsign the callsign to set
     */
    public void setCallsign(String callsign) {
        this.callsign = callsign;
    }

    /**
     * @return the ssid
     */
    public String getSsid() {
        return ssid;
    }

    /**
     * @param ssid the ssid to set
     */
    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    /**
     * @return the used
     */
    public boolean isUsed() {
        return used;
    }

    /**
     * @param used the used to set
     */
    public void setUsed(boolean used) {
        this.used = used;
    }
    
    public String toString() {
        return callsign+( ssid == "" ? "" : "-" )+ssid+ ( isUsed() ? "*":"");
    }
}
