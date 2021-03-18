package wpcg.beziercurves;

import wpcg.beziercurves.viz.DeCasteljauViz;
import javax.swing.*;
import java.awt.*;

/**
 * Main window containing canvas and options menu
 *
 * @author Lennart Draeger
 */

public class MainWindow extends JFrame {

    // window dimensions
    int WIDTH = 1600;
    int HEIGHT = 1000;

    // options menu colors
    Color OPTIONS_MENU_COLOR = Color.DARK_GRAY;
    Color OPTIONS_MENU_TEXT_COLOR = Color.CYAN;

    // container for options
    JPanel optionsMenu;
    // slider for t
    JSlider incrementSlider;
    // label for t
    JLabel incrementLabel;
    // control point options
    JCheckBox showControlPointLinesCheckBox;
    JCheckBox showControlPointsCheckBox;
    // helper options
    JCheckBox showHelperLinesCheckBox;
    JCheckBox showHelperPointsCheckBox;
    // curve options
    JCheckBox showCurveLinesCheckBox;
    JCheckBox showCurvePointsCheckBox;
    // check box to display only one curve point
    JCheckBox showCurrentCurvePointCheckBox;
    // convex hull
    JCheckBox showConvexHullControlPointsCheckBox;
    // reset option
    JButton resetButton;
    // canvas the bezier curve is drawn on
    DeCasteljauViz canvas;

    public MainWindow() {
        setupCG2D();
        setupDeCasteljauViz();
        setupOptionsMenu();
        setupOptionsMenuItems();
        this.add(optionsMenu, BorderLayout.EAST);
        this.add(canvas, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(MainWindow::new);
    }

    // ++++++++++++++++++++++ window setup methods ++++++++++++++++++++++

    private void setupOptionsMenuItems() {
        // setup increment slider
        incrementSlider = new JSlider(5, 990);
        optionsMenu.add(incrementSlider);
        incrementSlider.addChangeListener(canvas);
        incrementSlider.setMajorTickSpacing(10);
        incrementSlider.setBounds(0,0, WIDTH, HEIGHT);
        incrementSlider.setBackground(OPTIONS_MENU_COLOR);
        incrementSlider.setSnapToTicks(true);
        incrementSlider.setPreferredSize(new Dimension(200, 50));

        // setup increment label
        incrementLabel = new JLabel();
        incrementLabel.setForeground(OPTIONS_MENU_TEXT_COLOR);
        optionsMenu.add(incrementLabel);
        incrementLabel.setText("t: " + incrementSlider.getValue() / 100.0);

        // setup control point lines check box
        showControlPointLinesCheckBox = new JCheckBox("Show ctrl point lines", true);
        configureCheckBox(showControlPointLinesCheckBox);

        // setup control points check box
        showControlPointsCheckBox = new JCheckBox("Show ctrl points", true);
        configureCheckBox(showControlPointsCheckBox);

        // setup helper lines check box
        showHelperLinesCheckBox = new JCheckBox("Show helper lines", true);
        configureCheckBox(showHelperLinesCheckBox);

        // setup helper point check box
        showHelperPointsCheckBox = new JCheckBox("Show helper points", true);
        configureCheckBox(showHelperPointsCheckBox);

        // setup curve lines check box
        showCurveLinesCheckBox = new JCheckBox("Show curve lines", false);
        configureCheckBox(showCurveLinesCheckBox);

        // setup curve points check box
        showCurvePointsCheckBox = new JCheckBox("Show curve points", false);
        configureCheckBox(showCurvePointsCheckBox);

        // setup curve point for current t check box
        showCurrentCurvePointCheckBox = new JCheckBox("Show curve point for current t", true);
        configureCheckBox(showCurrentCurvePointCheckBox);

        // setup convex hull for control points check box
        showConvexHullControlPointsCheckBox = new JCheckBox("Show convex hull for control points", false);
        configureCheckBox(showConvexHullControlPointsCheckBox);

        // setup reset button
        resetButton = new JButton("reset canvas");
        resetButton.setFocusable(false);
        resetButton.addActionListener(canvas);
        optionsMenu.add(resetButton);
    }

    // ++++++++++++++++++++++ helper methods ++++++++++++++++++++++00

    // setup options menu
    private void setupOptionsMenu() {
        optionsMenu = new JPanel();
        optionsMenu.setBackground(OPTIONS_MENU_COLOR);
        optionsMenu.setPreferredSize(new Dimension(300, HEIGHT));
        optionsMenu.setLayout(new FlowLayout());
    }

    // setup canvas
    private void setupDeCasteljauViz() {
        canvas = new DeCasteljauViz(this);
        canvas.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    // setup CG2D
    private void setupCG2D() {
        this.setVisible(true);
        this.setLayout(new BorderLayout(5, 5));
        this.setSize(WIDTH, HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // generic check box setup method
    private void configureCheckBox(JCheckBox checkBox) {
        checkBox.addChangeListener(canvas);
        checkBox.setForeground(OPTIONS_MENU_TEXT_COLOR);
        checkBox.setBackground(OPTIONS_MENU_COLOR);
        optionsMenu.add(checkBox);
    }

    // ++++++++++++++++++++++ Getters ++++++++++++++++++++++

    public JSlider getIncrementSlider() {
        return incrementSlider;
    }

    public JLabel getIncrementLabel() {
        return incrementLabel;
    }

    public JButton getResetButton() {
        return resetButton;
    }

    public JCheckBox getShowControlPointLinesCheckBox() {
        return showControlPointLinesCheckBox;
    }

    public JCheckBox getShowControlPointsCheckBox() {
        return showControlPointsCheckBox;
    }

    public JCheckBox getShowHelperLinesCheckBox() {
        return showHelperLinesCheckBox;
    }

    public JCheckBox getShowHelperPointsCheckBox() {
        return showHelperPointsCheckBox;
    }

    public JCheckBox getShowCurveLinesCheckBox() {
        return showCurveLinesCheckBox;
    }

    public JCheckBox getShowCurvePointsCheckBox() {
        return showCurvePointsCheckBox;
    }

    public JCheckBox getShowConvexHullControlPointsCheckBox() {
        return showConvexHullControlPointsCheckBox;
    }

    public JCheckBox getShowCurrentCurvePointCheckBox() { return showCurrentCurvePointCheckBox; }

}
