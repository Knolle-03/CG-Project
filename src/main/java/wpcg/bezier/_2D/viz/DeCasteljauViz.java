package wpcg.bezier._2D.viz;

import com.jme3.math.Vector2f;
import lombok.Data;
import lombok.EqualsAndHashCode;
import wpcg.CG2D;
import wpcg.base.canvas2d.Canvas2D;
import wpcg.bezier._2D.algorithm.DeCasteljau;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;
public class DeCasteljauViz extends Canvas2D implements MouseListener, ChangeListener {
    private static final int BOUNDS = 300;
    private HashMap<Float, List<Vector2f>> intermediateSteps;
    private final ArrayList<Vector2f> controlPoints = new ArrayList<>();
    private DeCasteljau casteljauMath;
    private double increment = .01;
    private CG2D container;
    private Graphics2D g2D;



    public DeCasteljauViz(CG2D container) {
        super(600, 600, new Vector2f(-BOUNDS, -BOUNDS), new Vector2f(BOUNDS, BOUNDS));
        this.container = container;


        addMouseListener(this);

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
        if (g2D == null) g2D = g;
        g.setBackground(Color.lightGray);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        if (!controlPoints.isEmpty()) {
            g.clearRect(0,0, getWidth(), getHeight());

            // first cp
            drawPoint(g, controlPoints.get(0), Color.RED);
            // draw remaining cps and lines between them
            for (int i = 1; i < controlPoints.size(); i++) {
                drawLine(g, controlPoints.get(i), controlPoints.get(i - 1), Color.BLACK);
                drawPoint(g, controlPoints.get(i), Color.RED);
            }


            System.out.println("cps: " + controlPoints.size());
            System.out.println(Arrays.toString(intermediateSteps.values().toArray()));
            for (List<Vector2f> vList : intermediateSteps.values()) {
                int currentSkippingIndex = controlPoints.size() - 2;
                int offsetToNextSkippingIndex = currentSkippingIndex;
                for (int i = 0; i < vList.size() - 1; i++) {
                    drawPoint(g, vList.get(i), Color.BLUE);
                }

                for (int i = 0; i < vList.size() - 1; i++) {

                    // skip line drawing between "layers"
                    if (i == currentSkippingIndex) {
                        currentSkippingIndex += offsetToNextSkippingIndex;
                        // offset decreases each "layer" by one
                        offsetToNextSkippingIndex -= 1;
                        continue;
                    }

                    drawLine(g, vList.get(i), vList.get(i + 1), Color.DARK_GRAY);
                    System.out.println("Start: " + vList.get(i));
                    System.out.println("End: " + vList.get(i + 1));
                }
                drawPoint(g, vList.get(vList.size() - 1), Color.cyan);
            }











//            for (Map.Entry<Float, List<Vector2f>> entry : intermediateSteps.entrySet()) {
//                for (Vector2f val : entry.getValue()) {
//                    //drawPoint(g, val, Color.BLUE);
//                }
//            }
//            System.out.println( intermediateSteps.get(0.01f));
//
//            List<Vector2f> vecList = intermediateSteps.get(0.01f);
//            for (Vector2f vec : vecList) {
//                drawPoint(g, vec, Color.BLACK);
//            }
//
//            for (Vector2f point : casteljauMath.getCurvePoints().values()) {
//                drawPoint(g, point, Color.YELLOW);
//            }
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
            casteljauMath.reCalcCurvePoints();
            intermediateSteps = casteljauMath.getIntermediateSteps();
            repaint();
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private Vector2f translatePixelCoordsToVector(MouseEvent e) {
        return unit2World(pixel2Unit(new Vector2f(e.getX(), e.getY())));
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == container.getIncrementSlider()) {

            float t = Math.round(container.getIncrementSlider().getValue() / 10.0);
            t /= 100;
            System.out.println(t);
            casteljauMath.setIncrement(t);
            casteljauMath.reCalcCurvePoints();
            intermediateSteps = casteljauMath.getIntermediateSteps();
            System.out.println("lol");
            container.getIncrementLabel().setText("t: " + t);
            repaint();
        }
    }

    public void setIncrement(double increment) {
        this.increment = increment;
    }
}
