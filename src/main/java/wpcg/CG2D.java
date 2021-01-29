/**
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 */

package wpcg;

import lombok.Data;
import lombok.EqualsAndHashCode;
import wpcg.bezier._2D.viz.DeCasteljauViz;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Lecture support application for 2D scenes
 */
public class CG2D extends JFrame implements ChangeListener, PropertyChangeListener {
    int WIDTH = 1000;
    int HEIGHT = 600;

    Color OPTIONS_MENU_COLOR = Color.DARK_GRAY;
    Color OPTIONS_MENU_TEXT_COLOR = Color.CYAN;

    JPanel optionsMenu;
    JSlider incrementSlider;
    JLabel incrementLabel;
    JToggleButton showIncrementsToggleButton;
    JCheckBox showHelperLinesBox;
    JButton resetButton;

    DeCasteljauViz canvas;

    public CG2D() {

        // setup CG2D
        this.setVisible(true);
        this.setLayout(new BorderLayout(5, 5));
        this.setSize(WIDTH, HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // setup canvas
        canvas = new DeCasteljauViz(this);
        canvas.setPreferredSize(new Dimension(WIDTH, HEIGHT));


        // setup options menu
        optionsMenu = new JPanel();
        optionsMenu.setBackground(OPTIONS_MENU_COLOR);
        optionsMenu.setPreferredSize(new Dimension(300, HEIGHT));
        optionsMenu.setLayout(new FlowLayout());

        // setup increment slider
        incrementSlider = new JSlider(10, 900);
        optionsMenu.add(incrementSlider);
        incrementSlider.addChangeListener(canvas);
        incrementSlider.setPaintTicks(true);
        //incrementSlider.setPaintLabels(true);
        incrementSlider.setMajorTickSpacing(10);
        incrementSlider.setBounds(0,0, WIDTH, HEIGHT);
        incrementSlider.setBackground(OPTIONS_MENU_COLOR);
        incrementSlider.setSnapToTicks(true);
        incrementSlider.setPreferredSize(new Dimension(200, 50));

        // setup increment label
        incrementLabel = new JLabel();
        //incrementLabel.addPropertyChangeListener(this);
        incrementLabel.setForeground(OPTIONS_MENU_TEXT_COLOR);
        optionsMenu.add(incrementLabel);
        incrementLabel.setText("t: " + incrementSlider.getValue() / 100.0);

        // setup helper lines checkbox
        showHelperLinesBox = new JCheckBox("Show helper lines", true);
        showHelperLinesBox.addChangeListener(canvas);
        showHelperLinesBox.setForeground(OPTIONS_MENU_TEXT_COLOR);
        showHelperLinesBox.setBackground(OPTIONS_MENU_COLOR);
        optionsMenu.add(showHelperLinesBox);








        this.add(optionsMenu, BorderLayout.EAST);
        this.add(canvas, BorderLayout.CENTER);





    }

    public static void main(String[] args) {
        EventQueue.invokeLater(CG2D::new);
    }

    @Override
    public void stateChanged(ChangeEvent e) {



    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    public JPanel getOptionsMenu() {
        return optionsMenu;
    }

    public JSlider getIncrementSlider() {
        return incrementSlider;
    }

    public JLabel getIncrementLabel() {
        return incrementLabel;
    }

    public JToggleButton getShowIncrementsToggleButton() {
        return showIncrementsToggleButton;
    }

    public JButton getResetButton() {
        return resetButton;
    }

    public DeCasteljauViz getCanvas() {
        return canvas;
    }

    public JCheckBox getShowHelperLinesBox() {
        return showHelperLinesBox;
    }
}
