package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when some thresholds are met.
 *
 * <p>
 * It evaluates patient records stored in {@link DataStorage} detects 
 * abnormal conditions such as blood pressure anomalies, oxygen saturation drops, ECG irregularities, and combined risk
 * conditions.
 * </p>
 */
public class AlertGenerator {

    /** Reference to the system's central data storage. */
    private DataStorage dataStorage;

    /** List of all alerts generated during evaluation. */
    private final List<Alert> emittedAlerts = new ArrayList<>();

    /**
     * Constructs an {@code AlertGenerator} with access to patient data.
     *
     * @param dataStorage the data storage system used to retrieve patient records
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Returns all alerts generated during evaluation.
     *
     * @return a list of emitted {@link Alert} objects
     */
    public List<Alert> getEmittedAlerts() {
        return emittedAlerts;
    }

    /**
     * Evaluates all patient records and checks for any medical
     * conditions that require alerts.
     *
     * <p>
     * This method executes all detection rules such as:
     * blood pressure monitoring, blood oxygen saturation checks, ECG anomaly
     * detection, and combined critical condition detection.
     * </p>
     * 
     *Evaluates all patients stored in DataStorage and checks for alerts.
     *
     * @param patient the patient whose records are being checked
     */
    public void evaluateData() {

        List<Patient> patients = dataStorage.getAllPatients();
         
         for (Patient patient : patients) {

        List<PatientRecord> records =
                patient.getRecords(0, Long.MAX_VALUE);

        if (records == null || records.isEmpty()) continue;

        checkBloodPressure(patient.getPatientId(), records);
        checkBloodSaturation(patient.getPatientId(), records);
        checkECG(patient.getPatientId(), records);
        checkTriggeredAlert(patient.getPatientId(), records);
         }
    }

    /**
     * Evaluates systolic and diastolic blood pressure readings for:
     * <ul>
     *     <li>Critical threshold violations</li>
     *     <li>Rapid increasing or decreasing trends</li>
     * </ul>
     *
     * @param patientId patient identifier
     * @param records   list of patient records
     */
    private void checkBloodPressure(int patientId, List<PatientRecord> records) {

        List<Double> systolic = new ArrayList<>();
        List<Double> diastolic = new ArrayList<>();

        for (PatientRecord r : records) {
            if (r.getRecordType().equals("SystolicPressure")) {
                systolic.add(r.getMeasurementValue());
            }
            if (r.getRecordType().equals("DiastolicPressure")) {
                diastolic.add(r.getMeasurementValue());
            }
        }

        checkBPThreshold(patientId, systolic, "SystolicPressure", 90, 180);
        checkBPThreshold(patientId, diastolic, "DiastolicPressure", 60, 120);

        checkTrend(patientId, systolic, "SystolicPressure");
        checkTrend(patientId, diastolic, "DiastolicPressure");
    }

    /**
     * Checks whether blood pressure values exceed given thresholds.
     *
     * @param patientId patient identifier
     * @param values    list of blood pressure readings
     * @param type      type of blood pressure (systolic/diastolic)
     * @param low       lower safe threshold
     * @param high      upper safe threshold
     */
    private void checkBPThreshold(int patientId, List<Double> values, String type,
                                  double low, double high) {

        for (double v : values) {
            if (v < low || v > high) {
                triggerAlert(new Alert(
                        String.valueOf(patientId),
                        "BLOOD_PRESSURE_CRITICAL",
                        System.currentTimeMillis()
                ));
                return;
            }
        }
    }

    /**
     * Detects upward or downward trends in blood pressure readings.
     *
     * <p>
     * A trend is detected if three consecutive readings each differ by more than
     * ±10 mmHg compared to the previous value.
     * </p>
     *
     * @param patientId patient identifier
     * @param values    list of readings
     * @param type      measurement type
     */
    private void checkTrend(int patientId, List<Double> values, String type) {

        if (values.size() < 3) return;

        for (int i = 2; i < values.size(); i++) {

            double a = values.get(i - 2);
            double b = values.get(i - 1);
            double c = values.get(i);

            boolean increasing = (b - a > 10) && (c - b > 10);
            boolean decreasing = (a - b > 10) && (b - c > 10);

            if (increasing || decreasing) {
                triggerAlert(new Alert(
                        String.valueOf(patientId),
                        "BP_TREND_ALERT",
                        System.currentTimeMillis()
                ));
                return;
            }
        }
    }

