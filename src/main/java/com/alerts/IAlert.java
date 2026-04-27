package com.alerts;

/**
 * Interface for all alert types.
 * Allows decorators to wrap alerts without modifying base class.
 */
public interface IAlert {

    String getPatientId();

    String getCondition();

    long getTimestamp();
}
