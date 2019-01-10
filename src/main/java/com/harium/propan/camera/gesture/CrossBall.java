package com.harium.propan.camera.gesture;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.harium.etyl.commons.event.MouseEvent;
import com.harium.etyl.commons.event.PointerEvent;
import com.harium.etyl.commons.event.PointerState;
import com.harium.propan.camera.CameraController;

public class CrossBall extends CameraController {

    private static final float MIN_SCALE = 0.001f;
    private static final int FIRST_FINGER = 0;
    private static final int SECOND_FINGER = 1;
    private static final float UNDEFINED = -999;

    Matrix4 lastMatrix;

    int mx, my;

    // Scale Stuff
    int newMx, newMy;

    double startDistance = 0;
    float scale = 1;
    //float lastScale = 1;
    float originalScale = 1;
    float minDistance = UNDEFINED;

    // Greater is slower
    private float rotationSpeed = 1;
    private float scaleSpeed = 100f;

    //private boolean clicked = false;
    private int pointers = 0;
    private float yaw = 0;
    private float lastYaw = 0;

    private float pitch = 0;
    private float lastPitch = 0;

    public CrossBall() {
        super();
        lastMatrix = new Matrix4();
    }

    public CrossBall(Matrix4 transform) {
        super(transform);
        lastMatrix = new Matrix4(transform);
    }

    public boolean update() {
        if (pointers == 1) {
            matrix.set(lastMatrix);
            applyRotation(matrix);
            return true;
        } else if (pointers == 2) {
            matrix.set(lastMatrix);
            applyScale(matrix);
            return true;
        }
        return false;
    }

    public void updateMouse(PointerEvent event) {
        if (event.isButtonDown(MouseEvent.MOUSE_BUTTON_LEFT)) {
            if (event.getPointer() == FIRST_FINGER) {
                firstFingerDown(event);
            } else if (event.getPointer() == SECOND_FINGER) {
                secondFingerDown(event);
            }

        } else if (event.isButtonUp(MouseEvent.MOUSE_BUTTON_LEFT)) {
            if (event.getPointer() == FIRST_FINGER) {
                firstFingerUp();
            } else if (event.getPointer() == SECOND_FINGER) {
                secondFingerUp();
            }
        }
    }

    private void secondFingerDown(PointerEvent event) {
        if (event.getState() == PointerState.DRAGGED && event.getPointer() == SECOND_FINGER) {
            double currentDistance = Vector2.dst(newMx, newMy, event.getX(), event.getY());

            // First Touch
            if (pointers < 2) {
                pointers = 2;
                originalScale = lastMatrix.getScaleX();
                startDistance = Vector2.dst(mx, my, event.getX(), event.getY());
            }

            float diff = (float) (currentDistance - startDistance);

            scale = 1 + diff / scaleSpeed;

            if (scale < 0) {
                int a = Math.round(scale);
                scale = -a + scale;

                if (scale < 0) {
                    scale = -scale;
                }
            }
            if (scale < MIN_SCALE) {
                scale = MIN_SCALE;
                startDistance = currentDistance;
            }
        }
    }

    private void applyRotation(Matrix4 matrix) {
        matrix.rotate(Vector3.Y, yaw);

        // Angle between camera position (10,10) and origin (0,0)
        float cameraAngle = 180 + 45;
        Matrix4 alt = new Matrix4().rotate(Vector3.Y, cameraAngle - (yaw + lastYaw));
        Vector3 result = new Vector3(Vector3.X).rot(alt);
        matrix.rotate(result, pitch);
    }

    private void applyScale(Matrix4 matrix) {
        matrix.scale(scale, scale, scale);
    }

    private void firstFingerDown(PointerEvent event) {
        if (pointers == 0) {
            pointers = 1;
            mx = event.getX();
            my = event.getY();
        }

        if (pointers == 1) {
            yaw = (event.getX() - mx) / rotationSpeed;
            yaw %= 360;

            pitch = (my - event.getY()) / rotationSpeed;
            pitch %= 360;
        } else {
            newMx = event.getX();
            newMy = event.getY();
        }
    }

    private void firstFingerUp() {
        // Update Last Matrix
        applyRotation(lastMatrix);

        lastYaw += yaw;
        pointers = 0;
    }

    private void secondFingerUp() {
        // Release all pointers to avoid miss rotation
        //pointers = 1;
        pointers = 0;
        startDistance = 0;
        minDistance = UNDEFINED;

        applyScale(lastMatrix);
    }

    public Matrix4 getMatrix() {
        return matrix;
    }

    public float getScale() {
        return scale;
    }
}
