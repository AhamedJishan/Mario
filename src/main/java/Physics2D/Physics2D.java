package Physics2D;

import Engine.GameObject;
import Engine.Transform;
import Physics2D.Components.Box2DCollider;
import Physics2D.Components.Circle2DCollider;
import Physics2D.Components.Rigidbody2D;
import Physics2D.Enums.BodyType;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.joml.Math;
import org.joml.Vector2f;

public class Physics2D
{
    private Vec2 gravity = new Vec2(0, -10.0f);
    private World world = new World(gravity);

    private float physicsTime = 0.0f;
    private float physicsTimeStep = 1.0f / 60.0f;
    private int velocityIterations = 8;
    private int positionIterations = 3;

    public void Add(GameObject gameObject)
    {
        Rigidbody2D rb = gameObject.GetComponent(Rigidbody2D.class);
        if (rb != null && rb.GetRawBody() == null)
        {
            Transform transform = gameObject.transform;

            BodyDef bodyDef = new BodyDef();
            bodyDef.angle = Math.toRadians(transform.rotation);
            bodyDef.position.set(transform.position.x, transform.position.y);
            bodyDef.angularDamping = rb.GetAngularDamping();
            bodyDef.linearDamping = rb.GetLinearDamping();
            bodyDef.fixedRotation = rb.IsFixedRotation();
            bodyDef.bullet = rb.IsContinousCollision();

            switch (rb.GetBodyType())
            {
                case BodyType.Kinematic: bodyDef.type = org.jbox2d.dynamics.BodyType.KINEMATIC; break;
                case BodyType.Dynamic: bodyDef.type = org.jbox2d.dynamics.BodyType.DYNAMIC; break;
                case BodyType.Static: bodyDef.type = org.jbox2d.dynamics.BodyType.STATIC; break;
            }

            PolygonShape shape = new PolygonShape();
            Circle2DCollider circleCollider;
            Box2DCollider boxCollider;

            if ((circleCollider = gameObject.GetComponent(Circle2DCollider.class)) != null)
            {
                shape.setRadius(circleCollider.GetRadius());
            }
            else if ((boxCollider = gameObject.GetComponent(Box2DCollider.class)) != null)
            {
                Vector2f halfSize = new Vector2f(boxCollider.GetHalfSize().mul(0.5f));
                Vector2f offset = new Vector2f(boxCollider.GetOffset());
                Vector2f origin = new Vector2f(boxCollider.GetOrigin());
                shape.setAsBox(halfSize.x, halfSize.y, new Vec2(origin.x, origin.y), 0);

                Vec2 pos = bodyDef.position;
                float posX = pos.x + offset.x;
                float posY = pos.y + offset.y;
                bodyDef.position.set(posX, posY);
            }

            Body body = this.world.createBody(bodyDef);
            rb.SetRawBody(body);
            body.createFixture(shape, rb.GetMass());
        }
    }

    public void Update(float dt)
    {
        physicsTime += dt;
        if (physicsTime >= 0.0f)
        {
            physicsTime -= physicsTimeStep;
            world.step(physicsTimeStep, velocityIterations, positionIterations);
        }
    }
}
