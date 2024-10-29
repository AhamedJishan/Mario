package Physics2D.Components;

public class Circle2DCollider extends Collider
{
    private float radius = 1.0f;

    public float GetRadius() {
        return radius;
    }

    public void SetRadius(float radius) {
        this.radius = radius;
    }
}
