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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

/**
 * this class represents all of the various ways the time can be encoded into an APRS message
 * n.b. according to the spec, timestamps should really only be used for historical data, not
 * current data.  positions/objects/reports sent with no timestamp should be considered "current"
 * and those sent with a timestamp should be considered historical
 */
public class TimeField extends APRSData {
    private Calendar reportedTimestamp;

    /**
     * build a new timefield set to the current UTC time
     */
    public TimeField() {
        reportedTimestamp = Calendar.getInstance();
        reportedTimestamp.setTimeZone(TimeZone.getTimeZone("UTC"));
    }
        
    /** 
     * @param msgBody char[] of the complete message body
     * @param startPos where to start looking for a time field in the msgBody
     * 
     * Common constructor for a TimeField object.
     */
    public TimeField(byte[] msgBody, int startPos) {
        char dti=(char)msgBody[0];
        int cursor = startPos;
        byte timeIndicator = 'z';
        try {
            timeIndicator = msgBody[7];
        } catch ( IndexOutOfBoundsException iobe ) {
            this.setHasFault( true );
            this.setFaultReason("Index out of bounds parsing time field");
        }
        if (dti == '/' || dti == '@') {
            /*
             * From the protocol spec, chapter 6, there are 3 different timestamp formats:
             * DHM: fixed 7 character, Day Hour Minute, either zulu or local 
             * HMS: fixed 7 character, Hour Minute Second, always ZULU 
             * MDHM: fixed _8_ character zulu timestamp
             */
            Calendar.Builder cb = new Calendar.Builder();
            switch (timeIndicator) {
                case 'z': {
                    // DHM zulu time
                    Calendar c = Calendar.getInstance();
                    c.setLenient(true);
                    c.setTimeZone(TimeZone.getTimeZone("GMT"));
                    c.setTime(new Date(System.currentTimeMillis()));
                    int currentMonth = c.get(Calendar.MONTH);
                    int currentDay = c.get(Calendar.DAY_OF_MONTH);
                    int msgDay = (msgBody[1]-'0')*10 + ((short)msgBody[2]-'0');
                    // since it's possible we're reading this message some time after it was actually sent
                    // (i.e. from a testing file), we need to make sure we do the best we can to get 
                    // the month correct.  For example, the test file is from the end of July, but if
                    // it's read during the beginning of August, messages sent on July 29 will be 
                    // stamped with AUG 29 unless we do this check
                    if ( msgDay > currentDay ) {
                        currentMonth-=1;
                    }
                    c.set(Calendar.MONTH, currentMonth);
                    c.set(Calendar.DAY_OF_MONTH, msgDay);
                    c.set(Calendar.HOUR_OF_DAY, (short)(msgBody[3]-'0')*10 + ((short)msgBody[4]-'0'));
                    c.set(Calendar.MINUTE, (short)(msgBody[5]-'0')*10 + (short)(msgBody[6]-'0'));
                    c.set(Calendar.SECOND, 0);
                    this.reportedTimestamp = c;
                    cursor += 7;
                    break;
                }
                case '/': {
                    // Well, this makes it hard...  We're supposed to calculate the GMT offset from the
                    // positional data in the packet, then apply the local time zone to the DHM data
                    // to extract the zulu time this packet was sent.
                    // TODO - load geospatial representations of all timezones, then use the location
                    // of this station to figure out what their "local" time is.  For now, we fake it.
                    this.reportedTimestamp = cb.build();
                    cursor += 7;
                    break;
                }
                case 'h': {
                    // HMS zulu time.
                    this.reportedTimestamp = cb.build();
                    cursor += 7;
                    break;
                }
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9': {
                    // this is for the funky case of MHDM format, always in Zulu.
                    this.reportedTimestamp = cb.build();
                    cursor += 8;
                    break;								
                }
                default: {
                    this.reportedTimestamp = cb.build();
                    this.reportedTimestamp.setTimeInMillis(0);
                    cursor += 7;
                }
            }
        }
        setLastCursorPosition(cursor);
    }

    
    /** 
     * @return Calendar
     * returns the reported timestamp from the message
     */
    public Calendar getReportedTimestamp() {
        return this.reportedTimestamp;
    }

    
    /** 
     * @return String
     * returns a string representation of the time field
     */
    @Override
    public String toString() {
        SimpleDateFormat f = new SimpleDateFormat("dd HH:MM");          
        StringBuffer sb = new StringBuffer("---TIMESTAMP---\n");
        sb.append("Reported Timestamp: "+f.format(reportedTimestamp.getTime())+"\n");
        return sb.toString();
    }

    
    /** 
     * @param o the object to compare to
     * @return int
     * returns 0 if the two objects are identical, else 1
     */
    @Override
    public int compareTo(APRSData o) {
        if (this.hashCode() > o.hashCode()) {
            return 1;
        }
        if (this.hashCode() == o.hashCode()) {
            return 0;
        }
        return -1;
    }

    /** 
     * @param o a TimeField object to be compared to
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof TimeField)) {
            return false;
        }
        TimeField timeField = (TimeField) o;
        return Objects.equals(reportedTimestamp, timeField.reportedTimestamp);
    }

    
    /** 
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(reportedTimestamp);
    }

 }
