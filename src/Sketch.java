import peasy.PeasyCam;
import processing.core.PApplet;
import processing.core.PVector;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class Sketch extends PApplet {
    public int DIM = 128;
    PeasyCam cam;
    int counter = 0;
    int prozentStand = 0;
    public float time = pow(DIM, 3);
    ArrayList<PVector> points = new ArrayList<PVector>();

    public static void main(String args[]) {
        PApplet.main("Sketch");
    }

    public void settings() {
        size(600, 600, P3D);

    }

    public void setup() {
        FileWriter geek_file;
        try {
            geek_file = new FileWriter("ABC.csv");
            BufferedWriter geekwrite = new BufferedWriter(geek_file);

            cam = new PeasyCam(this, 500);

            for (int i = 0; i < DIM; i++) {
                for (int j = 0; j < DIM; j++) {
                    boolean edge = false;
                    for (int k = 0; k < DIM; k++) {
                        counter++;
                        if (((counter / time) * 100 >= prozentStand)) {
                            prozentStand++;
                            System.out.println(prozentStand + "%");
                        }

                        float x = map(i, 0, DIM, -1, 1);
                        float y = map(j, 0, DIM, -1, 1);
                        float z = map(k, 0, DIM, -1, 1);

                        float r = sqrt(x * x + y * y + z * z);
                        float theta = atan2(sqrt(x * x + y * y), z);
                        float phi = atan2(y, x);

                        PVector zeta = new PVector(0, 0, 0);


                        int n = 8;
                        int maxiterations = 100;
                        int iterations = 0;

                        while (true) {
                            Spherical sphericalZ = spherical(zeta.x, zeta.y, zeta.z);


                            float newX = pow(sphericalZ.r, n) * sin(sphericalZ.theta * n) * cos(sphericalZ.phi * n);
                            float newY = pow(sphericalZ.r, n) * sin(sphericalZ.theta * n) * sin(sphericalZ.phi * n);
                            float newZ = pow(sphericalZ.r, n) * cos(sphericalZ.theta * n);
                            zeta.x = newX + x;
                            zeta.y = newY + y;
                            zeta.z = newZ + z;
                            iterations++;
                            if (sphericalZ.r > 16) {
                                if (edge) {
                                    edge = false;
                                }
                                break;
                            }


                            if (iterations > maxiterations) {
                                if (!edge) {
                                    edge = true;
                                    color(r, theta, phi);
                                    geekwrite.append(x * 100 + ";" + y * 100 + ";" + z * 100+"\n");
                                    points.add(new PVector(x * 100, y * 100, z * 100));
                                }

                                break;
                            }
                        }

                    }
                }
            }
            geekwrite.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw() {
        background(0);
        for (PVector p : points) {
            stroke(255);
            point(p.x, p.y, p.z);
        }

    }

    class Spherical {
        float r;
        float theta;
        float phi;

        Spherical(float r, float theta, float phi) {
            this.r = r;
            this.theta = theta;
            this.phi = phi;
        }


    }

    Spherical spherical(float x, float y, float z) {
        float r = sqrt(x * x + y * y + z * z);
        float theta = atan2(sqrt(x * x + y * y), z);
        float phi = atan2(y, x);
        return new Spherical(r, theta, phi);
    }


}


