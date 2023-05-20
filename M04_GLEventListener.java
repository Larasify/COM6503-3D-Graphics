import gmaths.*;

import java.nio.*;
import java.util.ArrayList;
import java.util.List;

import javax.xml.crypto.dsig.Transform;

import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;

/* I declare that this code is my own work */
/* Author Eylul Lara Cikis elcikis1@sheffield.ac.uk lara.cikis@gmail.com */
  
public class M04_GLEventListener implements GLEventListener {
  
  private static final boolean DISPLAY_SHADERS = false;
    
  public M04_GLEventListener(Camera camera) {
    this.camera = camera;
    this.camera.setPosition(new Vec3(0f,8f,17f));
  }
  
  // ***************************************************
  /*
   * METHODS DEFINED BY GLEventListener
   */

  /* Initialisation */
  public void init(GLAutoDrawable drawable) {   
    GL3 gl = drawable.getGL().getGL3();
    System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
    gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); 
    gl.glClearDepth(1.0f);
    gl.glEnable(GL.GL_DEPTH_TEST);
    //Blend bit is new to enable transparancy
    gl.glEnable(GL.GL_BLEND);
    gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
    gl.glDepthFunc(GL.GL_LESS);
    gl.glFrontFace(GL.GL_CCW);    // default is 'CCW'
    gl.glEnable(GL.GL_CULL_FACE); // default is 'not enabled'
    gl.glCullFace(GL.GL_BACK);   // default is 'back', assuming CCW
    initialise(gl);
    startTime = getSeconds();
  }
  
  /* Called to indicate the drawing surface has been moved and/or resized  */
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    GL3 gl = drawable.getGL().getGL3();
    gl.glViewport(x, y, width, height);
    float aspect = (float)width/(float)height;
    camera.setPerspectiveMatrix(Mat4Transform.perspective(45, aspect));
  }

  /* Draw */
  public void display(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    render(gl);
  }

  /* Clean up memory, if necessary */
  public void dispose(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    //We make multiple lights as an array of light objects
    light[0].dispose(gl);
    light[1].dispose(gl);
    light[2].dispose(gl);
    light[3].dispose(gl);
    floor.dispose(gl);
    sphere.dispose(gl);
    cube.dispose(gl);
    cube2.dispose(gl);
  }
  
  
  // ***************************************************
  /* INTERACTION
   *
   *
   */

   //ANIMATION FUNCTIONS
  public void lamp1position1() {
    lamp1lowerArmRotate.animation = true;
    lamp1lowerArmRotate.rotationTarget = -15f;
    lamp1lowerArmRotate.rotationAxis = "z";
    lamp1UpperArmRotate.animation = true;
    lamp1UpperArmRotate.rotationTarget = -30f;
    lamp1UpperArmRotate.rotationAxis = "z";
    lamp1HeadRotate.animation = true;
    lamp1HeadRotate.rotationTarget = -4f;
    lamp1HeadRotate.rotationAxis = "z";
  }
   
  public void lamp1position2() {
    lamp1lowerArmRotate.animation = true;
    lamp1lowerArmRotate.rotationTarget = 30f;
    lamp1lowerArmRotate.rotationAxis = "z";
    lamp1UpperArmRotate.animation = true;
    lamp1UpperArmRotate.rotationTarget = -70f;
    lamp1UpperArmRotate.rotationAxis = "z";
    lamp1HeadRotate.animation = true;
    lamp1HeadRotate.rotationTarget = 80f;
    lamp1HeadRotate.rotationAxis = "z";
  }
  public void lamp1position3() {
    lamp1lowerArmRotate.animation = true;
    lamp1lowerArmRotate.rotationTarget = -0f;
    lamp1lowerArmRotate.rotationAxis = "z";
    lamp1UpperArmRotate.animation = true;
    lamp1UpperArmRotate.rotationTarget = -90f;
    lamp1UpperArmRotate.rotationAxis = "z";
    lamp1HeadRotate.animation = true;
    lamp1HeadRotate.rotationTarget = 90f;
    lamp1HeadRotate.rotationAxis = "z";

  }
  public void lamp2position1(){
    lamp2lowerArmRotate.animation = true;
    lamp2lowerArmRotate.rotationTarget = -5f;
    lamp2lowerArmRotate.rotationAxis = "x";
    lamp2UpperArmRotate.animation = true;
    lamp2UpperArmRotate.rotationTarget = 50f;
    lamp2UpperArmRotate.rotationAxis = "z";
    lamp2HeadRotate.animation = true;
    lamp2HeadRotate.rotationTarget =-30f;
    lamp2HeadRotate.rotationAxis = "z";
  }
  public void lamp2position2(){
    
    lamp2lowerArmRotate.animation = true;
    lamp2lowerArmRotate.rotationTarget = 60f;
    lamp2lowerArmRotate.rotationAxis = "x";
    lamp2UpperArmRotate.animation = true;
    lamp2UpperArmRotate.rotationTarget = 30f;
    lamp2UpperArmRotate.rotationAxis = "z";
    lamp2HeadRotate.animation = true;
    lamp2HeadRotate.rotationTarget = 40f;
    lamp2HeadRotate.rotationAxis = "z";
  }
  public void lamp2position3(){
    lamp2lowerArmRotate.animation = true;
    lamp2lowerArmRotate.rotationTarget = -30f;
    lamp2lowerArmRotate.rotationAxis = "x";
    lamp2UpperArmRotate.animation = true;
    lamp2UpperArmRotate.rotationTarget = 0f;
    lamp2UpperArmRotate.rotationAxis = "z";
    lamp2HeadRotate.animation = true;
    lamp2HeadRotate.rotationTarget = -90f;
    lamp2HeadRotate.rotationAxis = "z";
  }
  //TOGGLE BUTTONS FOR LIGHTS
  public void togglespotlight1() {
    light[2].toggleLight();
  }
  public void togglespotlight2() {
    light[3].toggleLight();
  }
  public void toggleworldlight1() {
    light[0].toggleLight();
  }
  public void toggleworldlight2() {
    light[1].toggleLight();
  }

  
  // ***************************************************
  /* THE SCENE
   * Now define all the methods to handle the scene.
   * This will be added to in later examples.
   */

  private Camera camera;
  private Mat4 perspective;
  private Model floor,wall1,wall2,sky2, windowBottom,windowTop, sphere,sphere2,sphere3 ,cube, cube2, cube3, cube4;
  private Light[] light = new Light[4];
  private SGNode tableroot, lamp1root, lamp2root, windowframeroot;
  private NameNode lamp1Head, lamp2Head;


  
  private TransformNode tableMoveTranslate,windowframeMoveTranslate ,lamp1MoveTranslate, lamp1lowerArmRotate, lamp1UpperArmRotate, lamp1HeadRotate, lamp2MoveTranslate, lamp2lowerArmRotate, lamp2UpperArmRotate, lamp2HeadRotate, eggRotate, eggTranslate;
  
  private void initialise(GL3 gl) {
    createRandomNumbers();
    int[] textureId0 = TextureLibrary.loadTexture(gl, "textures/floor.jpg");
    int[] textureId1 = TextureLibrary.loadTexture(gl, "textures/tiger.jpg");
    int[] textureId2 = TextureLibrary.loadTexture(gl, "textures/zebra.jpg");
    int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/table.jpg");
    int[] textureId4 = TextureLibrary.loadTexture(gl, "textures/rocks.jpg");
    int[] textureId5 = TextureLibrary.loadTexture(gl, "textures/metal.jpg");
    int[] textureId6 = TextureLibrary.loadTexture(gl, "textures/metal_spec.jpg");
    int[] textureId7 = TextureLibrary.loadTexture(gl, "textures/concrete.jpg");
    int[] textureId8 = TextureLibrary.loadTexture(gl, "textures/cloud3.jpg");
    int[] textureId9 = TextureLibrary.loadTexture(gl, "textures/window.jpg");
    int[] textureId10 = TextureLibrary.loadTexture(gl, "textures/night.jpg");
        
    light[0] = new Light(gl);
    light[1] = new Light(gl);
    light[2] = new Light(gl);
    light[3] = new Light(gl);
    light[0].setCamera(camera);
    light[1].setCamera(camera);
    light[2].setCamera(camera);
    light[3].setCamera(camera);
    
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    Material material = new Material(new Vec3(0.5f, 0.5f, 0.5f), new Vec3(0.7f, 0.7f, 0.7f), new Vec3(0.3f, 0.3f, 1.0f), 32.0f);
    Mat4 modelMatrix = Mat4Transform.scale(12,1f,12);
    floor = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId0);

    mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    material = new Material(new Vec3(1.0f, 1f, 1f), new Vec3(1.0f, 1f, 1f), new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90),Mat4Transform.scale(64,1f,32));
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0,0,-16),modelMatrix);
    sky2 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId10);

    mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    material = new Material(new Vec3(1f, 1f, 1f), new Vec3(1f, 1f, 1f), new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90),Mat4Transform.scale(12,1f,12));
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90),modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-6,6,0),modelMatrix);
    wall1 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId7);

    mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    material = new Material(new Vec3(1f, 1f, 1f), new Vec3(1f, 1f, 1f), new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90),Mat4Transform.scale(12,1f,12));
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(-90),modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(6,6,0),modelMatrix);
    wall2 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId7);

    mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05_window.txt");
    material = new Material(new Vec3(0.5f, 0.5f, 0.5f), new Vec3(0.7f, 0.7f, 0.7f), new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90),Mat4Transform.scale(32,1f,16));
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0,0,-8),modelMatrix);
    windowBottom = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId9);

    mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    shader = new Shader(gl, "vs_tt_05_move.txt", "fs_tt_05_window.txt");
    material = new Material(new Vec3(0.5f, 0.5f, 0.5f), new Vec3(0.7f, 0.7f, 0.7f), new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90),Mat4Transform.scale(32,1f,16));
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0,16,-8),modelMatrix);
    windowTop = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId8);
    
    mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
    shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
    sphere = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId1);
    sphere2 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId5, textureId6);
    sphere3 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId2);
    
    mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
    cube = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId3);
    
    cube2 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId5, textureId6); 
    cube3 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId1);
    material = new Material(new Vec3(1.0f, 0.1f, 0.1f), new Vec3(1.0f, 0.1f, 0.1f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f); 
    cube4 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId4); 

    //BUILD FUNCTIONS FOR OUR SCENE GRAPH OBJECTS
    buildTable();
    buildWindowFrame();
    buildLamp1();
    buildLamp2();
    lamp1position1();
    lamp2position1();
      
    lamp1root.update();
    lamp2root.update();    
    tableroot.update();
    windowframeroot.update();

    //robotRoot.print(0, false);
    //System.exit(0);
  }
 
  private void render(GL3 gl) {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    //RENDER LIGHTS THAT ARE OPEN 
    if(light[0].status){
      light[0].setPosition(new Vec3(5.0f, 5.0f, 5.0f));  // reset light position to default
      light[0].render(gl);
    }
    if(light[1].status){
      light[1].setPosition(new Vec3(-5.0f, 5.0f, -5.0f));  // reset light position to default
      light[1].render(gl); // reset light position to default
    }
    //NEW RENDER FUNCTION FOR SPOTLIGHTS THAT CALCULATE WHERE THEY ARE IN THE WORLD AND THE FRONT OF THE OBJECT THEY ARE CONNECTED TO
    if(light[2].status) 
      light[2].render(gl, lamp1Head.getWorldTransform(),"right");
    if(light[3].status)
      light[3].render(gl, lamp2Head.getWorldTransform(),"left");
    floor.render(gl); 
    wall1.render(gl);
    wall2.render(gl);
    sky2.render(gl);
    windowBottom.render(gl);
    windowTop.render(gl);
    //CALL ANIMATIONS AFTER WE FINISH RENDERING THEM ALL
    runAnimations();
    lamp1root.draw(gl);
    lamp2root.draw(gl);
    tableroot.draw(gl);
    windowframeroot.draw(gl);
  }
  // I HAVE ADDED ROTATION TARGET AND ROTATION AXIS TO TRANSFORMNODES TO MAKE ANIMATION CALCULATIONS
  private void runAnimations(){
    List<TransformNode> animation_nodes_lamp1 = new ArrayList<TransformNode>();
    List<TransformNode> animation_nodes_lamp2 = new ArrayList<TransformNode>();
    animation_nodes_lamp1.add(lamp1lowerArmRotate);
    animation_nodes_lamp1.add(lamp1UpperArmRotate);
    animation_nodes_lamp1.add(lamp1HeadRotate);
    animation_nodes_lamp2.add(lamp2lowerArmRotate);
    animation_nodes_lamp2.add(lamp2UpperArmRotate);
    animation_nodes_lamp2.add(lamp2HeadRotate);
    for(TransformNode node : animation_nodes_lamp1){
      if(node.animation){
        updateAnimation(lamp1root, node, node.rotationTarget, node.rotationAxis);
      }
    }
    for(TransformNode node : animation_nodes_lamp2){
      if(node.animation){
        updateAnimation(lamp2root, node, node.rotationTarget, node.rotationAxis);
      }
    }

      eggRotate.animation = true;
      eggTranslate.animation = true;
    if(eggRotate.animation){
      double elapsedTime = getSeconds()-startTime;
      float rotateAngle = 5f*(float)Math.sin(elapsedTime);
      eggRotate.setTransform(Mat4Transform.rotateAroundZ(rotateAngle));
      eggRotate.update();
    }
    if(eggTranslate.animation){
      double elapsedTime = getSeconds()-startTime;
      float translateY = Math.max(0, 0.3f*(float)Math.cos(elapsedTime));
      eggTranslate.setTransform(Mat4Transform.translate(0, translateY, 0));
      eggTranslate.update();
    }

  }

  private void updateAnimation(SGNode root, TransformNode transformnode,float angle,String axis){
    float step = 1f;
    if(transformnode.rotationTarget < transformnode.previousRotationAngle){
      transformnode.previousRotationAngle = transformnode.previousRotationAngle-step;
    }
    else{
      transformnode.previousRotationAngle = transformnode.previousRotationAngle+step;
    }
    
    if (transformnode.previousRotationAngle == angle){
      transformnode.animation = false;
    }
    if (axis == "x"){
      transformnode.setTransform(Mat4Transform.rotateAroundX(transformnode.previousRotationAngle));
    }
    if (axis == "y"){
      transformnode.setTransform(Mat4Transform.rotateAroundY(transformnode.previousRotationAngle));
    }
    if (axis == "z"){
      transformnode.setTransform(Mat4Transform.rotateAroundZ(transformnode.previousRotationAngle));
    }
    root.update();
  }

  
  // ***************************************************
  /* TIME
   */ 
  
  public static double startTime;
  
  public static double getSeconds() {
    return System.currentTimeMillis()/1000.0;
  }

  // ***************************************************
  /* An array of random numbers
   */ 
  
  private int NUM_RANDOMS = 1000;
  private float[] randoms;
  
  private void createRandomNumbers() {
    randoms = new float[NUM_RANDOMS];
    for (int i=0; i<NUM_RANDOMS; ++i) {
      randoms[i] = (float)Math.random();
    }
  }
  
