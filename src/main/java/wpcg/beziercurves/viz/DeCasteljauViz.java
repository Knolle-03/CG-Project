package wpcg.beziercurves.viz;

import com.jme3.math.Vector2f;
import wpcg.beziercurves.MainWindow;
import wpcg.base.canvas2d.Canvas2D;
import wpcg.beziercurves.algorithms.DeCasteljau;
import wpcg.beziercurves.algorithms.QuickHull;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * This class visualizes the construction of the bezier curve calculated by the DeCasteljau class.
 *
 * @author Lennart Draeger
 */

public class DeCasteljauViz extends Canvas2D implements MouseListener, ChangeListener, MouseMotionListener, ActionListener {

    // Window containing this Canvas and the options menu
    private final MainWindow container;

    // Bounds of the canvas the curve is displayed on
    private static final int BOUNDS = 300;

    // currently represented bezier curve
    private final DeCasteljau casteljauMath;
    private final ArrayList<Vector2f> controlPoints = new ArrayList<>();

    // toggles to display different lines and point on the canvas
    private boolean showControlPointLines = true;
    private boolean showControlPoints = true;
    private boolean showHelperLines = true;
    private boolean showHelperPoints = true;
    private boolean showCurveLines = false;
    private boolean showCurvePoints = false;
    private boolean showCurrentCurvePoint = true;
    private boolean showConvexHullControlPoints = false;

    private Vector2f selectedControlPoint;

    // colors to draw helper lines in alternating colors for each "layer"
    private Color currentColor;
    private final List<Color> colors = new ArrayList<>();

    public DeCasteljauViz(MainWindow container) {
        super(1920, 1080, new Vector2f(-BOUNDS, -BOUNDS), new Vector2f(BOUNDS, BOUNDS));
        this.container = container;
        colors.add(new Color(255, 0, 0));
        colors.add(new Color(0, 0, 255));
        //colors.add(new Color(0, 255,0 ));
        currentColor = colors.get(0);
        addMouseListener(this);
        addMouseMotionListener(this);

        controlPoints.add(new Vector2f(-250,0));
        controlPoints.add(new Vector2f(-125,200));
        controlPoints.add(new Vector2f(125,200));
        controlPoints.add(new Vector2f(250,0));

        // calculate points on bezier curve with given increment
        casteljauMath = new DeCasteljau(controlPoints, .01);
        casteljauMath.calcCurvePoints();
    }

    @Override
    public void onRepaint(Graphics2D g) {
        g.setBackground(Color.lightGray);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setStroke(new BasicStroke(2));

        // paint points and lines if toggle is set to true
        if (!controlPoints.isEmpty()) {
            g.clearRect(0,0, getWidth(), getHeight());
            if (showControlPoints) paintControlPoints(g);
            if (showConvexHullControlPoints) paintConvexHull(g);
            if (showControlPointLines) paintControlPointLines(g);
            paintHelpers(g);
            if (showCurvePoints) paintCurvePoints(g);
            if (showCurrentCurvePoint) paintCurrentCurvePoint(g);
            if (showCurveLines) paintCurveLines(g);
        }
    }

    // ++++++++++++++++++++++ paint methods used in the onRepaint method ++++++++++++++++++++++

    private void paintCurveLines(Graphics2D g) {
        List<Vector2f> curvePoints = casteljauMath.getCurvePointList();

        for (int i = 0; i < curvePoints.size() - 1; i++) {
            drawLine(g, curvePoints.get(i), curvePoints.get(i + 1), Color.YELLOW);
        }
    }

    private void paintCurrentCurvePoint(Graphics2D g) {
        // draw curve point for current value of t
        drawPoint(g, casteljauMath.getCurvePointList().get(1), Color.CYAN);

        Iterator<Map.Entry<Float, Vector2f>> iterator = casteljauMath.getCurvePointsForSmallest_t().entrySet().iterator();

        Map.Entry<Float, Vector2f> prev = iterator.next();
        Map.Entry<Float, Vector2f> current = iterator.next();

        float current_T = current.getKey();
        while (current_T <= getCurrent_t() && iterator.hasNext()) {
            drawLine(g, prev.getValue(), current.getValue(), Color.YELLOW);
            prev = current;
            current = iterator.next();
            current_T = current.getKey();
        }
    }

