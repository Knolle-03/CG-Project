package wpcg.bezier.algorithms;

import com.jme3.math.Vector2f;

import java.util.*;

/**
 * This class DeCasteljau contains methods to calculate a bezier curve using the de casteljau algorithm
 *  @author Lennart Draeger
 */

public class DeCasteljau {

    private final ArrayList<Vector2f> controlPoints;
    private final HashMap<Float, Vector2f> curvePointsForCurrent_t = new LinkedHashMap<>();
    // maps a given t to a List of helper Points on the control line segments
    private final HashMap<Float, List<Vector2f>> intermediateSteps = new LinkedHashMap<>();
    private final HashMap<Float, Vector2f> curvePointsForSmallest_t = new LinkedHashMap<>();
    // increment of t
    private double increment;


    public DeCasteljau(ArrayList<Vector2f> controlPoints, double increment) {
        if (increment > 1) throw new IllegalArgumentException("increment needs to be <= 1");
        this.controlPoints = controlPoints;
        this.increment = increment;

        for (float t = 0; t <= 1; t += 0.01) {
            // calculate the curve point of t and add it to the hash map
            curvePointsForSmallest_t.put((Math.round(t * 100f) / 100f), calcCurvePoint(t, false));
        }

    }

    public void calcCurvePoints() {
        // for each t <= 1.0
        for (float t = 0; t <= 1; t += increment) {
            // calculate the curve point of t and add it to the hash map
            curvePointsForCurrent_t.put((Math.round(t * 100f) / 100f), calcCurvePoint(t, true));
        }
    }

    // called to recalculate the curve point after every manipulation
    public void reCalcCurvePoints() {
        intermediateSteps.clear();
        curvePointsForCurrent_t.clear();
        curvePointsForSmallest_t.clear();

        if (controlPoints.isEmpty()) {
            controlPoints.add(new Vector2f(0, 0));
        }

        for (float t = 0.0f; t <= 1.0005; t += increment) {
            // calculate the curve point of t and add it to the hash map
            curvePointsForCurrent_t.put((Math.round(t * 100f) / 100f), calcCurvePoint(t, true));
        }

        for (float t = 0.0f; t <= 1.0005; t += 0.01) {
            // calculate the curve point of t and add it to the hash map
            curvePointsForSmallest_t.put((Math.round(t * 100f) / 100f), calcCurvePoint(t, false));
        }
    }

    /**
     * Method to calculate a point on the curve. Optionally the intermediate steps can also be saved.
     *
     * @param t within [0, 1] to calculate point on the bezier curve
     * @param calcHelpers option to save intermediate steps of the calculation
     * @return Vector representing the point on the bezier curve for the given t
     */

    private Vector2f calcCurvePoint(float t, boolean calcHelpers) {
        // aux list for copy of control points
        ArrayList<Vector2f> auxList = new ArrayList<>();

        //last index of controlPoints
        int n = controlPoints.size() - 1;

        // add control points to aux list
        for (int i = 0; i <= n; i++) {
            auxList.add(i, new Vector2f(controlPoints.get(i)));
        }

        List<Vector2f> tList = new ArrayList<>();
        for (int k = 1; k <= n; k++) {
            // calc new point between point i and i + 1 in the aux list
            for (int i = 0; i <= n - k; i++) {
                // newPoint = startPoint + t * (endPoint - startPoint)
                Vector2f v = auxList.get(i).add((auxList.get(i + 1).subtract(auxList.get(i))).mult(t));
                auxList.set(i, v);
                if (calcHelpers && t > 0 && t < 1) {
                    Vector2f v2 = new Vector2f(v);
                    if (!tList.contains(v2)) tList.add(new Vector2f(v2));
                }
            }
            if (calcHelpers && t > 0 && t < 1) intermediateSteps.put(t, tList);
        }
        // return calculated point
        return auxList.get(0);
    }

    // ++++++++++++++++++++++ GETTERS / SETTERS ++++++++++++++++++++++

    public Map<Float, Vector2f> getCurvePointsForCurrent_t() {
        return curvePointsForCurrent_t;
    }

    public List<Vector2f> getCurvePointList() {
        return new ArrayList<>(curvePointsForCurrent_t.values());
    }

    public HashMap<Float, List<Vector2f>> getIntermediateSteps() {
        return intermediateSteps;
    }

    public void setIncrement(double newIncrement) {
        increment = newIncrement;
    }

    public HashMap<Float, Vector2f> getCurvePointsForSmallest_t() {
        return curvePointsForSmallest_t;
    }
}
