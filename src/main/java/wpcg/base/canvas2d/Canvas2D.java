/**
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 */

package wpcg.base.canvas2d;

import com.jme3.math.Vector2f;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public abstract class Canvas2D extends JPanel {

  private static final int POINT_SIZE = 10;

  /**
   * Lower left corner in world coordinates.
   */
  private Vector2f ll;

  /**
   * Upper right corner in world coordinates.
   */
  private Vector2f ur;

  public Graphics2D g2D;


  public Canvas2D(int width, int height, Vector2f ll, Vector2f ur) {
    setSize(width, height);
    this.ll = new Vector2f(ll);
    this.ur = new Vector2f(ur);
    g2D = (Graphics2D) getGraphics();
  }

  public void setupListener() {
    addMouseMotionListener(new MouseMotionListener() {
      @Override
      public void mouseDragged(MouseEvent e) {
        onMouseDragged(e.getX(), e.getY());
      }

      @Override
      public void mouseMoved(MouseEvent e) {
        onMouseMoved(e.getX(), e.getY());
      }
    });

    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        onMouseClicked(e.getX(), e.getY());
      }
    });
  }

  public void paint(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    onRepaint(g2);
  }

  /**
   * Draw the scene content here.
   */
  public abstract void onRepaint(Graphics2D g);

  /**
   * Handle mouse drag events here
   */
  protected abstract void onMouseDragged(int x, int y);

  /**
   * Handle mouse move events here
   */
  protected abstract void onMouseMoved(int x, int y);

  /**
   * Handle mouse click events here.
   */
  protected abstract void onMouseClicked(int x, int y);

  /**
   * Draw a line from a -> b.
   */
  protected void drawLine(Graphics gc, Vector2f a, Vector2f b, Color color) {
    Vector2f a_ = world2Pixel(a);
    Vector2f b_ = world2Pixel(b);
    gc.setColor(color);
    gc.drawLine((int) a_.x, (int) a_.y, (int) b_.x, (int) b_.y);
  }

  /**
   * Draw a point at p.
   */
  protected void drawPoint(Graphics gc, Vector2f p, Color color) {
    Vector2f pixelPoint = world2Pixel(p);
    int x = (int) pixelPoint.x - POINT_SIZE / 2;
    int y = (int) pixelPoint.y - POINT_SIZE / 2;
    gc.setColor(color);
    gc.fillArc(x, y, POINT_SIZE, POINT_SIZE, 0, 360);
    gc.setColor(Color.BLACK);
    gc.drawArc(x, y, POINT_SIZE, POINT_SIZE, 0, 360);
  }

  protected void drawControlPoint(Graphics gc, Vector2f p, Color color) {
    Vector2f pixelPoint = world2Pixel(p);
    int x = (int) pixelPoint.x - POINT_SIZE / 2;
    int y = (int) pixelPoint.y - POINT_SIZE / 2;
    int x2 = (int) pixelPoint.x;
    int y2 = (int) pixelPoint.y;
    gc.setColor(color);

    gc.drawLine(x - POINT_SIZE , y2 , x , y2); //left
    gc.drawLine(x2 , y - POINT_SIZE  , x2 , y); // top
    gc.drawLine(x + 2 * POINT_SIZE, y2, x + POINT_SIZE, y2); // right
    gc.drawLine(x2 , y + 2 * POINT_SIZE, x2 , y + POINT_SIZE);
    //gc.setColor(Color.BLACK);
    gc.drawArc(x - POINT_SIZE / 2, y2 - POINT_SIZE, POINT_SIZE * 2, POINT_SIZE * 2, 0, 360);
  }

  /**
   * Draw a point at p.
   */
  protected void drawControlPoint(Graphics gc, Vector2f p, Color color) {
    Vector2f pixelPoint = world2Pixel(p);
    int offset = POINT_SIZE / 2;

    int x = (int) pixelPoint.x;
    int y = (int) pixelPoint.y;
    //left
    gc.drawLine(x - offset  * 4 , y , x - offset, y );
    //right
    gc.drawLine(x + offset  * 4 , y , x + offset, y );
    // bottom
    gc.drawLine(x, y + offset * 4 , x, y + offset);
    //top
    gc.drawLine(x, y - offset * 4 , x, y - offset);

    gc.drawArc(x - POINT_SIZE, y - POINT_SIZE, POINT_SIZE * 2, POINT_SIZE * 2, 0, 360);



    //gc.setColor(color);
    //gc.fillArc(x, y, POINT_SIZE, POINT_SIZE, 0, 360);
    //gc.setColor(Color.BLACK);
  }

  /**
   * World -> pixel coordinates.
   */
  protected Vector2f world2Pixel(Vector2f pWorld) {
    return unit2Pixel(world2Unit(pWorld));
  }

  /**
   * Pixel coordinates -> world coordinates.
   */
  protected Vector2f pixel2World(Vector2f pPixel) {
    return unit2World(pixel2Unit(pPixel));
  }

  /**
   * World coordinates -> unit coordinates.
   */
  protected Vector2f world2Unit(Vector2f pWorld) {
    Vector2f diagonal = ur.subtract(ll);
    float scale = Math.max(diagonal.x, diagonal.y);
    Vector2f pUnit = pWorld.subtract(ll).mult(1.0f / scale);
    return pUnit;
  }

  /**
   * Unit coordinates -> world coordinates.
   */
  protected Vector2f unit2World(Vector2f pUnit) {
    Vector2f diagonal = ur.subtract(ll);
    float scale = Math.max(diagonal.x, diagonal.y);
    return pUnit.mult(scale).add(ll);
  }

  /**
   * Unit coordinates -> pixel coordinates
   */
  protected Vector2f unit2Pixel(Vector2f pUnit) {
    float scale = Math.min(getWidth(), getHeight());
    Vector2f offset = new Vector2f((getWidth() - scale) / 2.0f,
            (getHeight() - scale) / 2.0f);
    Vector2f pPixel = pUnit.mult(scale).add(offset);
    pPixel.y = getHeight() - pPixel.y;
    return pPixel;
  }

  /**
   * Pixel coorinates -> unit coordinates.
   */
  protected Vector2f pixel2Unit(Vector2f pPixel) {
    float scale = Math.min(getWidth(), getHeight());
    Vector2f offset = new Vector2f((getWidth() - scale) / 2.0f,
            (getHeight() - scale) / 2.0f);
    Vector2f pUnit = new Vector2f(pPixel.x, getHeight() - pPixel.y);
    pUnit = pUnit.subtract(offset).mult(1.0f / scale);
    return pUnit;
  }


  public static int getPointSize() {
    return POINT_SIZE;
  }
}