    private void paintCurvePoints(Graphics2D g) {
        for (Vector2f curvePoint : casteljauMath.getCurvePointsForCurrent_t().values()) {
            drawPoint(g, curvePoint, Color.CYAN);
        }
    }

    private void paintHelpers(Graphics2D g) {
        for (List<Vector2f> vList : casteljauMath.getIntermediateSteps().values()) {
            int currentSkippingIndex = controlPoints.size() - 2;
            int offsetToNextSkippingIndex = currentSkippingIndex;

            if (showHelperPoints) paintHelperPoints(g, vList);

            if (showHelperLines) {
                // draw helper lines
                for (int i = 0; i < vList.size() - 1; i++) {
                    // skip line drawing between "layers"
                    if (i == currentSkippingIndex) {
                        currentColor = getOtherColor();
                        currentSkippingIndex += offsetToNextSkippingIndex;
                        // offset decreases each "layer" by one
                        offsetToNextSkippingIndex -= 1;
                        continue;
                    }
                    drawLine(g, vList.get(i), vList.get(i + 1), currentColor);
                }
            }
            // stop after first iteration
            if (showCurrentCurvePoint) break;
        }
    }

    private void paintControlPointLines(Graphics2D g) {
        for (int i = 1; i < controlPoints.size(); i++) {
            drawLine(g, controlPoints.get(i), controlPoints.get(i - 1), Color.BLACK);
        }
    }

    private void paintHelperPoints(Graphics2D g, List<Vector2f> vList) {
        for (int i = 0; i < vList.size() - 1; i++) {
            drawPoint(g, vList.get(i), Color.BLUE);
        }
    }

    private void paintConvexHull(Graphics2D g) {
        List<Vector2f> convexHull = QuickHull.calcConvexHull(controlPoints);

        if (convexHull.size() > 2) {
            for (int i = 0; i < convexHull.size() - 1; i++) {
                drawLine(g, convexHull.get(i), convexHull.get(i + 1), Color.ORANGE);
            }
            drawLine(g, convexHull.get(convexHull.size() - 1), convexHull.get(0), Color.ORANGE);
        }
    }

    private void paintControlPoints(Graphics2D g) {
        // first cp
        drawControlPoint(g, controlPoints.get(0), Color.BLACK);
        // draw remaining cps and lines between them
        for (int i = 1; i < controlPoints.size(); i++) {
            drawControlPoint(g, controlPoints.get(i), Color.BLACK);
        }
    }

    // method to add and remove control points
    @Override
    public void mouseClicked(MouseEvent e) {
        // add control point to canvas
        if (e.getButton() == 1) {
            controlPoints.add(translatePixelCoordsToVector(e));
            reCalcDeCasteljau();
            repaint();
        }
        // remove control point if there is one within the delta
        else if (e.getButton() == 3) {
            Vector2f cp = controlPointWithinDelta(e);
            if (cp != null) {
                controlPoints.remove(cp);
                reCalcDeCasteljau();
                repaint();
            } else {
                System.out.println("No cp within delta");
            }
        }
    }

