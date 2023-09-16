package freed.camera;

import processing.core.*;

import java.util.HashMap;

import static java.lang.Math.PI;

/**
 * This is a template class and can be used to start a new processing Library.
 * Make sure you rename this class as well as the name of the example package 'template' 
 * to your own Library naming convention.
 * 
 * (the tag example followed by the name of an example included in folder 'examples' will
 * automatically include the example in the javadoc.)
 *
 * @example Hello 
 */

public class FreeD {
	PApplet applet;

	FreeDListener listener;

	public HashMap<Integer, FreeDBuffer> cameras;

	public final static String VERSION = "1.0.0";
	

	/**
	 * a Constructor, usually called in the setup() method in your sketch to
	 * initialize and start the Library.
	 * 
	 * @example Hello
	 * @param parent the parent PApplet
	 * @param port the port on which to listen
	 */
	public FreeD(PApplet parent, int port) {
		applet = parent;
		listener = new FreeDListener();
		cameras = new HashMap();
		listener.parent = parent;
		listener.port = port;
		listener.cameras = cameras;
		listener.start();
		parent.registerMethod("dispose", this);
	}


	public FreeD(PApplet parent){
		this(parent, 40000);
	}

	/**
	 * return the version of the Library.
	 * 
	 * @return String
	 */
	public static String version() {
		return VERSION;
	}

	public Camera camera(int id){
		FreeDBuffer camera = cameras.get(id);
		if(camera == null){
			camera = new FreeDBuffer();
		}
		Camera cam = new Camera();
		cam.id = camera.id;
		cam.focus = camera.focus;
		cam.zoom = camera.zoom;
		cam.x = -camera.posZ;
		cam.y = camera.posY;
		cam.z = camera.posX;
		cam.pitch = camera.pitch / 180 * (float)PI;
		cam.yaw = camera.yaw / 180 * (float)PI;
		cam.roll = -camera.roll / 180 * (float)PI;

		return cam;
	}

	public void dispose() {
		listener.active = false;
	}
}

