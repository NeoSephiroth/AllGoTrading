package org.yats.trading.option;

import org.joda.time.DateTime;

public abstract class OptionBase {

    public abstract double getPrice(Parameter p);
    public abstract Greeks getGreeks(Parameter p);
    public abstract boolean isCall();


    public String toCallPutString() {
        return (isCall() ? "CALL" : "PUT ");
    }

    public double getStrikePrice() {
        return strikePrice;
    }

    public DateTime getExpiration() {
        return expiration;
    }

    @Override
    public String toString() {
        return "OptionBase{" +
                "strikePrice=" + strikePrice +
                ", expiration=" + expiration +
                ", " + toCallPutString() +
                '}';
    }

    public double calcNormalized(double z) {
        if (z >  6.0) { return 1.0; }// this guards against overflow
        if (z < -6.0) { return 0.0; }

        double b1 =  0.31938153;
        double b2 = -0.356563782;
        double b3 =  1.781477937;
        double b4 = -1.821255978;
        double b5 =  1.330274429;
        double p  =  0.2316419;
        double c2 =  0.3989423;

        double a=Math.abs(z);
        double t = 1.0/(1.0+a*p);
        double b = c2*Math.exp((-z) * (z / 2.0));
        double n = ((((b5*t+b4)*t+b3)*t+b2)*t+b1)*t;
        n = 1.0-b*n;
        if ( z < 0.0 ) n = 1.0 - n;
        return n;
    }


    public double calcNormalDistribution(double z)  {  // normal distribution function
        return (1.0/Math.sqrt(2.0 * Math.PI))*Math.exp(-0.5 * z * z);
    }


    protected double strikePrice;
    protected DateTime expiration;


} // class
