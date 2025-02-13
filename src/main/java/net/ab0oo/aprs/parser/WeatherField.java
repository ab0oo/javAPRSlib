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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * <p>WeatherField class.</p>
 *
 * @author john
 * @version $Id: $Id
 */
public class WeatherField extends APRSData {
    /**
     * wind direction, in degrees, 0-359
     */
    private Integer windDirection;
    /**
     * Sustained 1-minute wind speed, in MPH
     */
    private Integer windSpeed;
    /**
     * Peak wind speed in last 5 minutes
     */
    private Integer windGust;
    /**
     * temperature, in degrees F.  Temps below 0 are expressed as -01 to -99
     */
    private Integer temp;
    /**
     * Rainfall, in hundredths of an inch, in the last hour
     */
    private Double rainLastHour;
    /**
     * Rainfall, in hundredths of an inch, in the last 24 hours
     */
    private Double rainLast24Hours;
    /**
     * Rainfall, in hundredths of an inch, since midnight local
     */
    private Double rainSinceMidnight;
    /**
     * humidity, in %.  00 = 100% humidity
     */
    private Double humidity;
    /**
     * Barometric Pressure, in tenths of millibars/tenths of hPascal
     */
    private Double pressure;
    /**
     * Luminosity, in watts per square meter
     */
    private Integer luminosity;
    /**
     * snowfall in last 24 hours, in inches
     */
    private Double snowfallLast24Hours;
    /**
     * Raw rain counter ()
     */
    private Integer rawRainCounter;

    /**
     * <p>round.</p>
     *
     * @param d a {@link java.lang.Double} object
     * @param decimalPlace a int
     * @return Double
     */
    public static Double round(Double d, int decimalPlace) {
        if (d == null)
            return null;
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("---WEATHER---\n");
        sb.append("Wind Direction:\t" + this.windDirection + "\n");
        sb.append("Wind Speed:\t" + this.windSpeed + "\n");
        sb.append("Wind Gust:\t" + this.windGust + "\n");
        sb.append("Temperature:\t" + this.temp + "\n");
        sb.append("Rain last hour:\t" + this.rainLastHour + "\n");
        sb.append("Rain 24 hours:\t" + this.rainLast24Hours + "\n");
        sb.append("Rain since 00:00:\t" + this.rainSinceMidnight + "\n");
        sb.append("Humidity:\t" + this.humidity + "\n");
        sb.append("Pressure:\t" + this.pressure + "\n");
        sb.append("Luminosity:\t" + this.luminosity + "\n" );
        sb.append("Snowfall 24 hours:\t" + this.snowfallLast24Hours + "\n" );
        return sb.toString();
    }

    /**
     * <p>Getter for the field <code>windDirection</code>.</p>
     *
     * @return Integer direction wind is blowing FROM
     *
     * Gets the wind direction from the WeatherField object
     */
    public Integer getWindDirection() {
        return this.windDirection;
    }

    /**
     * <p>Setter for the field <code>windDirection</code>.</p>
     *
     * @param windDirection direction wind is blowing FROM
     *
     * Sets the wind direction in a new WeatherField object
     */
    public void setWindDirection(Integer windDirection) {
        this.windDirection = windDirection;
    }

    /**
     * <p>Getter for the field <code>windSpeed</code>.</p>
     *
     * @return Integer sustained 1 minute wind speed in miles per hour
     *
     * Gets the sustained 1 minute wind speed from the object
     */
    public Integer getWindSpeed() {
        return this.windSpeed;
    }

    /**
     * <p>Setter for the field <code>windSpeed</code>.</p>
     *
     * @param windSpeed sustained 1 minute wind speed in miles per hour
     *
     * Sets the sustained 1 minute wind speed in a new object
     */
    public void setWindSpeed(Integer windSpeed) {
        this.windSpeed = windSpeed;
    }

    /**
     * <p>Getter for the field <code>windGust</code>.</p>
     *
     * @return Integer gust (peak wind speed in last 5 minutes) in miles per hour
     */
    public Integer getWindGust() {
        return this.windGust;
    }

    /**
     * <p>Setter for the field <code>windGust</code>.</p>
     *
     * @param windGust gust (peak wind speed in last 5 minutes) in miles per hour
     */
    public void setWindGust(Integer windGust) {
        this.windGust = windGust;
    }

    /**
     * <p>Getter for the field <code>temp</code>.</p>
     *
     * @return Integer temperature in degrees Fahrenheit
     */
    public Integer getTemp() {
        return this.temp;
    }

    /**
     * <p>Setter for the field <code>temp</code>.</p>
     *
     * @param temp temperature in degrees Fahrenheit
     */
    public void setTemp(Integer temp) {
        this.temp = temp;
    }

    /**
     * <p>Getter for the field <code>rainLastHour</code>.</p>
     *
     * @return Double rainfall in hundredths of an inch in the last hour
     */
    public Double getRainLastHour() {
        return round(this.rainLastHour, 2);
    }

