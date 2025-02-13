/*
 * javAPRSlib - https://github.com/ab0oo/javAPRSlib
 *
 * Copyright (C) 2011, 2024 John Gorkos, AB0OO
 *
 * javAPRSlib is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * javAPRSlib is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */
package net.ab0oo.aprs.parser;

import java.util.ArrayList;

/**
 * <p>Digipeater class.</p>
 *
 * @author johng
 *         This class represents a single digipeater in a TNC2-format VIA
 *         string.
 * @version $Id: $Id
 */
public class Digipeater extends Callsign {
    private static final long serialVersionUID = 1L;
    /**
     * flag indicating that this digipeater has seen and processed the message
     * and has retransmitted it.
     */
    private boolean used;

    /**
     * <p>Constructor for Digipeater.</p>
     *
     * @param call a {@link java.lang.String} object
     */
    public Digipeater(String call) {
        super(call.replaceAll("\\*", ""));
        if (call.indexOf("*") >= 0) {
            setUsed(true);
        }
    }

    /**
     * <p>Constructor for Digipeater.</p>
     *
     * @param data an array of {@link byte} objects
     * @param offset a int
     */
    public Digipeater(byte[] data, int offset) {
        super(data, offset);
        this.used = (data[offset + 6] & 0x80) == 0x80;
    }

    /**
     * parse a comma-separated list of digipeaters
     *
     * @return the list of digipeaters as an array
     * @param digiList a {@link java.lang.String} object
     * @param includeFirst a boolean
     */
    public static ArrayList<Digipeater> parseList(String digiList, boolean includeFirst) {
        String[] digiTemp = digiList.split(",");
        ArrayList<Digipeater> digis = new ArrayList<Digipeater>();
        boolean includeNext = includeFirst;
        // for now, '*' is set for all digis with used bit.
        // however, only the last used digi should have a '*'
        for (String digi : digiTemp) {
            String digiTrim = digi.trim();
            if (digiTrim.length() > 0 && includeNext)
                digis.add(new Digipeater(digiTrim));
            includeNext = true;
        }
        return digis;
    }

    /**
     * <p>isUsed.</p>
     *
     * @return the used
     */
    public boolean isUsed() {
        return used;
    }

    /**
     * <p>Setter for the field <code>used</code>.</p>
     *
     * @param used the used to set
     */
    public void setUsed(boolean used) {
        this.used = used;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return super.toString() + (isUsed() ? "*" : "");
    }

    /** {@inheritDoc} */
    @Override
    public byte[] toAX25() throws IllegalArgumentException {
        byte[] ax25 = super.toAX25();
        ax25[6] |= (isUsed() ? 0x80 : 0);
        return ax25;
    }
}
