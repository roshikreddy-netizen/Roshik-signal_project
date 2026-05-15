package data_management;

import com.data_management.*;
import com.alerts.*;
import com.alerts.rules.*;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for AlertGenerator covering both normal and edge cases.
 *
 * These tests ensure:
 * - Alerts trigger correctly at boundary thresholds
 * - No alerts are triggered when conditions are not met
 * - System behaves safely with minimal or missing data
 * - Combined and multi-condition logic works as expected
 *
 * 
 * - Unit tests validate expected system behavior
 * - Explicit checks reduce ambiguous test results
 * - Edge cases improve system reliability
 */
class AlertGeneratorTest {

    /**
     * Creates a standard AlertGenerator with all relevant rules.
     * This ensures consistency across tests and isolates rule configuration.
     *
     * 
     * - Reusing setup logic avoids duplicated code
     * - Supports maintainability and clean test structure
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
     * do not trigger alerts, ensuring strict inequality is respected.
     *
     * 
     * - Boundary testing helps detect comparison logic errors
     */
    @Test
    void testBloodPressureBoundaryValues() {

        DataStorage storage = new DataStorage();

        storage.addPatientData(1, 90, "SystolicPressure", 1000L);
        storage.addPatientData(1, 180, "SystolicPressure", 2000L);

        AlertGenerator generator = createGenerator(storage);
        generator.evaluateData();

        /**
         * Verifies no alerts were generated.
         * Exact boundary values should remain valid.
         */
        assertEquals(0,
                generator.getEmittedAlerts().size());

        assertTrue(generator.getEmittedAlerts().isEmpty(),
                "Boundary values should NOT trigger alerts");
    }

    /**
     * Edge case: Tests extreme out-of-range blood pressure values.
     *
     * Verifies system correctly flags dangerous values far beyond thresholds.
     * Ensures no upper/lower bound limitations break detection.
     *
     * 
     * - Extreme testing validates system robustness
     */
    @Test
    void testExtremeBloodPressureValues() {

        DataStorage storage = new DataStorage();

        storage.addPatientData(1, 300, "SystolicPressure", 1000L);
        storage.addPatientData(1, 20, "SystolicPressure", 2000L);

        AlertGenerator generator = createGenerator(storage);
        generator.evaluateData();

        /**
         * Verifies alerts were generated correctly.
         * Explicit checks reduce ambiguous test outcomes.
         */
        assertEquals(2,
                generator.getEmittedAlerts().size());

        Alert firstAlert = generator.getEmittedAlerts().get(0);

        /**
         * Verifies correct patient triggered alert.
         */
        assertEquals("1",
                firstAlert.getPatientId());

        assertTrue(generator.getEmittedAlerts().stream()
                        .allMatch(a -> a.getPatientId().equals("1")),
                "All alerts should belong to patient 1");

        assertFalse(generator.getEmittedAlerts().isEmpty(),
                "Extreme values must trigger alerts");
    }

    /**
     * Edge case: Tests behavior with no patient data.
     *
     * Ensures system does not crash or produce false alerts
     * when no records are available.
     *
     * 
     * - Empty input handling improves system stability
     */
    @Test
    void testNoData() {

        DataStorage storage = new DataStorage();

        AlertGenerator generator = createGenerator(storage);
        generator.evaluateData();

        /**
         * Verifies empty datasets produce no alerts.
         */
        assertEquals(0,
                generator.getEmittedAlerts().size());

        assertTrue(generator.getEmittedAlerts().isEmpty(),
                "No data should result in no alerts");
    }

    /**
     * Edge case: Tests minimal data (only one reading).
     *
     * Ensures that rules requiring multiple data points (like trends)
     * do not trigger incorrectly.
     *
     * 
     * - Prevents false positives from incomplete data
     */
    @Test
    void testSingleReadingNoTrend() {

        DataStorage storage = new DataStorage();

        storage.addPatientData(1, 120, "SystolicPressure", 1000L);

        AlertGenerator generator = createGenerator(storage);
        generator.evaluateData();

        /**
         * Verifies trend logic requires enough readings.
         */
        assertEquals(0,
                generator.getEmittedAlerts().size());

        assertTrue(generator.getEmittedAlerts().isEmpty(),
                "Single reading should NOT trigger trend alerts");
    }

