


import java.awt.event.KeyEvent;

import java.awt.event.KeyListener;

import java.awt.event.MouseEvent;

import java.awt.event.MouseListener;

import java.awt.event.MouseMotionListener;

import java.io.File;


import com.jogamp.opengl.*;

import com.jogamp.opengl.glu.GLU;

import com.jogamp.opengl.glu.GLUquadric;

//import com.jogamp.opengl.util.gl2.GLUT;

import com.jogamp.opengl.util.texture.*;




public class JoglEventListener implements GLEventListener, KeyListener, MouseListener, MouseMotionListener {

	int windowWidth, windowHeight;

	float orthoX=40;

	float Xclick, Yclick;

	float backrgb[] = new float[4]; 

	float rot; 
	float fov = 90f;

	Texture Basket = null; 

	Texture toppart = null;

	float redDiffuseMaterial []    = { 1.0f, 0.0f, 0.0f }; //set the material to red
	float whiteSpecularMaterial [] = { 1.0f, 1.0f, 1.0f }; //set the material to white
	float greenEmissiveMaterial [] = { 0.0f, 1.0f, 0.0f }; //set the material to green
	float whiteSpecularLight []    = { 1.0f, 1.0f, 1.0f }; //set the light specular to white
	float blankMaterial[]     = { 0.0f, 0.0f, 0.0f }; //set the material to black
	float grayMaterial[]     = { 0.7f, 0.7f, 0.7f }; //set the material to gray

	boolean animation=true;

	float YellowColor[]     = { 255.0f, 215.0f, 0.0f };

	Texture RBleacher = null;

	Texture botmpart = null;

	Texture Floor = null;

	Texture LBleacher = null;

	Texture Displayview = null;

	Texture Ball = null;

	int mouseX0, mouseY0;	
	float picked_x = 0.0f, picked_y = 0.0f;

	boolean rightClick = false;
	boolean leftClick = false;

	//angle of rotation
	float rotateX = 0.0f; // 
	float rotateY = 0.0f;
	float rotateZ = 0.0f;

	float X = 0;
	float Z = 0;
	
	float scale = 1;

	float oldRotateX = rotateX;
	float oldRotateY = rotateY; 

	float focalLength = 15f;
	float oldFocalLength = focalLength; //used for smoother changes in focal length
	
	// Used to more easily standardize relative distances
	float width = 49;
	float depth = 94;
	float height = 27;
	
	// Used to more easily edit the dimensions of the gym
	float bottomFrontRight[] = { width, 0, 0 };
	float bottomFrontLeft[] = { 0, 0, 0 };
	float bottomBackRight[] = { width, 0, depth };
	float bottomBackLeft[] = {0, 0, depth };
	
	float topFrontRight[] = { width, height, 0 };
	float topFrontLeft[] = { 0, height, 0 };
	float topBackRight[] = { width, height, depth };
	float topBackLeft[] = {0, height, depth };
	
	float freeThrowLocation[] = { width / 2f, 6f, 21f };

	private GLU glu = new GLU();

	//private GLUT glut = new GLUT();


	public void displayChanged(GLAutoDrawable gLDrawable, 

			boolean modeChanged, boolean deviceChanged) {

	}


	/** Called by the drawable immediately after the OpenGL context is

	 * initialized for the first time. Can be used to perform one-time OpenGL

	 * initialization such as setup of lights and display lists.

	 * @param gLDrawable The GLAutoDrawable object.

	 */

	public void init(GLAutoDrawable gLDrawable) {

		GL2 gl = gLDrawable.getGL().getGL2();





		gl.glClearDepth(1.0f);                      

		gl.glEnable(GL.GL_DEPTH_TEST);              

		gl.glDepthFunc(GL.GL_LEQUAL);              



		//Images

		try {

			RBleacher = TextureIO.newTexture(new File("audience.jpg"), false);
			LBleacher = TextureIO.newTexture(new File("audience.jpg"), false);
			Floor = TextureIO.newTexture(new File("Floor2.png"), false);
			Basket = TextureIO.newTexture(new File("Basket.bmp"), false);
			Ball=TextureIO.newTexture(new File("Ball.png"), false);

			gl.glEnable(GL.GL_TEXTURE);

			Basket.bind(gl);
			Floor.bind(gl);
			LBleacher.bind(gl);
			RBleacher.bind(gl);
			Ball.bind(gl);

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	}



	public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width, int height) {


		windowWidth = width;


		windowHeight = height;

		final GL2 gl = gLDrawable.getGL().getGL2();


		if (height <= 0) 

			height = 1;

		final float h = (float) width / (float) height;

		gl.glViewport(0, 0, width, height);

		gl.glMatrixMode(GL2.GL_PROJECTION);

		gl.glLoadIdentity();

		glu.gluPerspective(fov, h, .1,200);

		gl.glMatrixMode(GL2.GL_MODELVIEW);

		gl.glLoadIdentity();



	}


