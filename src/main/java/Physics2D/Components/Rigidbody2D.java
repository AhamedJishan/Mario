package Physics2D.Components;

import Components.Component;
import Physics2D.Enums.BodyType;
import org.jbox2d.dynamics.Body;
import org.joml.Vector2f;

public class Rigidbody2D extends Component
{
    private Vector2f velocity = new Vector2f();
    private float angularDamping = 0.8f;
    private float linearDamping = 0.9f;
    private float mass = 0.0f;
    private BodyType bodyType = BodyType.Dynamic;

    private boolean fixedRotation = false;
    private boolean continousCollision = true;

    private transient Body rawBody = null;

    @Override
    public void Update(float dt)
    {
        if (rawBody != null)
        {
            this.gameObject.transform.position.set(rawBody.getPosition().x, rawBody.getPosition().y);
            this.gameObject.transform.rotation = (float)Math.toDegrees(rawBody.getAngle());
        }
    }

    public Vector2f GetVelocity() {
        return velocity;
    }

    public void SetVelocity(Vector2f velocity) {
        this.velocity = velocity;
    }

    public float GetAngularDamping() {
        return angularDamping;
    }

    public void SetAngularDamping(float angularDamping) {
        this.angularDamping = angularDamping;
    }

    public float GetLinearDamping() {
        return linearDamping;
    }

    public void SetLinearDamping(float linearDamping) {
        this.linearDamping = linearDamping;
    }

    public float GetMass() {
        return mass;
    }

    public void SetMass(float mass) {
        this.mass = mass;
    }

    public BodyType GetBodyType() {
        return bodyType;
    }

    public void SetBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    public boolean IsFixedRotation() {
        return fixedRotation;
    }

    public void SetFixedRotation(boolean fixedRotation) {
        this.fixedRotation = fixedRotation;
    }

    public boolean IsContinousCollision() {
        return continousCollision;
    }

    public void SetContinousCollision(boolean continousCollision) {
        this.continousCollision = continousCollision;
    }

    public Body GetRawBody() {
        return rawBody;
    }

    public void SetRawBody(Body rawBody) {
        this.rawBody = rawBody;
    }
}