    /**
     * Funcationality test: Verifies increasing trend detection.
     *
     * Uses 3 readings with >10 mmHg increase each time.
     * Confirms trend rule correctly identifies upward patterns.
     *
     * WHY:
     * - Validates business logic for trend monitoring
     */
    @Test
    void testIncreasingTrendAlert() {

        DataStorage storage = new DataStorage();

        storage.addPatientData(1, 100, "SystolicPressure", 1000L);
        storage.addPatientData(1, 115, "SystolicPressure", 2000L);
        storage.addPatientData(1, 130, "SystolicPressure", 3000L);

        AlertGenerator generator = createGenerator(storage);
        generator.evaluateData();

        /**
         * Verifies trend alert was generated.
         */
        assertEquals(1,
                generator.getEmittedAlerts().size());

        Alert alert = generator.getEmittedAlerts().get(0);

        /**
         * Verifies correct patient and alert type.
         */
        assertEquals("1",
                alert.getPatientId());

        assertTrue(alert.getCondition().contains("TREND"));

        assertTrue(
                generator.getEmittedAlerts().stream()
                        .anyMatch(a -> a.getCondition().contains("TREND")),
                "Increasing trend should trigger alert"
        );
    }

    /**
     * Edge case: Tests trend values just below threshold.
     *
     * Ensures that changes =<10 mmHg do NOT trigger alerts,
     * validating correct threshold sensitivity.
     *
     * 
     * - Validates strict threshold comparison logic
     */
    @Test
    void testTrendBelowThreshold() {

        DataStorage storage = new DataStorage();

        storage.addPatientData(1, 100, "SystolicPressure", 1000L);
        storage.addPatientData(1, 108, "SystolicPressure", 2000L);
        storage.addPatientData(1, 115, "SystolicPressure", 3000L);

        AlertGenerator generator = createGenerator(storage);
        generator.evaluateData();

        /**
         * Verifies no alert is generated below threshold.
         */
        assertEquals(0,
                generator.getEmittedAlerts().size());

        assertTrue(generator.getEmittedAlerts().isEmpty(),
                "Trend below threshold should NOT trigger alert");
    }


    /**
     * Edge case: Saturation exactly at threshold.
     *
     * Ensures value = 92% does NOT trigger alert.
     *
     * 
     * - Boundary checks improve accuracy of rule validation
     */
    @Test
    void testSaturationBoundary() {

        DataStorage storage = new DataStorage();

        storage.addPatientData(1, 92, "Saturation", 1000L);

        AlertGenerator generator = createGenerator(storage);
        generator.evaluateData();

        /**
         * Verifies threshold equality does not trigger alert.
         */
        assertEquals(0,
                generator.getEmittedAlerts().size());

        assertTrue(generator.getEmittedAlerts().isEmpty(),
                "Boundary saturation should NOT trigger alert");
    }

    /**
     * Funcationality test: Combined condition alert.
     *
     * Verifies simultaneous low BP and low saturation
     * triggers critical combined alert.
     *
     * - Combined rules validate multi-condition logic
     */
    @Test
    void testCombinedCondition() {

        DataStorage storage = new DataStorage();

        storage.addPatientData(1, 80, "SystolicPressure", 1000L);
        storage.addPatientData(1, 85, "Saturation", 1000L);

        AlertGenerator generator = createGenerator(storage);
        generator.evaluateData();

        /**
         * Verifies exactly one combined alert exists.
         */
        assertEquals(1,
                generator.getEmittedAlerts().size());

        Alert alert = generator.getEmittedAlerts().get(0);

        /**
         * Verifies alert belongs to correct patient.
         */
        assertEquals("1",
                alert.getPatientId());

        assertTrue(
                generator.getEmittedAlerts().stream()
                        .anyMatch(a -> a.getCondition().contains("Hypotensive Hypoxemia"))
        );
    }
}