    /**
     * Evaluates blood oxygen saturation levels for:
     * <ul>
     *     <li>Low saturation conditions (<92%)</li>
     *     <li>Rapid drops within a 10-minute interval</li>
     * </ul>
     *
     * @param patientId patient identifier
     * @param records   list of patient records
     */
    private void checkBloodSaturation(int patientId, List<PatientRecord> records) {

        List<PatientRecord> sats = new ArrayList<>();

        for (PatientRecord r : records) {
            if (r.getRecordType().equals("Saturation")) {
                sats.add(r);
            }
        }

        for (PatientRecord r : sats) {
            if (r.getMeasurementValue() < 92) {
                triggerAlert(new Alert(
                        String.valueOf(patientId),
                        "LOW_SATURATION",
                        System.currentTimeMillis()
                ));
            }
        }

        for (int i = 1; i < sats.size(); i++) {

            PatientRecord prev = sats.get(i - 1);
            PatientRecord curr = sats.get(i);

            long timeDiff = curr.getTimestamp() - prev.getTimestamp();
            double drop = prev.getMeasurementValue() - curr.getMeasurementValue();

            if (timeDiff <= 600_000 && drop >= 5) {
                triggerAlert(new Alert(
                        String.valueOf(patientId),
                        "RAPID_DROP_SATURATION",
                        System.currentTimeMillis()
                ));
            }
        }
    }

    /**
     * Detects abnormal ECG behavior using deviation from average signal value.
     *
     * <p>
     * An alert is triggered when any ECG reading deviates by a large amount
     * (greater than 50%) from the average.
     * </p>
     *
     * @param patientId patient identifier
     * @param records   list of patient records
     */
    private void checkECG(int patientId, List<PatientRecord> records) {

        List<Double> ecg = new ArrayList<>();

        for (PatientRecord r : records) {
            if (r.getRecordType().equals("ECG")) {
                ecg.add(r.getMeasurementValue());
            }
        }

        if (ecg.size() < 5) return;

        double avg = ecg.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0);

        for (double v : ecg) {
            if (Math.abs(v - avg) > avg * 0.5) {
                triggerAlert(new Alert(
                        String.valueOf(patientId),
                        "ABNORMAL_ECG_PEAK",
                        System.currentTimeMillis()
                ));
                return;
            }
        }
    }

    /**
     * Detects combined critical condition:
     * low blood pressure and low oxygen saturation.
     *
     * <p>
     * This condition indicates potential health issue.
     * </p>
     *
     * @param patientId patient identifier
     * @param records   list of patient records
     */
    private void checkTriggeredAlert(int patientId, List<PatientRecord> records) {

        boolean lowBP = false;
        boolean lowSat = false;

        for (PatientRecord r : records) {

            if (r.getRecordType().equals("SystolicPressure")
                    && r.getMeasurementValue() < 90) {
                lowBP = true;
            }

            if (r.getRecordType().equals("Saturation")
                    && r.getMeasurementValue() < 92) {
                lowSat = true;
            }
        }

        if (lowBP && lowSat) {
            triggerAlert(new Alert(
                    String.valueOf(patientId),
                    "HYPOTENSIVE_HYPOXEMIA",
                    System.currentTimeMillis()
            ));
        }
    }

    /**
     * Triggers and registers an alert in the system.
     *
     * <p>
     * This method acts as the central alert dispatch block. It stores the
     * alert internally and prints a log entry for monitoring purposes.
     * </p>
     *
     * @param alert the alert object containing condition details
     */
    private void triggerAlert(Alert alert) {
        emittedAlerts.add(alert);

        System.out.println(
                "[ALERT] Patient=" + alert.getPatientId() +
                        " Condition=" + alert.getCondition() +
                        " Timestamp=" + alert.getTimestamp()
        );
    }
}