package Engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera
{
    private Matrix4f projectionMatrix, viewMatrix, inverseProjection, inverseView;
    public Vector2f position;
    private float projectionWidth = 6;
    private float projectionHeight = 3;
    private Vector2f projectionSize = new Vector2f(projectionWidth, projectionHeight);

    private float zoom = 1.0f;

    public Camera(Vector2f position)
    {
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.inverseProjection = new Matrix4f();
        this.inverseView = new Matrix4f();
        AdjustProjection();
    }

    public void AdjustProjection()
    {
        this.projectionMatrix.identity();
        this.projectionMatrix.ortho(0.0f, projectionSize.x * zoom, 0.0f, projectionSize.y * zoom, 0.0f, 100.0f);
        projectionMatrix.invert(inverseProjection);
    }

    public Matrix4f GetViewMatrix()
    {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMatrix.identity();
        this.viewMatrix.lookAt(new Vector3f(position.x, position.y, 0.0f), cameraFront.add(position.x, position.y, 0.0f),cameraUp);
        this.viewMatrix.invert(inverseView);
        return this.viewMatrix;
    }

    public Matrix4f GetProjectionMatrix()
    {
        return this.projectionMatrix;
    }

    public Matrix4f GetInverseProjection()
    {
        return inverseProjection;
    }

    public Matrix4f GetInverseView()
    {
        return inverseView;
    }

    public Vector2f GetProjectionSize()
    {
        return projectionSize;
    }

    public float GetZoom() {
        return zoom;
    }

    public void SetZoom(float value) {
        this.zoom = value;
    }

    public void AddZoom(float value) {
        this.zoom += value;
    }
}
