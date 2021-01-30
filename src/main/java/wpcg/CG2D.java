/**
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 */

package wpcg;

import wpcg.bezier._2D.viz.DeCasteljauViz;

import javax.swing.*;
import java.awt.*;

/**
 * Lecture support application for 2D scenes
 */
public class CG2D extends JFrame {

    // window dimensions
    int WIDTH = 1000;
    int HEIGHT = 600;

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
    // reset option
    JButton resetButton;
    // canvas the bezier curve is drawn on
    DeCasteljauViz canvas;


    public CG2D() {
        setupCG2D();
        setupDeCasteljauViz();
        setupOptionsMenu();
        setupOptionsMenuItems();
        this.add(optionsMenu, BorderLayout.EAST);
        this.add(canvas, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(CG2D::new);
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
        showCurveLinesCheckBox = new JCheckBox("Show curve lines", true);
        configureCheckBox(showCurveLinesCheckBox);

        // setup curve points check box
        showCurvePointsCheckBox = new JCheckBox("Show curve points", true);
        configureCheckBox(showCurvePointsCheckBox);

        // setup reset button
        resetButton = new JButton("reset canvas");
        resetButton.setFocusable(false);
        resetButton.addActionListener(canvas);
        optionsMenu.add(resetButton);
    }

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

    // ++++++++++++++++++++++ GETTERS ++++++++++++++++++++++

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
}