// ***************************************************
// METHODS TO CREATE THE SCENE GRAPHS
// ***************************************************

  public void buildTable(){
    float tabletopWidth = 4f;
    float tabletopHeight = 0.3f;
    float tablelegWidth = 0.3f;
    float tablelegHeight = 3f;
    tableroot = new NameNode("table root");
    tableMoveTranslate = new TransformNode("table move", Mat4Transform.translate(0f,tablelegHeight,0));
    NameNode tabletop = new NameNode("tabletop");
      Mat4 m = Mat4Transform.scale(tabletopWidth, tabletopHeight, tabletopWidth);
      m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
      TransformNode tableScaleTranslate = new TransformNode("table scale translate", m);
        ModelNode tableTopShape = new ModelNode("Cube(tabletop)", cube);
      
      NameNode tableleg1 = new NameNode("tableleg1");
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.translate((tabletopWidth*0.5f)-(tablelegWidth*0.5f),0,(tabletopWidth*0.5f)-(tablelegWidth*0.5f)));
        m = Mat4.multiply(m, Mat4Transform.scale(tablelegWidth,tablelegHeight,tablelegWidth));
        m = Mat4.multiply(m, Mat4Transform.rotateAroundX(180));
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        TransformNode tableleg1ScaleTranslate = new TransformNode("tableleg1 scale translate", m);
          ModelNode tableleg1Shape = new ModelNode("Cube(tableleg1)", cube);

      NameNode tableleg2 = new NameNode("tableleg2");
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.translate((tabletopWidth*0.5f)-(tablelegWidth*0.5f),0,-(tabletopWidth*0.5f)+(tablelegWidth*0.5f)));
        m = Mat4.multiply(m, Mat4Transform.scale(tablelegWidth,tablelegHeight,tablelegWidth));
        m = Mat4.multiply(m, Mat4Transform.rotateAroundX(180));
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        TransformNode tableleg2ScaleTranslate = new TransformNode("tableleg2 scale translate", m);
          ModelNode tableleg2Shape = new ModelNode("Cube(tableleg2)", cube);
      
      NameNode tableleg3 = new NameNode("tableleg3");
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.translate(-(tabletopWidth*0.5f)+(tablelegWidth*0.5f),0,(tabletopWidth*0.5f)-(tablelegWidth*0.5f)));
        m = Mat4.multiply(m, Mat4Transform.scale(tablelegWidth,tablelegHeight,tablelegWidth));
        m = Mat4.multiply(m, Mat4Transform.rotateAroundX(180));
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        TransformNode tableleg3ScaleTranslate = new TransformNode("tableleg3 scale translate", m);
          ModelNode tableleg3Shape = new ModelNode("Cube(tableleg3)", cube);

      NameNode tableleg4 = new NameNode("tableleg4");
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.translate(-(tabletopWidth*0.5f)+(tablelegWidth*0.5f),0,-(tabletopWidth*0.5f)+(tablelegWidth*0.5f)));
        m = Mat4.multiply(m, Mat4Transform.scale(tablelegWidth,tablelegHeight,tablelegWidth));
        m = Mat4.multiply(m, Mat4Transform.rotateAroundX(180));
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        TransformNode tableleg4ScaleTranslate = new TransformNode("tableleg4 scale translate", m);
          ModelNode tableleg4Shape = new ModelNode("Cube(tableleg4)", cube);

      // egg
      NameNode eggBaseBox = new NameNode("eggbasebox");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.translate(0,tabletopHeight,0));
      m = Mat4.multiply(m, Mat4Transform.scale(1f,0.3f,0.5f));
      m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
      TransformNode eggBaseBoxScaleTranslate = new TransformNode("eggbasebox scale translate", m);
        ModelNode eggBaseBoxShape = new ModelNode("Cube(eggbasebox)", cube);

    NameNode egg = new NameNode("egg");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.translate(0,tabletopHeight+0.3f,0));
      m = Mat4.multiply(m, Mat4Transform.scale(0.65f,1f,0.65f));
      m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
      TransformNode eggScaleTranslate = new TransformNode("egg scale translate", m);
      eggRotate = new TransformNode("lamp1lowerarm rotate", Mat4Transform.rotateAroundX(0f));
      eggTranslate = new TransformNode("lamp1lowerarm translate", Mat4Transform.translate(0f,0f,0f));
        ModelNode eggShape = new ModelNode("Sphere(egg)", sphere2);


      //SCENE GRAPH
