import gmaths.*;
import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;

public class Model {
  
  private Mesh mesh;
  private int[] textureId1; 
  private int[] textureId2; 
  private Material material;
  private Shader shader;
  private Mat4 modelMatrix;
  private Camera camera;
  private Light[] light;
  
  public Model(GL3 gl, Camera camera, Light[] light, Shader shader, Material material, Mat4 modelMatrix, Mesh mesh, int[] textureId1, int[] textureId2) {
    this.mesh = mesh;
    this.material = material;
    this.modelMatrix = modelMatrix;
    this.shader = shader;
    this.camera = camera;
    this.light = light;
    this.textureId1 = textureId1;
    this.textureId2 = textureId2;
  }
  
  public Model(GL3 gl, Camera camera, Light[] light, Shader shader, Material material, Mat4 modelMatrix, Mesh mesh, int[] textureId1) {
    this(gl, camera, light, shader, material, modelMatrix, mesh, textureId1, null);
  }
  
  public Model(GL3 gl, Camera camera, Light[] light, Shader shader, Material material, Mat4 modelMatrix, Mesh mesh) {
    this(gl, camera, light, shader, material, modelMatrix, mesh, null, null);
  }
  
  public void setModelMatrix(Mat4 m) {
    modelMatrix = m;
  }
  
  public void setCamera(Camera camera) {
    this.camera = camera;
  }
  
  public void setLight(Light[] light) {
    this.light = light;
  }

  public void render(GL3 gl, Mat4 modelMatrix) {
    Mat4 mvpMatrix = Mat4.multiply(camera.getPerspectiveMatrix(), Mat4.multiply(camera.getViewMatrix(), modelMatrix));
    shader.use(gl);
    shader.setFloatArray(gl, "model", modelMatrix.toFloatArrayForGLSL());
    shader.setFloatArray(gl, "mvpMatrix", mvpMatrix.toFloatArrayForGLSL());
    
    shader.setVec3(gl, "viewPos", camera.getPosition());

    shader.setVec3(gl, "pointLight[0].position", light[0].getPosition());
    shader.setVec3(gl, "pointLight[0].ambient", light[0].getMaterial().getAmbient());
    shader.setVec3(gl, "pointLight[0].diffuse", light[0].getMaterial().getDiffuse());
    shader.setVec3(gl, "pointLight[0].specular", light[0].getMaterial().getSpecular());
    //attenuation
    shader.setFloat(gl, "pointLight[0].constant", 1.0f);
    shader.setFloat(gl, "pointLight[0].linear", 0.06f);
    shader.setFloat(gl, "pointLight[0].quadratic", 0.020f);
    
    shader.setVec3(gl, "pointLight[1].position", light[1].getPosition());
    shader.setVec3(gl, "pointLight[1].ambient", light[1].getMaterial().getAmbient());
    shader.setVec3(gl, "pointLight[1].diffuse", light[1].getMaterial().getDiffuse());
    shader.setVec3(gl, "pointLight[1].specular", light[1].getMaterial().getSpecular());
    //attenuation
    shader.setFloat(gl, "pointLight[1].constant", 1.0f);
    shader.setFloat(gl, "pointLight[1].linear", 0.06f);
    shader.setFloat(gl, "pointLight[1].quadratic", 0.020f);
 
    shader.setVec3(gl, "pointLight[2].ambient", light[2].getMaterial().getAmbient());
    shader.setVec3(gl, "pointLight[2].diffuse", light[2].getMaterial().getDiffuse());
    shader.setVec3(gl, "pointLight[2].specular", light[2].getMaterial().getSpecular());
    //attenuation
    shader.setFloat(gl, "pointLight[2].constant", 1.0f);
    shader.setFloat(gl, "pointLight[2].linear", 0.09f);
    shader.setFloat(gl, "pointLight[2].quadratic", 0.032f);
    //spotlight
    //shader.setVec3(gl, "pointLight[2].position", camera.getPosition());
    shader.setVec3(gl,"pointLight[2].position", light[2].getPosition());
    shader.setVec3(gl, "pointLight[2].direction", light[2].getDirection());
    shader.setFloat(gl, "pointLight[2].cutOff", (float)Math.cos(Math.toRadians(15.5f)));
    shader.setFloat(gl, "pointLight[2].outerCutOff", (float)Math.cos(Math.toRadians(18.5f)));


    shader.setVec3(gl, "pointLight[3].ambient", light[3].getMaterial().getAmbient());
    shader.setVec3(gl, "pointLight[3].diffuse", light[3].getMaterial().getDiffuse());
    shader.setVec3(gl, "pointLight[3].specular", light[3].getMaterial().getSpecular());
    //attenuation
    shader.setFloat(gl, "pointLight[3].constant", 1.0f);
    shader.setFloat(gl, "pointLight[3].linear", 0.09f);
    shader.setFloat(gl, "pointLight[3].quadratic", 0.032f);
    //spotlight
    //shader.setVec3(gl, "pointLight[2].position", camera.getPosition());
    shader.setVec3(gl,"pointLight[3].position", light[3].getPosition());
    shader.setVec3(gl, "pointLight[3].direction", light[3].getDirection());
    shader.setFloat(gl, "pointLight[3].cutOff", (float)Math.cos(Math.toRadians(15.5f)));
    shader.setFloat(gl, "pointLight[3].outerCutOff", (float)Math.cos(Math.toRadians(18.5f)));


    shader.setVec3(gl, "material.ambient", material.getAmbient());
    shader.setVec3(gl, "material.diffuse", material.getDiffuse());
    shader.setVec3(gl, "material.specular", material.getSpecular());
    shader.setFloat(gl, "material.shininess", material.getShininess()); 
    
    double elapsedTime = M04_GLEventListener.getSeconds() - M04_GLEventListener.startTime;
    double t = elapsedTime*0.05;
    float offsetX = (float)(t - Math.floor(t));
    float offsetY = 0.0f;
    shader.setFloat(gl, "offset", offsetX, offsetY);

    if (textureId1!=null) {
      shader.setInt(gl, "first_texture", 0);  // be careful to match these with GL_TEXTURE0 and GL_TEXTURE1
      gl.glActiveTexture(GL.GL_TEXTURE0);
      gl.glBindTexture(GL.GL_TEXTURE_2D, textureId1[0]);
    }
    if (textureId2!=null) {
      shader.setInt(gl, "second_texture", 1);
      gl.glActiveTexture(GL.GL_TEXTURE1);
      gl.glBindTexture(GL.GL_TEXTURE_2D, textureId2[0]);
    }
    mesh.render(gl);
  } 
  
  public void render(GL3 gl) {
    render(gl, modelMatrix);
  }
  
  public void dispose(GL3 gl) {
    mesh.dispose(gl);
    if (textureId1!=null) gl.glDeleteBuffers(1, textureId1, 0);
    if (textureId2!=null) gl.glDeleteBuffers(1, textureId2, 0);
  }
  
}