package data_management;

import com.data_management.*;
import com.alerts.*;
import com.alerts.rules.*;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for AlertGenerator covering both normal and edge cases.
 *
 * These tests ensure:
 * - Alerts trigger correctly at boundary thresholds
 * - No alerts are triggered when conditions are not met
 * - System behaves safely with minimal or missing data
 * - Combined and multi-condition logic works as expected
 */
class AlertGeneratorTest {

    /**
     * Creates a standard AlertGenerator with all relevant rules.
     * This ensures consistency across tests and isolates rule configuration.
     */
    private AlertGenerator createGenerator(DataStorage storage) {
        return new AlertGenerator(
                storage,
                List.of(
                        new ThresholdRule("SystolicPressure", 90, 180),
                        new ThresholdRule("DiastolicPressure", 60, 120),
                        new CombinedRule(),
                        new TrendRule("SystolicPressure")
                )
        );
    }

    /**
     * Edge case: Tests exact boundary values for blood pressure.
     *
     * Verifies that values exactly equal to thresholds (90 and 180)
     * do NOT trigger alerts, ensuring strict inequality is respected.
     */
    @Test
    void testBloodPressureBoundaryValues() {

        DataStorage storage = new DataStorage();

        storage.addPatientData(1, 90, "SystolicPressure", 1000L);
        storage.addPatientData(1, 180, "SystolicPressure", 2000L);

        AlertGenerator generator = createGenerator(storage);
        generator.evaluateData();

        assertTrue(generator.getEmittedAlerts().isEmpty(),
                "Boundary values should NOT trigger alerts");
    }

    /**
     * Edge case: Tests extreme out-of-range blood pressure values.
     *
     * Verifies system correctly flags dangerous values far beyond thresholds.
     * Ensures no upper/lower bound limitations break detection.
     */
    @Test
    void testExtremeBloodPressureValues() {

        DataStorage storage = new DataStorage();

        storage.addPatientData(1, 300, "SystolicPressure", 1000L);
        storage.addPatientData(1, 20, "SystolicPressure", 2000L);

        AlertGenerator generator = createGenerator(storage);
        generator.evaluateData();

        assertFalse(generator.getEmittedAlerts().isEmpty(),
                "Extreme values must trigger alerts");
    }

    /**
     * Edge case: Tests behavior with no patient data.
     *
     * Ensures system does not crash or produce false alerts
     * when no records are available.
     */
    @Test
    void testNoData() {

        DataStorage storage = new DataStorage();

        AlertGenerator generator = createGenerator(storage);
        generator.evaluateData();

        assertTrue(generator.getEmittedAlerts().isEmpty(),
                "No data should result in no alerts");
    }

    /**
     * Edge case: Tests minimal data (only one reading).
     *
     * Ensures that rules requiring multiple data points (like trends)
     * do not trigger incorrectly.
     */
    @Test
    void testSingleReadingNoTrend() {

        DataStorage storage = new DataStorage();

        storage.addPatientData(1, 120, "SystolicPressure", 1000L);

        AlertGenerator generator = createGenerator(storage);
        generator.evaluateData();

        assertTrue(generator.getEmittedAlerts().isEmpty(),
                "Single reading should NOT trigger trend alerts");
    }

    /**
     * Funcationality test: Verifies increasing trend detection.
     *
     * Uses 3 readings with >10 mmHg increase each time.
     * Confirms trend rule correctly identifies upward patterns.
     */
    @Test
    void testIncreasingTrendAlert() {

        DataStorage storage = new DataStorage();

        storage.addPatientData(1, 100, "SystolicPressure", 1000L);
        storage.addPatientData(1, 115, "SystolicPressure", 2000L);
        storage.addPatientData(1, 130, "SystolicPressure", 3000L);

        AlertGenerator generator = createGenerator(storage);
        generator.evaluateData();

        assertTrue(
                generator.getEmittedAlerts().stream()
                        .anyMatch(a -> a.getCondition().contains("TREND")),
                "Increasing trend should trigger alert"
        );
    }

    /**
     * Edge case: Tests trend values just below threshold.
     *
     * Ensures that changes ≤10 mmHg do NOT trigger alerts,
     * validating correct threshold sensitivity.
     */
    @Test
    void testTrendBelowThreshold() {

        DataStorage storage = new DataStorage();

        storage.addPatientData(1, 100, "SystolicPressure", 1000L);
        storage.addPatientData(1, 108, "SystolicPressure", 2000L);
        storage.addPatientData(1, 115, "SystolicPressure", 3000L);

        AlertGenerator generator = createGenerator(storage);
        generator.evaluateData();

        assertTrue(generator.getEmittedAlerts().isEmpty(),
                "Trend below threshold should NOT trigger alert");
    }

    /**
     * Funcationality test: Low saturation detection.
     *
     * Confirms that saturation below 92% is correctly flagged.
     */
    @Test
    void testLowSaturation() {

        DataStorage storage = new DataStorage();

        storage.addPatientData(1, 85, "Saturation", 1000L);

        AlertGenerator generator = createGenerator(storage);
        generator.evaluateData();

        assertTrue(
                generator.getEmittedAlerts().stream()
                        .anyMatch(a -> a.getCondition().contains("LOW_SATURATION"))
        );
    }

    /**
     * Edge case: Saturation exactly at threshold.
     *
     * Ensures value = 92% does NOT trigger alert.
     */
    @Test
    void testSaturationBoundary() {

        DataStorage storage = new DataStorage();

        storage.addPatientData(1, 92, "Saturation", 1000L);

        AlertGenerator generator = createGenerator(storage);
        generator.evaluateData();

        assertTrue(generator.getEmittedAlerts().isEmpty(),
                "Boundary saturation should NOT trigger alert");
    }

    /**
     * Funcationality test: Combined condition alert.
     *
     * Verifies simultaneous low BP and low saturation
     * triggers critical combined alert.
     */
    @Test
    void testCombinedCondition() {

        DataStorage storage = new DataStorage();

        storage.addPatientData(1, 80, "SystolicPressure", 1000L);
        storage.addPatientData(1, 85, "Saturation", 1000L);

        AlertGenerator generator = createGenerator(storage);
        generator.evaluateData();

        assertTrue(
                generator.getEmittedAlerts().stream()
                        .anyMatch(a -> a.getCondition().contains("HYPOTENSIVE_HYPOXEMIA"))
        );
    }
}
