package com.example.requestthrottle.model;

import java.util.ArrayDeque;
import java.util.Queue;

public class Counter {
    private static final Long SEC_IN_MIN = 60L;
    private static final Long MILLIS_IN_SEC = 1000L;
    private final Queue<Long> visitTimes = new ArrayDeque<>();

    public int getVisitCount(Long timePeriodInMinutes) {
        Long timePeriodInSec = SEC_IN_MIN * timePeriodInMinutes;
        while ((visitTimes.peek() + timePeriodInSec) < (System.currentTimeMillis() / MILLIS_IN_SEC)) {
            visitTimes.poll();
        }
        return visitTimes.size();
    }

    public void visit() {
        visitTimes.add(System.currentTimeMillis() / 1000);
    }
}