	@Override

	public void display(GLAutoDrawable gLDrawable) {

		// TODO Auto-generated method stub

		final GL2 gl = gLDrawable.getGL().getGL2();


		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		gl.glPushMatrix();
		
		gl.glRotatef(rotateX, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(rotateY, 0, 1, 0);
		gl.glRotatef(rotateZ, 0, 0, 1);
		
		glu.gluLookAt( freeThrowLocation[0], freeThrowLocation[1], freeThrowLocation[2], // Camera location
				freeThrowLocation[0], freeThrowLocation[1], freeThrowLocation[2] - 1, // Camera focal point
				0.0, 1.0, 0.0); // Up direction

		//CALL THE OBJECTs

		Gym(gl);

		ball(gl);

		gl.glPopMatrix();

		gl.glLoadIdentity();

	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		char key= e.getKeyChar();
		System.out.printf("Key typed: %c\n", key); 

		switch(key)
		{
		case 'x':
			rotateX += 5.0f;
			if(rotateX >= 360.0f)
				rotateX -= 360.0f;
			break;
		case 'X':
			rotateX -= 5.0f;
			if(rotateX <= 0)
				rotateX += 360;
			break;	
		case 'y':
			rotateY += 5.0f;
			if(rotateY >= 360.0f)
				rotateY -= 360.0f;
			System.out.println(rotateY);
			break;
		case 'Y':
			rotateY -= 5.0f;
			if(rotateY <= 0)
				rotateY += 360;
			System.out.println(rotateY);
			break;	
		case 'z':
			rotateZ += 5.0f;
			if(rotateZ >= 360.0f)
				rotateZ -= 360.0f;
			break;
		case 'Z':
			rotateZ -= 5.0f;
			if(rotateZ <= 0)
				rotateZ += 360;
			break;	

		case 'g':
		case 'G':
			focalLength += .05;

			break;

		case 'h':
		case 'H':
			focalLength -= .05;
			break;

		case 'w':
		case 'W':
			X += -.1*Math.sin( rotateY / 180 * Math.PI );
			Z += .1*Math.cos( rotateY / 180 * Math.PI );
			break;

		case 'a':
		case 'A':
			X += .1*Math.cos( rotateY / 180 * Math.PI );
			Z += .1*Math.sin( rotateY / 180 * Math.PI );
			break;

		case 's':
		case 'S':
			X += .1*Math.sin( rotateY / 180 * Math.PI );
			Z += -.1*Math.cos( rotateY / 180 * Math.PI );
			break;

		case 'd':
		case 'D':
			X += -.1*Math.cos( rotateY / 180 * Math.PI );
			Z += -.1*Math.sin( rotateY / 180 * Math.PI );
			break;
			
		case 'f':
			fov += 5;
			System.out.println(fov);
			break;
			
		case 'F':
			fov -= 5;
			System.out.println(fov);
			break;
			
		default:
			break;

		}

	}


	@Override

	public void keyPressed(KeyEvent e) {

		// TODO Auto-generated method stub

		char key= e.getKeyChar();

		System.out.printf("Key typed: %c\n", key); 

	}


	@Override
	public void mouseDragged(MouseEvent e) {

		float XX = (e.getX()-windowWidth*0.5f)*orthoX/windowWidth;
		float YY = -(e.getY()-windowHeight*0.5f)*orthoX/windowHeight;

		if( leftClick ) {
			rotateY = oldRotateY + (XX - picked_x) * 5;
			if( rotateY < 0 ) rotateY += 360;
			else if( rotateY > 360 ) rotateY -= 360;
			rotateX = oldRotateX + (picked_y - YY) * 5;
			if( rotateX < -90 ) { 
				rotateX = -90; 
			}
			else if( rotateX > 90 ) { 
				rotateX = 90;
			}
		}
		else if( rightClick ) {
			focalLength = oldFocalLength + (picked_y - YY);
		}

	}

	@Override

	public void mouseMoved(MouseEvent e) {}


	@Override

	public void mouseClicked(MouseEvent e) {}


	@Override

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		/*
		 * Coordinates printout
		 */
		picked_x = (e.getX()-windowWidth*0.5f)*orthoX/windowWidth;
		picked_y = -(e.getY()-windowHeight*0.5f)*orthoX/windowHeight;

		System.out.printf("Point clicked: (%.3f, %.3f)\n", picked_x, picked_y);

		mouseX0 = e.getX();
		mouseY0 = e.getY();

		if(e.getButton()==MouseEvent.BUTTON1) {	// Left button
			leftClick = true;
			oldRotateX = rotateX; //save old values so constant updates don't do strange things
			oldRotateY = rotateY;
		}
		else if(e.getButton()==MouseEvent.BUTTON3) {	// Right button
			rightClick = true;
			oldFocalLength = focalLength;
		}
	}


