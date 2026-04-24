package data_management;

import com.data_management.*;

import com.alerts.AlertGenerator;
import com.alerts.Alert;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AlertGenerator.
 *
 * These tests verify that medical rules correctly trigger alerts
 * based on simulated patient data.
 */
class AlertGeneratorTest {

    /**
     * Tests whether extremely high blood pressure triggers an alert.
     *
     * Rule tested:
     * - Systolic pressure > 180 should generate a CRITICAL alert
     */
    @Test
    void testCriticalBloodPressureAlert() {

        DataStorage storage = new DataStorage();

        storage.addPatientData(1, 200, "SystolicPressure", 1000L);

        AlertGenerator generator = new AlertGenerator(storage);

        generator.evaluateData();

        List<Alert> alerts = generator.getEmittedAlerts();

        assertFalse(alerts.isEmpty(),
                "Critical blood pressure should trigger an alert");
    }

    /**
     * Tests low oxygen saturation alert (<92%).
     * Rule tested:
     * - Saturation below 92% should generate LOW_SATURATION alert
     */
    @Test
    void testLowSaturationAlert() {

        DataStorage storage = new DataStorage();

        storage.addPatientData(1, 85, "Saturation", 1000L);

        AlertGenerator generator = new AlertGenerator(storage);

        generator.evaluateData();

        assertTrue(
                generator.getEmittedAlerts().stream()
                        .anyMatch(a -> a.getCondition().contains("LOW_SATURATION")),
                "Low saturation should trigger alert"
        );
    }

  /**
     * Tests combined emergency condition:
     * - Low blood pressure AND low oxygen saturation
     *
     * This represents a critical medical emergency.
     */
    @Test
    void testCombinedHypotensiveHypoxemiaAlert() {

        DataStorage storage = new DataStorage();

        storage.addPatientData(1, 85, "Saturation", 1000L);
        storage.addPatientData(1, 80, "SystolicPressure", 1000L);

        AlertGenerator generator = new AlertGenerator(storage);

        generator.evaluateData();

        assertTrue(
                generator.getEmittedAlerts().stream()
                        .anyMatch(a -> a.getCondition().contains("HYPOTENSIVE_HYPOXEMIA")),
                "Combined condition should trigger critical alert"
        );
    }
}

