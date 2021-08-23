package net.ab0oo.aprs.parser;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

public class TimeField extends APRSData {
    private Date reportedTimestamp;

    public TimeField() {
        
    }
    
    public static TimeField parse(byte[] msgBody, int startPos) {
        TimeField tf = new TimeField();
        char dti=(char)msgBody[0];
        int cursor = startPos;
        byte timeIndicator = 'z';
        try {
            timeIndicator = msgBody[7];
        } catch ( IndexOutOfBoundsException iobe ) {
            tf.hasFault = true;
            return tf;
        }
        if (dti == '/' || dti == '@') {
            /*
             * From the protocol spec, chapter 6, there are 3 different timestamp formats:
             * DHM: fixed 7 character, Day Hour Minute, either zulu or local 
             * HMS: fixed 7 character, Hour Minute Second, always ZULU 
             * MDHM: fixed _8_ character zulu timestamp
             */
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
                    tf.reportedTimestamp = c.getTime();
                    cursor += 7;
                    break;
                }
                case '/': {
                    // Well, this makes it hard...  We're supposed to calculate the GMT offset from the
                    // positional data in the packet, then apply the local time zone to the DHM data
                    // to extract the zulu time this packet was sent.
                    // TODO - load geospatial representations of all timezones, then use the location
                    // of this station to figure out what their "local" time is.  For now, we fake it.
                    tf.reportedTimestamp = new Date(System.currentTimeMillis());
                    cursor += 7;
                    break;
                }
                case 'h': {
                    // HMS zulu time.
                    tf.reportedTimestamp = new Date(System.currentTimeMillis());
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
                    tf.reportedTimestamp = new Date(System.currentTimeMillis());
                    cursor += 8;
                    break;								
                }
                default: {
                    tf.reportedTimestamp = new Date(0);
                    cursor += 7;
                }
            }
        }
        tf.setLastCursorPosition(cursor);
        return tf;
    }

    public Date getReportedTimestamp() {
        return this.reportedTimestamp;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("---TIMESTAMP---\n");
        sb.append("Reported Timestamp: "+this.reportedTimestamp+"\n");
        return sb.toString();
    }

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

    @Override
    public boolean hasFault() {
        return this.hasFault;
    }

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

    @Override
    public int hashCode() {
        return Objects.hashCode(reportedTimestamp);
    }

 }
