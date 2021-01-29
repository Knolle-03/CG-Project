package wpcg;

import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import wpcg.base.Scene;
import wpcg.bezier._3D.IntroScene;
import wpcg.ui.ComputergraphicsJMEApp;
import wpcg.ui.ComputergraphicsFrame;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This launcher is used to start the 3D application with a Java Swing user interface.
 */
public class CG3D {

  /**
   * Never call this method but in the main().
   */
  private void start(Scene scene) {
    java.awt.EventQueue.invokeLater(() -> {
      AppSettings settings = new AppSettings(true);
      settings.setTitle("WP Computergraphics (3D)");
      settings.setUseInput(false);
      settings.setWidth(640);
      settings.setHeight(480);

      ComputergraphicsJMEApp app = new ComputergraphicsJMEApp(scene);
      app.setSettings(settings);
      app.setShowSettings(false);
      app.createCanvas();
      app.setPauseOnLostFocus(false);
      JmeCanvasContext ctx = (JmeCanvasContext) app.getContext();
      ctx.setSystemListener(app);

      new ComputergraphicsFrame(ctx.getCanvas(), app, scene);
      app.startCanvas();
    });
  }

  public static void main(String[] args) {
    // Change scene object here
    Scene scene = new IntroScene();

    // no change here
    Logger.getLogger("com.jme3").setLevel(Level.SEVERE);
    new CG3D().start(scene);
  }
}
