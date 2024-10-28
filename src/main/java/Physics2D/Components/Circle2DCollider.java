package Physics2D.Components;

import Components.Component;

public class Circle2DCollider extends Component
{
    private float radius = 1.0f;

    public float GetRadius() {
        return radius;
    }

    public void SetRadius(float radius) {
        this.radius = radius;
    }
}