    // method to "pick up" control point
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 1) {
            Vector2f cp = controlPointWithinDelta(e);
            if (cp != null) {
                selectedControlPoint = cp;
            }
        }
    }

    // method to "release" control point
    @Override
    public void mouseReleased(MouseEvent e) {
        selectedControlPoint = null;
    }

    // method move control point
    @Override
    public void mouseDragged(MouseEvent e) {
        Vector2f currentVec = translatePixelCoordsToVector(e);
        if (selectedControlPoint != null) {
            selectedControlPoint.x = currentVec.getX();
            selectedControlPoint.y = currentVec.getY();
            reCalcDeCasteljau();
            repaint();
        }
    }

    // method to confirm reset of canvas
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == container.getResetButton()) {
            int response = JOptionPane.showConfirmDialog(container, "Delete current curve?", "Reset", JOptionPane.YES_NO_OPTION);
            if (response == 0) {
                controlPoints.clear();
                reCalcDeCasteljau();
                repaint();
            }
        }
    }

    // method to repaint canvas if options have changed
    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == container.getIncrementSlider()) {
            casteljauMath.setIncrement(getCurrent_t());
            casteljauMath.calcCurvePoints();
            container.getIncrementLabel().setText("t: " + getCurrent_t());
            repaint();
        } else if (e.getSource() == container.getShowHelperLinesCheckBox()) {
            setShowHelperLines(container.getShowHelperLinesCheckBox().isSelected());
            repaint();
        } else if (e.getSource() == container.getShowHelperPointsCheckBox()) {
            setShowHelperPoints(container.getShowHelperPointsCheckBox().isSelected());
            repaint();
        } else if (e.getSource() == container.getShowCurveLinesCheckBox()) {
            setShowCurveLines(container.getShowCurveLinesCheckBox().isSelected());
            repaint();
        } else if (e.getSource() == container.getShowCurvePointsCheckBox()) {
            setShowCurvePoints(container.getShowCurvePointsCheckBox().isSelected());
            repaint();
        } else if (e.getSource() == container.getShowControlPointLinesCheckBox()) {
            setShowControlPointLines(container.getShowControlPointLinesCheckBox().isSelected());
            repaint();
        } else if (e.getSource() == container.getShowControlPointsCheckBox()) {
            setShowControlPoints(container.getShowControlPointsCheckBox().isSelected());
            repaint();
        } else if (e.getSource() == container.getShowConvexHullControlPointsCheckBox()) {
            setShowConvexHullControlPoints(container.getShowConvexHullControlPointsCheckBox().isSelected());
            repaint();
        } else if (e.getSource() == container.getShowCurrentCurvePointCheckBox()) {
            setShowCurrentCurvePoint(container.getShowCurrentCurvePointCheckBox().isSelected());
            repaint();
        }
    }

    // ++++++++++++++++++++++ helper methods ++++++++++++++++++++++

    private Color getOtherColor() {
        if (currentColor.equals(colors.get(0))) return colors.get(1);
        return colors.get(0);
    }

    private Vector2f translatePixelCoordsToVector(MouseEvent e) {
        return unit2World(pixel2Unit(new Vector2f(e.getX(), e.getY())));
    }

    private Vector2f controlPointWithinDelta(MouseEvent e) {
        Vector2f coords = translatePixelCoordsToVector(e);
        for (Vector2f cp : controlPoints) {
            // if cp lies within a delta of the coords vector
            if ((cp.x < coords.x + getPointSize() && cp.x > coords.x - getPointSize()) &&
                    (cp.y < coords.y + getPointSize() && cp.y > coords.y - getPointSize())) {
                return cp;
            }
        }
        return null;
    }

    private void reCalcDeCasteljau() {
        casteljauMath.calcCurvePoints();
    }

    // ++++++++++++++++++++++ Getters / Setters ++++++++++++++++++++++

    public float getCurrent_t() {
        return Math.round(container.getIncrementSlider().getValue() / 10.0) / 100f;
    }

    public void setShowHelperLines(boolean showHelperLines) {
        this.showHelperLines = showHelperLines;
    }

    public void setShowHelperPoints(boolean showHelperPoints) {
        this.showHelperPoints = showHelperPoints;
    }

    public void setShowCurveLines(boolean showCurveLines) {
        this.showCurveLines = showCurveLines;
    }

    public void setShowCurvePoints(boolean showCurvePoints) {
        this.showCurvePoints = showCurvePoints;
    }

    public void setShowControlPointLines(boolean showControlPointLines) {
        this.showControlPointLines = showControlPointLines;
    }

    public void setShowControlPoints(boolean showControlPoints) {
        this.showControlPoints = showControlPoints;
    }

    public void setShowConvexHullControlPoints(boolean showConvexHullControlPoints) {
        this.showConvexHullControlPoints = showConvexHullControlPoints;
    }

    public void setShowCurrentCurvePoint(boolean showCurrentCurvePoint) {
        this.showCurrentCurvePoint = showCurrentCurvePoint;
    }

    // ++++++++++++++++++++++ unused methods from implemented interfaces ++++++++++++++++++++++

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    protected void onMouseDragged(int x, int y) {}

    @Override
    protected void onMouseMoved(int x, int y) {}

    @Override
    protected void onMouseClicked(int x, int y) {}
}