    /**
     * <p>Setter for the field <code>rainLastHour</code>.</p>
     *
     * @param rainLastHour rainfall in hundredths of an inch in the last hour
     */
    public void setRainLastHour(Double rainLastHour) {
        this.rainLastHour = rainLastHour;
    }

    /**
     * <p>Getter for the field <code>rainLast24Hours</code>.</p>
     *
     * @return Double rainfall in hundredths of an inch in the last 24 hours
     */
    public Double getRainLast24Hours() {
        return round(this.rainLast24Hours, 2);
    }

    /**
     * <p>Setter for the field <code>rainLast24Hours</code>.</p>
     *
     * @param rainLast24Hours rainfall in hundredths of an inch in the last 24 hours
     */
    public void setRainLast24Hours(Double rainLast24Hours) {
        this.rainLast24Hours = rainLast24Hours;
    }

    /**
     * <p>Getter for the field <code>rainSinceMidnight</code>.</p>
     *
     * @return Double rainfall in hundredths of an inch since local midnight
     */
    public Double getRainSinceMidnight() {
        return round(this.rainSinceMidnight, 2);
    }

    /**
     * <p>Setter for the field <code>rainSinceMidnight</code>.</p>
     *
     * @param rainSinceMidnight rainfall in hundredths of an inch since local midnight
     */
    public void setRainSinceMidnight(Double rainSinceMidnight) {
        this.rainSinceMidnight = rainSinceMidnight;
    }

    /**
     * <p>Getter for the field <code>humidity</code>.</p>
     *
     * @return Double humidity in percent.  00 = 100%
     */
    public Double getHumidity() {
        return round(this.humidity, 2);
    }

    /**
     * <p>Setter for the field <code>humidity</code>.</p>
     *
     * @param humidity humidity in percent.  00 = 100%
     */
    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    /**
     * <p>Getter for the field <code>pressure</code>.</p>
     *
     * @return Double barometric pressure in 10th of millibars
     */
    public Double getPressure() {
        return this.pressure;
    }

    /**
     * <p>Setter for the field <code>pressure</code>.</p>
     *
     * @param pressure barometric pressure in 10th of millibars
     */
    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    /**
     * <p>Getter for the field <code>luminosity</code>.</p>
     *
     * @return Integer luminosity in watts per square meter (&lt;=999)
     */
    public Integer getLuminosity() {
        return this.luminosity;
    }

    /**
     * <p>Setter for the field <code>luminosity</code>.</p>
     *
     * @param luminosity luminosity in watts per square meter (&lt;=999)
     */
    public void setLuminosity(Integer luminosity) {
        if ( luminosity < 0 ) luminosity = 0;
        this.luminosity = luminosity;
    }

    /**
     * <p>Getter for the field <code>snowfallLast24Hours</code>.</p>
     *
     * @return Double snowfall in inches in the last 24 hours
     */
    public Double getSnowfallLast24Hours() {
        return round(this.snowfallLast24Hours, 1);
    }

    /**
     * <p>Setter for the field <code>snowfallLast24Hours</code>.</p>
     *
     * @param snowfallLast24Hours snowfall in inches in the last 24 hours
     */
    public void setSnowfallLast24Hours(Double snowfallLast24Hours) {
        this.snowfallLast24Hours = snowfallLast24Hours;
    }

    /**
     * <p>Getter for the field <code>rawRainCounter</code>.</p>
     *
     * @return Integer raw rain counter number
     */
    public Integer getRawRainCounter() {
        return this.rawRainCounter;
    }

    /**
     * <p>Setter for the field <code>rawRainCounter</code>.</p>
     *
     * @param rawRainCounter raw rain counter number
     */
    public void setRawRainCounter(Integer rawRainCounter) {
        this.rawRainCounter = rawRainCounter;
    }

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof WeatherField)) {
            return false;
        }
        WeatherField weatherField = (WeatherField) o;
        return Objects.equals(windDirection, weatherField.windDirection)
                && Objects.equals(windSpeed, weatherField.windSpeed) && Objects.equals(windGust, weatherField.windGust)
                && Objects.equals(temp, weatherField.temp) && Objects.equals(rainLastHour, weatherField.rainLastHour)
                && Objects.equals(rainLast24Hours, weatherField.rainLast24Hours)
                && Objects.equals(rainSinceMidnight, weatherField.rainSinceMidnight)
                && Objects.equals(humidity, weatherField.humidity) && Objects.equals(pressure, weatherField.pressure)
                && Objects.equals(luminosity, weatherField.luminosity)
                && Objects.equals(snowfallLast24Hours, weatherField.snowfallLast24Hours)
                && Objects.equals(rawRainCounter, weatherField.rawRainCounter);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(windDirection, windSpeed, windGust, temp, rainLastHour, rainLast24Hours, rainSinceMidnight,
                humidity, pressure, luminosity, snowfallLast24Hours, rawRainCounter);
    }

}