	@Override

	public void mouseReleased(MouseEvent e) {
		if( leftClick ) {
			leftClick = false;
		}
		if( rightClick ) {
			rightClick = false;
		}
	}


	@Override

	public void mouseEntered(MouseEvent e) {}


	@Override

	public void mouseExited(MouseEvent e) {}

	public void ball(final GL2 gl){

		GLUquadric sphere = glu.gluNewQuadric();

		gl.glEnable(GL.GL_TEXTURE_2D);

		//glu.gluQuadricDrawStyle(sphere, GL2.GL_FILL);

		glu.gluQuadricTexture(sphere,true); 

		glu.gluQuadricNormals(sphere, GL2.GL_SMOOTH);

		glu.gluQuadricTexture(sphere, true);

		//BALL "MOVES"

		gl.glTranslatef(0, -4, -5);

		gl.glRotated(-45, 0,-4, -5);

		Ball.bind(gl);

		glu.gluSphere(sphere,1,100,100);

		gl.glEnd();

		gl.glDisable(GL.GL_TEXTURE_2D);

	}

	public void Gym(final GL2 gl){

		Floor.bind(gl); 

		gl.glBlendFunc(GL2.GL_SRC_ALPHA,GL2.GL_ONE);

		gl.glEnable(GL.GL_TEXTURE_2D);

		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, redDiffuseMaterial, 0);

		gl.glMaterialfv(GL2.GL_BACK, GL2.GL_AMBIENT, redDiffuseMaterial, 0);



		// floor

		gl.glBegin(GL2.GL_QUADS);

		gl.glTexCoord2f(1,0); gl.glVertex3fv(bottomBackLeft, 0); 

		gl.glTexCoord2f(1,1); gl.glVertex3fv(bottomBackRight, 0); 

		gl.glTexCoord2f(0,1); gl.glVertex3fv(bottomFrontRight, 0); 

		gl.glTexCoord2f(0,0); gl.glVertex3fv(bottomFrontLeft, 0); 

		gl.glEnd();



		//Right

		RBleacher.bind(gl);

		gl.glBegin(GL2.GL_QUADS);

		gl.glTexCoord2f(0,0); gl.glVertex3fv(bottomBackRight, 0);

		gl.glTexCoord2f(1,0); gl.glVertex3fv(bottomFrontRight, 0);

		gl.glTexCoord2f(1,1); gl.glVertex3fv(topFrontRight, 0); 

		gl.glTexCoord2f(0,1); gl.glVertex3fv(topBackRight, 0); 

		gl.glEnd();

		//Left

		LBleacher.bind(gl);

		gl.glBegin(GL2.GL_QUADS);



		gl.glTexCoord2f(0,0); gl.glVertex3fv(bottomBackLeft, 0);

		gl.glTexCoord2f(1,0); gl.glVertex3fv(bottomFrontLeft, 0);

		gl.glTexCoord2f(1,1); gl.glVertex3fv(topFrontLeft, 0); 

		gl.glTexCoord2f(0,1); gl.glVertex3fv(topBackLeft, 0); 


		gl.glEnd(); 

		Basket.bind(gl); 

		gl.glBegin(GL2.GL_QUADS);





		gl.glTexCoord2f(0,0); gl.glVertex3fv(bottomFrontLeft, 0);  

		gl.glTexCoord2f(1,0); gl.glVertex3fv(bottomFrontRight, 0);

		gl.glTexCoord2f(1,1); gl.glVertex3fv(topFrontRight, 0);   

		gl.glTexCoord2f(0,1); gl.glVertex3fv(topFrontLeft, 0); 

		gl.glEnd(); 

		//Back

		/* 



        gl.glTexCoord2f(0,0);
gl.glVertex3f(10.0f, -8.0f, 10.0f);    

        gl.glTexCoord2f(1,0);
gl.glVertex3f(-10.0f, -8.0f, 10.0f);

        gl.glTexCoord2f(1,1);
gl.glVertex3f(-10.0f, 10.0f, 10.0f);   

        gl.glTexCoord2f(0,1);
gl.glVertex3f(10.0f, 10.0f, 10.0f); 

		 */







		gl.glDisable(GL.GL_TEXTURE_2D);

	}


}