tableroot.addChild(tableMoveTranslate);
tableMoveTranslate.addChild(tabletop);
  tabletop.addChild(tableScaleTranslate);
    tableScaleTranslate.addChild(tableTopShape);

  tabletop.addChild(eggBaseBox);
    eggBaseBox.addChild(eggBaseBoxScaleTranslate);
      eggBaseBoxScaleTranslate.addChild(eggBaseBoxShape);
    eggBaseBox.addChild(eggRotate);
      eggRotate.addChild(eggTranslate);
        eggTranslate.addChild(egg);
        egg.addChild(eggScaleTranslate);
          eggScaleTranslate.addChild(eggShape);

  tabletop.addChild(tableleg1);
    tableleg1.addChild(tableleg1ScaleTranslate);
      tableleg1ScaleTranslate.addChild(tableleg1Shape);
  tabletop.addChild(tableleg2);
    tableleg2.addChild(tableleg2ScaleTranslate);
      tableleg2ScaleTranslate.addChild(tableleg2Shape);
  tabletop.addChild(tableleg3);
    tableleg3.addChild(tableleg3ScaleTranslate);
      tableleg3ScaleTranslate.addChild(tableleg3Shape);
  tabletop.addChild(tableleg4);
    tableleg4.addChild(tableleg4ScaleTranslate);
      tableleg4ScaleTranslate.addChild(tableleg4Shape);

      tableroot.update();

}
public void buildWindowFrame(){
    float windowWidth = 12f;
  float windowHeight = 12f;
  float windowFrameWidth = 0.2f;
  float windowFrameHeight = 0.1f;
  windowframeroot = new NameNode("window frame root");
  windowframeMoveTranslate = new TransformNode("windowframe move", Mat4Transform.translate(0f,0,-6f));

  NameNode windowFrameBottom = new NameNode("windowframe");
    Mat4 m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.translate(0,0,0));
    m = Mat4.multiply(m, Mat4Transform.scale(windowWidth,windowFrameHeight,windowFrameWidth));
    m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode windowFrameBottomScaleTranslate = new TransformNode("windowframe scale translate", m);
      ModelNode windowFrameBottomShape = new ModelNode("Cube(windowframe)", cube);
  
  NameNode windowFrameTop = new NameNode("windowframe");
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.translate(0,windowHeight,0));
    m = Mat4.multiply(m, Mat4Transform.scale(windowWidth,windowFrameHeight,windowFrameWidth));
    m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode windowFrameTopScaleTranslate = new TransformNode("windowframe scale translate", m);
      ModelNode windowFrameTopShape = new ModelNode("Cube(windowframe)", cube);

  NameNode windowFrameLeft = new NameNode("windowframe");
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.translate(-6f,0,0));
    m = Mat4.multiply(m, Mat4Transform.scale(windowFrameWidth,windowHeight,windowFrameWidth));
    m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode windowFrameLeftScaleTranslate = new TransformNode("windowframe scale translate", m);
      ModelNode windowFrameLeftShape = new ModelNode("Cube(windowframe)", cube);

    NameNode windowFrameRight = new NameNode("windowframe");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.translate(6f,0,0));
      m = Mat4.multiply(m, Mat4Transform.scale(windowFrameWidth,windowHeight,windowFrameWidth));
      m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
      TransformNode windowFrameRightScaleTranslate = new TransformNode("windowframe scale translate", m);
        ModelNode windowFrameRightShape = new ModelNode("Cube(windowframe)", cube);


    windowframeroot.addChild(windowframeMoveTranslate);
    windowframeMoveTranslate.addChild(windowFrameBottom);
    windowFrameBottom.addChild(windowFrameBottomScaleTranslate);
        windowFrameBottomScaleTranslate.addChild(windowFrameBottomShape);
  
        windowframeMoveTranslate.addChild(windowFrameTop);
            windowFrameTop.addChild(windowFrameTopScaleTranslate);
              windowFrameTopScaleTranslate.addChild(windowFrameTopShape);
  
        windowframeMoveTranslate.addChild(windowFrameLeft);
            windowFrameLeft.addChild(windowFrameLeftScaleTranslate);
              windowFrameLeftScaleTranslate.addChild(windowFrameLeftShape);
        
        windowframeMoveTranslate.addChild(windowFrameRight);
         windowFrameRight.addChild(windowFrameRightScaleTranslate);
            windowFrameRightScaleTranslate.addChild(windowFrameRightShape);

    windowframeroot.update();
}
public void buildLamp1(){
  float lamp1baseheight = 0.5f;
      float lamp1Armheight = 2.7f;
      lamp1root = new NameNode("lamp1 root");
      lamp1MoveTranslate = new TransformNode("lamp1 move", Mat4Transform.translate(-4f,0f,0f));

      NameNode lamp1Base = new NameNode("lamp1base");
        Mat4 m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(1f,lamp1baseheight,0.5f));
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        TransformNode lamp1BaseScaleTranslate = new TransformNode("lamp1base scale translate", m);
          ModelNode lamp1BaseShape = new ModelNode("Cube(lamp1base)", cube4);
      
      NameNode lamp1lowerArm = new NameNode("lamp1lowerarm");
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(0.3f,lamp1Armheight,0.3f));
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        TransformNode lamp1lowerArmScaleTranslate = new TransformNode("lamp1lowerarm scale translate", m);
        TransformNode lamp1lowerArmTranslateToTop = new TransformNode("lamp1lowerarm translate to top", Mat4Transform.translate(0,lamp1baseheight,0));
          lamp1lowerArmRotate = new TransformNode("lamp1lowerarm rotate", Mat4Transform.rotateAroundX(0f));
          ModelNode lamp1lowerArmShape = new ModelNode("Sphere(lamp1lowerarm)", sphere3);
      
      NameNode lamp1joint = new NameNode("lamp1joint");
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.translate(0,lamp1Armheight,0));
        m = Mat4.multiply(m, Mat4Transform.scale(0.3f,0.3f,0.3f));
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        TransformNode lamp1jointScaleTranslate = new TransformNode("lamp1joint scale translate", m);
          ModelNode lamp1jointShape = new ModelNode("Sphere(lamp1joint)", sphere3);

      NameNode lamp1upperArm = new NameNode("lamp1upperarm");
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(0.3f,lamp1Armheight,0.3f));
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        TransformNode lamp1upperTranslateToTop = new TransformNode("lamp1upperarm translate to top", Mat4Transform.translate(0,0.3f+lamp1Armheight,0));
        TransformNode lamp1upperArmScaleTranslate = new TransformNode("lamp1upperarm scale translate", m);
        lamp1UpperArmRotate = new TransformNode("lamp1Upperarm rotate", Mat4Transform.rotateAroundX(0f));
          ModelNode lamp1upperArmShape = new ModelNode("Sphere(lamp1upperarm)", sphere3);

      lamp1Head = new NameNode("lamp1head");
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(0.5f,0.5f,0.5f));
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        TransformNode lamp1HeadTranslateToTop = new TransformNode("lamp1head translate to top", Mat4Transform.translate(0,(lamp1Armheight),0));
        TransformNode lamp1HeadScaleTranslate = new TransformNode("lamp1head scale translate", m);
        lamp1HeadRotate = new TransformNode("lamp1Head rotate", Mat4Transform.rotateAroundX(0f));
          ModelNode lamp1HeadShape = new ModelNode("cube(lamp1head)", cube4);

        
          NameNode lamp1headdecoration1 = new NameNode("lamp1joint");
          m = new Mat4(1);
          m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
          m = Mat4.multiply(m, Mat4Transform.scale(0.3f,0.3f,0.3f));
          m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
          TransformNode lamp1headdecoration1ScaleTranslate = new TransformNode("lamp1joint scale translate", m);
            ModelNode lamp1headdecoration1Shape = new ModelNode("Sphere(lamp1joint)", sphere3);

            NameNode lamp1headdecoration2 = new NameNode("lamp1joint");
            m = new Mat4(1);
            m = Mat4.multiply(m, Mat4Transform.translate(0.3f,0.5f,0));
            m = Mat4.multiply(m, Mat4Transform.scale(0.3f,0.3f,0.3f));
            m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
            TransformNode lamp1headdecoration2ScaleTranslate = new TransformNode("lamp1joint scale translate", m);
              ModelNode lamp1headdecoration2Shape = new ModelNode("Sphere(lamp1joint)", sphere3);

              NameNode lamp1headdecoration3 = new NameNode("lamp1joint");
              m = new Mat4(1);
              m = Mat4.multiply(m, Mat4Transform.translate(-0.3f,0.5f,0));
              m = Mat4.multiply(m, Mat4Transform.scale(0.3f,0.3f,0.3f));
              m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
              TransformNode lamp1headdecoration3ScaleTranslate = new TransformNode("lamp1joint scale translate", m);
                ModelNode lamp1headdecoration3Shape = new ModelNode("Sphere(lamp1joint)", sphere3);


                NameNode lamp1jointdecoration1 = new NameNode("lamp1joint");
                m = new Mat4(1);
                m = Mat4.multiply(m, Mat4Transform.translate(-0.4f,lamp1Armheight,0));
                m = Mat4.multiply(m, Mat4Transform.scale(0.8f,0.2f,0.2f));
                m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
                TransformNode lamp1jointdecoration1ScaleTranslate = new TransformNode("lamp1joint scale translate", m);
                  ModelNode lamp1jointdecoration1Shape = new ModelNode("Sphere(lamp1joint)", cube4);
          
          lamp1root.addChild(lamp1MoveTranslate);
          lamp1MoveTranslate.addChild(lamp1Base);
            lamp1Base.addChild(lamp1BaseScaleTranslate);
              lamp1BaseScaleTranslate.addChild(lamp1BaseShape);
            lamp1Base.addChild(lamp1lowerArmTranslateToTop);
              lamp1lowerArmTranslateToTop.addChild(lamp1lowerArmRotate);
              lamp1lowerArmRotate.addChild(lamp1lowerArm);
                lamp1lowerArm.addChild(lamp1lowerArmScaleTranslate);
                  lamp1lowerArmScaleTranslate.addChild(lamp1lowerArmShape);
              lamp1lowerArm.addChild(lamp1joint);
                lamp1joint.addChild(lamp1jointScaleTranslate);
                  lamp1jointScaleTranslate.addChild(lamp1jointShape);
                lamp1joint.addChild(lamp1jointdecoration1);
                  lamp1jointdecoration1.addChild(lamp1jointdecoration1ScaleTranslate);
                    lamp1jointdecoration1ScaleTranslate.addChild(lamp1jointdecoration1Shape);
                lamp1joint.addChild(lamp1upperTranslateToTop);
                lamp1upperTranslateToTop.addChild(lamp1UpperArmRotate);
                lamp1UpperArmRotate.addChild(lamp1upperArm);
                  lamp1upperArm.addChild(lamp1upperArmScaleTranslate);
                    lamp1upperArmScaleTranslate.addChild(lamp1upperArmShape);
                     lamp1UpperArmRotate.addChild(lamp1HeadTranslateToTop);
                     lamp1HeadTranslateToTop.addChild(lamp1HeadRotate);
                     lamp1HeadRotate.addChild(lamp1Head);
                      lamp1Head.addChild(lamp1HeadScaleTranslate);
                        lamp1HeadScaleTranslate.addChild(lamp1HeadShape);
                        lamp1HeadRotate.addChild(lamp1headdecoration1);
                        lamp1headdecoration1.addChild(lamp1headdecoration1ScaleTranslate);
                          lamp1headdecoration1ScaleTranslate.addChild(lamp1headdecoration1Shape);
                        lamp1HeadRotate.addChild(lamp1headdecoration2);
                        lamp1headdecoration2.addChild(lamp1headdecoration2ScaleTranslate);
                          lamp1headdecoration2ScaleTranslate.addChild(lamp1headdecoration2Shape);
                        lamp1HeadRotate.addChild(lamp1headdecoration3);
                        lamp1headdecoration3.addChild(lamp1headdecoration3ScaleTranslate);
                          lamp1headdecoration3ScaleTranslate.addChild(lamp1headdecoration3Shape);
    
  }
  public void buildLamp2(){
    // anglepoise lamp 2
    float lamp2baseheight = 0.5f;
    float lamp2Armheight = 2.3f;
    lamp2root = new NameNode("lamp2 root");
    lamp2MoveTranslate = new TransformNode("lamp2 move", Mat4Transform.translate(4f,0f,0f));

    NameNode lamp2Base = new NameNode("lamp2base");
      Mat4 m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(1f,lamp2baseheight,0.5f));
      m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
      TransformNode lamp2BaseScaleTranslate = new TransformNode("lamp2base scale translate", m);
        ModelNode lamp2BaseShape = new ModelNode("Cube(lamp2base)", cube);

    NameNode lamp2lowerArm = new NameNode("lamp2lowerarm");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(0.3f,lamp2Armheight,0.3f));
      m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
      TransformNode lamp2lowerArmScaleTranslate = new TransformNode("lamp2lowerarm scale translate", m);
      TransformNode lamp2lowerArmTranslateToTop = new TransformNode("lamp2lowerarm translate to top", Mat4Transform.translate(0,lamp2baseheight,0));
        lamp2lowerArmRotate = new TransformNode("lamp2lowerarm rotate", Mat4Transform.rotateAroundX(0f));
        ModelNode lamp2lowerArmShape = new ModelNode("Sphere(lamp2lowerarm)", sphere);

    NameNode lamp2joint = new NameNode("lamp2joint");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.translate(0,lamp2Armheight,0));
      m = Mat4.multiply(m, Mat4Transform.scale(0.3f,0.3f,0.3f));
      m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
      TransformNode lamp2jointScaleTranslate = new TransformNode("lamp2joint scale translate", m);
        ModelNode lamp2jointShape = new ModelNode("Sphere(lamp2joint)", sphere);

    NameNode lamp2upperArm = new NameNode("lamp2upperarm");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(0.3f,lamp2Armheight,0.3f));
      m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
      TransformNode lamp2upperTranslateToTop = new TransformNode("lamp2upperarm translate to top", Mat4Transform.translate(0,0.3f+lamp2Armheight,0));
      TransformNode lamp2upperArmScaleTranslate = new TransformNode("lamp2upperarm scale translate", m);
      lamp2UpperArmRotate = new TransformNode("lamp2Upperarm rotate", Mat4Transform.rotateAroundX(0f));
        ModelNode lamp2upperArmShape = new ModelNode("Sphere(lamp2upperarm)", sphere);

    lamp2Head = new NameNode("lamp2head");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(0.7f,0.5f,0.5f));
      m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
      TransformNode lamp2HeadTranslateToTop = new TransformNode("lamp2head translate to top", Mat4Transform.translate(0,(lamp2Armheight),0));
      TransformNode lamp2HeadScaleTranslate = new TransformNode("lamp2head scale translate", m);
      lamp2HeadRotate = new TransformNode("lamp2Head rotate", Mat4Transform.rotateAroundX(0f));
        ModelNode lamp2HeadShape = new ModelNode("cube(lamp2head)", cube3);

        NameNode lamp2headdecoration1 = new NameNode("lamp1joint");
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.translate(-0.3f,0.5f,-0.2f));
        m = Mat4.multiply(m, Mat4Transform.scale(0.1f,0.3f,0.1f));
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        TransformNode lamp2headdecoration1ScaleTranslate = new TransformNode("lamp1joint scale translate", m);
          ModelNode lamp2headdecoration1Shape = new ModelNode("Sphere(lamp1joint)", cube);

          NameNode lamp2headdecoration2 = new NameNode("lamp1joint");
          m = new Mat4(1);
          m = Mat4.multiply(m, Mat4Transform.translate(-0.1f,0.5f,0.1f));
          m = Mat4.multiply(m, Mat4Transform.scale(0.1f,0.3f,0.1f));
          m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
          TransformNode lamp2headdecoration2ScaleTranslate = new TransformNode("lamp1joint scale translate", m);
            ModelNode lamp2headdecoration2Shape = new ModelNode("Sphere(lamp1joint)", cube);

            NameNode lamp2headdecoration3 = new NameNode("lamp1joint");
            m = new Mat4(1);
            m = Mat4.multiply(m, Mat4Transform.translate(0.1f,0.5f,-0.2f));
            m = Mat4.multiply(m, Mat4Transform.scale(0.1f,0.3f,0.1f));
            m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
            TransformNode lamp2headdecoration3ScaleTranslate = new TransformNode("lamp1joint scale translate", m);
              ModelNode lamp2headdecoration3Shape = new ModelNode("Sphere(lamp1joint)", cube);

            NameNode lamp2headdecoration4 = new NameNode("lamp1joint");
            m = new Mat4(1);
            m = Mat4.multiply(m, Mat4Transform.translate(0.1f,0.5f,0.2f));
            m = Mat4.multiply(m, Mat4Transform.scale(0.3f,0.3f,0.1f));
            m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
            TransformNode lamp2headdecoration4ScaleTranslate = new TransformNode("lamp1joint scale translate", m);
              ModelNode lamp2headdecoration4Shape = new ModelNode("Sphere(lamp1joint)", cube);

              NameNode lamp2feetdecoration1 = new NameNode("lamp1joint");
              m = new Mat4(1);
              m = Mat4.multiply(m, Mat4Transform.translate(-0.5f,0.5f,0.2f));
              m = Mat4.multiply(m, Mat4Transform.scale(0.3f,0.3f,0.3f));
              m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
              TransformNode lamp2feetdecoration1ScaleTranslate = new TransformNode("lamp1joint scale translate", m);
                ModelNode lamp2feetdecoration1Shape = new ModelNode("Sphere(lamp1joint)", sphere);
                
                NameNode lamp2feetdecoration2 = new NameNode("lamp1joint");
                m = new Mat4(1);
                m = Mat4.multiply(m, Mat4Transform.translate(-0.5f,0.5f,-0.1f));
                m = Mat4.multiply(m, Mat4Transform.scale(0.3f,0.3f,0.3f));
                m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
                TransformNode lamp2feetdecoration2ScaleTranslate = new TransformNode("lamp1joint scale translate", m);
                  ModelNode lamp2feetdecoration2Shape = new ModelNode("Sphere(lamp1joint)", sphere);


    lamp2root.addChild(lamp2MoveTranslate);
    lamp2MoveTranslate.addChild(lamp2Base);
    lamp2Base.addChild(lamp2BaseScaleTranslate);
      lamp2BaseScaleTranslate.addChild(lamp2BaseShape);

    lamp2Base.addChild(lamp2feetdecoration1);
      lamp2feetdecoration1.addChild(lamp2feetdecoration1ScaleTranslate);
        lamp2feetdecoration1ScaleTranslate.addChild(lamp2feetdecoration1Shape);
    lamp2Base.addChild(lamp2feetdecoration2);
      lamp2feetdecoration2.addChild(lamp2feetdecoration2ScaleTranslate);
        lamp2feetdecoration2ScaleTranslate.addChild(lamp2feetdecoration2Shape);
    lamp2Base.addChild(lamp2lowerArmTranslateToTop);

      lamp2lowerArmTranslateToTop.addChild(lamp2lowerArmRotate);
      lamp2lowerArmRotate.addChild(lamp2lowerArm);
        lamp2lowerArm.addChild(lamp2lowerArmScaleTranslate);
          lamp2lowerArmScaleTranslate.addChild(lamp2lowerArmShape);
      lamp2lowerArm.addChild(lamp2joint);
        lamp2joint.addChild(lamp2jointScaleTranslate);
          lamp2jointScaleTranslate.addChild(lamp2jointShape);
        lamp2joint.addChild(lamp2upperTranslateToTop);
        lamp2upperTranslateToTop.addChild(lamp2UpperArmRotate);
        lamp2UpperArmRotate.addChild(lamp2upperArm);
          lamp2upperArm.addChild(lamp2upperArmScaleTranslate);
            lamp2upperArmScaleTranslate.addChild(lamp2upperArmShape);
            lamp2UpperArmRotate.addChild(lamp2HeadTranslateToTop);
            lamp2HeadTranslateToTop.addChild(lamp2HeadRotate);
            lamp2HeadRotate.addChild(lamp2Head);
              lamp2Head.addChild(lamp2HeadScaleTranslate);
                lamp2HeadScaleTranslate.addChild(lamp2HeadShape);

            lamp2HeadRotate.addChild(lamp2headdecoration1);
            lamp2headdecoration1.addChild(lamp2headdecoration1ScaleTranslate);
                lamp2headdecoration1ScaleTranslate.addChild(lamp2headdecoration1Shape);
            lamp2HeadRotate.addChild(lamp2headdecoration2);
            lamp2headdecoration2.addChild(lamp2headdecoration2ScaleTranslate);
                lamp2headdecoration2ScaleTranslate.addChild(lamp2headdecoration2Shape);
            lamp2HeadRotate.addChild(lamp2headdecoration3);
            lamp2headdecoration3.addChild(lamp2headdecoration3ScaleTranslate);
                lamp2headdecoration3ScaleTranslate.addChild(lamp2headdecoration3Shape);
            lamp2HeadRotate.addChild(lamp2headdecoration4);
            lamp2headdecoration4.addChild(lamp2headdecoration4ScaleTranslate);
                lamp2headdecoration4ScaleTranslate.addChild(lamp2headdecoration4Shape);
  }
}