package wpcg.bezier._2D.viz;

import com.jme3.math.Vector2f;
import wpcg.CG2D;
import wpcg.base.canvas2d.Canvas2D;
import wpcg.bezier._2D.algorithm.DeCasteljau;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;


public class DeCasteljauViz extends Canvas2D implements MouseListener, ChangeListener, MouseMotionListener, ActionListener {
    private static final int BOUNDS = 300;
    private HashMap<Float, List<Vector2f>> intermediateSteps;
    private final ArrayList<Vector2f> controlPoints = new ArrayList<>();
    private final DeCasteljau casteljauMath;
    private double increment = .01;
    private final CG2D container;
    private final List<Color> colors = new ArrayList<>();
    private Color currentColor;

    private boolean showControlPointLines = true;
    private boolean showControlPoints = true;
    private boolean showHelperLines = true;
    private boolean showHelperPoints = true;
    private boolean showCurveLines = true;
    private boolean showCurvePoints = true;

    private Vector2f selectedControlPoint;


    public DeCasteljauViz(CG2D container) {
        super(600, 600, new Vector2f(-BOUNDS, -BOUNDS), new Vector2f(BOUNDS, BOUNDS));
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
        casteljauMath = new DeCasteljau(controlPoints, increment);
        casteljauMath.calcCurvePoints();
        intermediateSteps = casteljauMath.getIntermediateSteps();


    }

    @Override
    public void onRepaint(Graphics2D g) {
        // TODO:: Set once when initialized?
        g.setBackground(Color.lightGray);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setStroke(new BasicStroke(2));



        if (!controlPoints.isEmpty()) {
            g.clearRect(0,0, getWidth(), getHeight());


            if (showControlPoints) {
                // first cp
                drawPoint(g, controlPoints.get(0), Color.RED);
                // draw remaining cps and lines between them
                for (int i = 1; i < controlPoints.size(); i++) {
                    drawPoint(g, controlPoints.get(i), Color.RED);
                }
            }

            if (showControlPointLines) {
                for (int i = 1; i < controlPoints.size(); i++) {
                    drawLine(g, controlPoints.get(i), controlPoints.get(i - 1), Color.BLACK);
                }
            }



                for (List<Vector2f> vList : intermediateSteps.values()) {
                    int currentSkippingIndex = controlPoints.size() - 2;
                    int offsetToNextSkippingIndex = currentSkippingIndex;

                    if (showHelperPoints) {
                        // draw helper points
                        for (int i = 0; i < vList.size() - 1; i++) {
                            drawPoint(g, vList.get(i), Color.BLUE);
                        }
                    }

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

            }
                if (showCurvePoints) {
                    // draw curve points
                    for (Vector2f curvePoint : casteljauMath.getCurvePoints().values()) {
                        drawPoint(g, curvePoint, Color.CYAN);
                    }

                }

                if (showCurveLines) {
                    List<Vector2f> curvePoints = casteljauMath.getCurvePointList();

                    for (int i = 0; i < curvePoints.size() - 1; i++) {
                        drawLine(g, curvePoints.get(i), curvePoints.get(i + 1), Color.YELLOW);
                    }
                }



        }
    }

    @Override
    protected void onMouseDragged(int x, int y) {

    }

    @Override
    protected void onMouseMoved(int x, int y) {

    }

    @Override
    protected void onMouseClicked(int x, int y) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == 1) {
            controlPoints.add(translatePixelCoordsToVector(e));
            reCalcDeCasteljau();
            repaint();
        } else if (e.getButton() == 3) {
            Vector2f cp = controlPointWithinDelta(e);
            if (cp != null) {
                controlPoints.remove(cp);
                System.out.println("Removed cp:" + cp);
                reCalcDeCasteljau();
                repaint();
            } else {
                System.out.println("No cp within delta");
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 1) {
            System.out.println("in Mouse Pressed");
            Vector2f cp = controlPointWithinDelta(e);
            if (cp != null) {
                selectedControlPoint = cp;
            }
        }


    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("Released");
        selectedControlPoint = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

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

    @Override
    public void mouseMoved(MouseEvent e) {

    }

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



    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == container.getIncrementSlider()) {

            float t = Math.round(container.getIncrementSlider().getValue() / 10.0) / 100f;
            casteljauMath.setIncrement(t);
            casteljauMath.reCalcCurvePoints();
            intermediateSteps = casteljauMath.getIntermediateSteps();
            container.getIncrementLabel().setText("t: " + t);
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
        }
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
        casteljauMath.reCalcCurvePoints();
        intermediateSteps = casteljauMath.getIntermediateSteps();
    }



}
