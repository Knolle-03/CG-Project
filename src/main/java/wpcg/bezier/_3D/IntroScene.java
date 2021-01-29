/**
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 */

package wpcg.bezier._3D;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.util.TangentBinormalGenerator;
import wpcg.base.CameraController;
import wpcg.base.Scene;
import wpcg.base.animatedmesh.AnimatedMesh;
import wpcg.base.animatedmesh.AnimationControllerPath;
import wpcg.base.mesh.ObjReader;
import wpcg.base.mesh.TriangleMesh;
import wpcg.base.mesh.TriangleMeshTools;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This introduction scene contains the content of the first exercise: a knight that walks on a squared plane.
 */
public class IntroScene extends Scene {

    /**
     * A list of all animated entities
     */
    protected List<AnimatedMesh> animatedMeshList;
    /**
     * The asset manager is used to read content (e.g. triangle meshes or texture) from file to jMonkey.
     */
    protected AssetManager assetManager;
    /**
     * This is the root node of the scene graph with all the scene content.
     */
    protected Node rootNode;
    /**
     * This node contains the knight.
     */
    protected Node knightNode;

    /**
     * This node contains the house.
     */
    protected Geometry houseGeometry;

    public IntroScene() {
        assetManager = null;
        rootNode = null;
        knightNode = null;
        animatedMeshList = new ArrayList<>();
    }

    @Override
    public void init(AssetManager assetManager, Node rootNode, CameraController cameraController) {
        this.assetManager = assetManager;
        this.rootNode = rootNode;
        cameraController.setup(new Vector3f(-10, 10, -10),
                new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));

        // Load animated knight
        knightNode = loadCharacter(assetManager, rootNode, "Models/knight.gltf");
        knightNode.setLocalScale(0.01f);
        knightNode.setShadowMode(RenderQueue.ShadowMode.Cast);
        AnimationControllerPath knightAnimationController = new AnimationControllerPath(Arrays.asList(
                new Vector3f(6, 0, -6),
                new Vector3f(6, 0, -3.5f),
                new Vector3f(-3.5f, 0, 6),
                new Vector3f(-6f, 0, 6f),
                new Vector3f(-6f, 0, -6f)), 1.2f);
        animatedMeshList.add(new AnimatedMesh(knightNode, knightAnimationController));

        // Add a square to the scene graph
        createGround();

    }



    @Override
    public void update(float time) {
        // Update position
        for (AnimatedMesh animatedMesh : animatedMeshList) {
            animatedMesh.update(time);
        }
    }

    @Override
    public void render() {
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public JPanel getUI() {
        return null;
    }

    /**
     * Generate a ground mesh.
     */
    private void createGround() {
        float extend = 15;
        Box box = new Box(new Vector3f(-extend / 2, -0.05f, -extend / 2),
                new Vector3f(extend / 2, 0, extend / 2));
        Geometry quadGeometry = new Geometry("Ground", box);
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        //mat.setColor("Diffuse", new ColorRGBA(0.3f, 0.15f, 0.05f, 1));
        //mat.setParam("UseMaterialColors", VarType.Boolean, true);
        mat.setTexture("DiffuseMap", assetManager.loadTexture("Textures/stone.png"));
        TangentBinormalGenerator.generate(quadGeometry.getMesh());
        mat.setTexture("NormalMap", assetManager.loadTexture("Textures/stone_normalmap.png"));
        quadGeometry.setMaterial(mat);
        rootNode.attachChild(quadGeometry);
        quadGeometry.setShadowMode(RenderQueue.ShadowMode.Receive);
    }

    protected Node getKnightNode() {
        return knightNode;
    }
}
