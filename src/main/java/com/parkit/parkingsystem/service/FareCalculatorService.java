package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.*;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        //int inHour = ticket.getInTime().getHours();
        //int outHour = ticket.getOutTime().getHours();
        //TEST DURATION
        // Duration newDuration = Duration.between(ticket.getInTime().toInstant(), ticket.getOutTime().toInstant());
        //
        double inHour = ticket.getInTime().getTime();
        double outHour = ticket.getOutTime().getTime();

        //TODO: Some tests are failing here. Need to check if this logic is correct

        double duration = (outHour - inHour)/3600000;
        duration *= 100;
        duration = Math.round(duration);
        duration /= 100;
        // round not here, but with price ...
        //double duration = Math.round(((outHour - inHour)/3600000)*100)/100;

        // FPIC-974 Free parking for half hour
        if (duration < 0.5) {
            duration = 0;
        }

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}