
package net.ab0oo.aprs.parser;

public class ParseResult {
    private boolean hasFault;
    private String faultString = "";

    public ParseResult( boolean _hasFault, String _faultString ) {
        this.hasFault = _hasFault;
        this.faultString = _faultString;
    }
    
    /**
     * return boolean true if a fault was detected during parsing
     */
    public boolean hasFault() {
        return hasFault;
    }

    /**
     * return String the root cause of any decoding faults
     */
    String getFaultString() {
        return faultString;
    }
}
