package net.ab0oo.aprs.parser;

public class BadData extends APRSData {

    public BadData() {
        super.setHasFault(true);
    }

    @Override
    public String toString() {
        return "Unable to decode this packet";
    }
    
}
