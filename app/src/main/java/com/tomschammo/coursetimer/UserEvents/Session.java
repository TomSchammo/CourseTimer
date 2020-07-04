package com.tomschammo.coursetimer.UserEvents;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class Session {

    private int id;
    private String date, timeStart, timeStop, client;
    private double timeTotal, earnings;
}