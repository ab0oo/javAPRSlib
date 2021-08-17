package net.ab0oo.aprs.parser;

public class WeatherField extends APRSData {
    private Integer windDirection;
    private Integer windSpeed;
    private Integer windGust;
    private Integer temp;
    private Float rainLastHour;
    private Float rainLast24Hours;
    private Float rainSinceMidnight;
    private Float humidity;
    private Float pressure;
    private Integer luminosity = 0;
    private Integer snowfallLast24Hours = 0;
    private Integer rawRainCounter = 0;


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("---WEATHER---\n");
        sb.append("Wind Direction:\t"+this.windDirection+"\n");
        sb.append("Wind Speed:\t"+this.windSpeed+"\n");
        sb.append("Wind Gust:\t"+this.windGust+"\n");
        sb.append("Temperature:\t"+this.temp+"\n");
        sb.append("Rain last hour:\t"+this.rainLastHour+"\n");
        sb.append("Rain 24 hours:\t"+this.rainLast24Hours+"\n");
        sb.append("Rain since 00:00:\t"+this.rainSinceMidnight+"\n");
        sb.append("Humidity:\t"+this.humidity+"\n");
        sb.append("Pressure:\t"+this.pressure+"\n");
        return sb.toString();
    }

    public Integer getWindDirection() {
        return this.windDirection;
    }

    public void setWindDirection(Integer windDirection) {
        this.windDirection = windDirection;
    }

    public Integer getWindSpeed() {
        return this.windSpeed;
    }

    public void setWindSpeed(Integer windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Integer getWindGust() {
        return this.windGust;
    }

    public void setWindGust(Integer windGust) {
        this.windGust = windGust;
    }

    public Integer getTemp() {
        return this.temp;
    }

    public void setTemp(Integer temp) {
        this.temp = temp;
    }

    public Float getRainLastHour() {
        return this.rainLastHour;
    }

    public void setRainLastHour(Float rainLastHour) {
        this.rainLastHour = rainLastHour;
    }

    public Float getRainLast24Hours() {
        return this.rainLast24Hours;
    }

    public void setRainLast24Hours(Float rainLast24Hours) {
        this.rainLast24Hours = rainLast24Hours;
    }

    public Float getRainSinceMidnight() {
        return this.rainSinceMidnight;
    }

    public void setRainSinceMidnight(Float rainSinceMidnight) {
        this.rainSinceMidnight = rainSinceMidnight;
    }

    public Float getHumidity() {
        return this.humidity;
    }

    public void setHumidity(Float humidity) {
        this.humidity = humidity;
    }

    public Float getPressure() {
        return this.pressure;
    }

    public void setPressure(Float pressure) {
        this.pressure = pressure;
    }

    public Integer getLuminosity() {
        return this.luminosity;
    }

    public void setLuminosity(Integer luminosity) {
        this.luminosity = luminosity;
    }

    public Integer getSnowfallLast24Hours() {
        return this.snowfallLast24Hours;
    }

    public void setSnowfallLast24Hours(Integer snowfallLast24Hours) {
        this.snowfallLast24Hours = snowfallLast24Hours;
    }

    public Integer getRawRainCounter() {
        return this.rawRainCounter;
    }

    public void setRawRainCounter(Integer rawRainCounter) {
        this.rawRainCounter = rawRainCounter;
    }

    @Override
    public int compareTo(APRSData o) {
        if ( this.getClass().hashCode() > o.getClass().hashCode() ) {
            return 1;
        }
        return -1;
    }
  
    @Override
    public boolean hasFault() {
        return this.hasFault;
    }
}