package com.harium.propan.camera;

import com.badlogic.gdx.math.Matrix4;

public class CameraController {

    protected Matrix4 matrix;

    public CameraController() {
        matrix = new Matrix4();
    }

    public CameraController(Matrix4 matrix) {
        this.matrix = new Matrix4(matrix);
    }
}
