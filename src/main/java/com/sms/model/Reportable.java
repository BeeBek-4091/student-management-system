package com.sms.model;

/**
 * Any model class that can describe itself in a report line implements this.
 * Student and Course both implement it, each in its own way (polymorphism).
 */
public interface Reportable {
    String toReportLine();
}
