import freed.camera.*;
FreeD freed;

void setup(){
  freed = new FreeD(this);
  size(512, 512, P3D);
}

void draw(){
  background(128);

  var cam = freed.camera(0);

  beginCamera();
  camera(0, 0, 1000, 0, 0, 0, 0, 1, 0);

  rotateZ(cam.roll);
  rotateX(cam.yaw );
  rotateY(cam.pitch);
  translate(cam.x, cam.y, cam.z);

  endCamera();


  stroke(255);
  noFill();

  pushMatrix();
  translate(0,-90,0);
  var player2 = freed.camera(1);
  translate(player2.x, player2.y, player2.z);
  box(200);
  popMatrix();
  box(1000,1,1000);
}