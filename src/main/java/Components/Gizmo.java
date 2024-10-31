package Components;

import Editor.PropertiesWindow;
import Engine.*;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;

public class Gizmo extends Component
{
    private Vector4f xAxisColor = new Vector4f(1, 0.3f, 0.3f, 1);
    private Vector4f xAxisColorHover = new Vector4f(1, 0, 0, 1);
    private Vector4f yAxisColor = new Vector4f(0.3f, 1, 0.3f, 1);
    private Vector4f yAxisColorHover = new Vector4f(0, 1, 0, 1);

    private GameObject xAxisObject;
    private GameObject yAxisObject;
    private SpriteRenderer xAxisSprite;
    private SpriteRenderer yAxisSprite;
    protected GameObject activeGameObject = null;
    private PropertiesWindow propertiesWindow;

    private Vector2f xAxisOffset = new Vector2f(24f / 80.0f, -6f / 80.0f);
    private Vector2f yAxisOffset = new Vector2f(-7f / 80.0f, 21f / 80.0f);

    private float gizmoWidth = 16 / 80.0f;
    private float gizmoHeight = 48 / 80.0f;

    protected boolean xAxisActive = false;
    protected boolean yAxisActive = false;
    private boolean using = false;

    public Gizmo(Sprite sprite, PropertiesWindow propertiesWindow)
    {
        xAxisObject = Prefabs.GenerateSpriteObject(sprite, gizmoWidth, gizmoHeight);
        yAxisObject = Prefabs.GenerateSpriteObject(sprite, gizmoWidth, gizmoHeight);
        xAxisSprite = this.xAxisObject.GetComponent(SpriteRenderer.class);
        yAxisSprite = this.yAxisObject.GetComponent(SpriteRenderer.class);
        this.propertiesWindow = propertiesWindow;

        this.yAxisObject.AddComponent(new NonPickable());
        this.xAxisObject.AddComponent(new NonPickable());

        Window.GetScene().AddGameObjectToScene(xAxisObject);
        Window.GetScene().AddGameObjectToScene(yAxisObject);
    }

    @Override
    public void Start()
    {
        xAxisObject.transform.rotation = 90.0f;
        yAxisObject.transform.rotation = 180.0f;
        // Making sure the gizmo is always drawn above all the gameObjects
        xAxisObject.transform.zIndex = 100;
        yAxisObject.transform.zIndex = 100;
        xAxisObject.SetNoSerialize();
        yAxisObject.SetNoSerialize();
    }

    @Override
    public void Update(float dt)
    {
        if(using)
            SetInactive();
    }

    @Override
    public void EditorUpdate(float dt)
    {
        if (!using) return;

        this.activeGameObject = this.propertiesWindow.GetActiveGameObject();
        if (this.activeGameObject != null)
        {
            this.SetActive();

            // TODO: move this into it's correct place
            if (KeyListener.IsKeyPressed(GLFW_KEY_LEFT_SHIFT) && KeyListener.KeyBeginPress(GLFW_KEY_D))
            {
                GameObject newObj = activeGameObject.Copy();
                Window.GetScene().AddGameObjectToScene(newObj);
                newObj.transform.position.add(0.1f, 0.1f);
                this.propertiesWindow.SetActiveGameObject(newObj);
                return;
            }
            else if (KeyListener.IsKeyPressed(GLFW_KEY_DELETE))
            {
                activeGameObject.Destroy();
                SetInactive();
                propertiesWindow.SetActiveGameObject(null);
                return;
            }
        }
        else
        {
            this.SetInactive();
            return;
        }

        boolean xAxisHot = CheckXHoverState();
        boolean yAxisHot = CheckYHoverState();

        if ((xAxisHot || xAxisActive) && MouseListener.IsDragging() && MouseListener.MouseButtonDown(GLFW_MOUSE_BUTTON_LEFT))
        {
            xAxisActive = true;
            yAxisActive = false;
        }
        else if ((yAxisHot || yAxisActive) && MouseListener.IsDragging() && MouseListener.MouseButtonDown(GLFW_MOUSE_BUTTON_LEFT))
        {
            xAxisActive = false;
            yAxisActive = true;
        }
        else
        {
            xAxisActive = false;
            yAxisActive = false;
        }

        if (activeGameObject != null)
        {
            xAxisObject.transform.position.set(activeGameObject.transform.position);
            yAxisObject.transform.position.set(activeGameObject.transform.position);
            xAxisObject.transform.position.add(xAxisOffset);
            yAxisObject.transform.position.add(yAxisOffset);
        }
    }

    private boolean CheckXHoverState()
    {
        Vector2f mousePos = new Vector2f(MouseListener.GetOrthoX(), MouseListener.GetOrthoY());
        if (mousePos.x <= xAxisObject.transform.position.x + (gizmoHeight/2.0f)&&
                mousePos.x >= xAxisObject.transform.position.x - (gizmoHeight/2.0f) &&
                mousePos.y >= xAxisObject.transform.position.y - (gizmoWidth/2.0f) &&
                mousePos.y <= xAxisObject.transform.position.y + (gizmoWidth/2.0f))
        {
            xAxisSprite.SetColor(xAxisColorHover);
            return true;
        }

        xAxisSprite.SetColor(xAxisColor);
        return false;
    }

    private boolean CheckYHoverState()
    {
        Vector2f mousePos = new Vector2f(MouseListener.GetOrthoX(), MouseListener.GetOrthoY());
        if (mousePos.x <= yAxisObject.transform.position.x + (gizmoWidth/2.0f) &&
                mousePos.x >= yAxisObject.transform.position.x - (gizmoWidth/2.0f) &&
                mousePos.y <= yAxisObject.transform.position.y + (gizmoHeight/2.0f)&&
                mousePos.y >= yAxisObject.transform.position.y - (gizmoHeight/2.0f))
        {
            yAxisSprite.SetColor(yAxisColorHover);
            return true;
        }

        yAxisSprite.SetColor(yAxisColor);
        return false;
    }

    private void SetActive()
    {
        xAxisSprite.SetColor(xAxisColor);
        yAxisSprite.SetColor(yAxisColor);
    }

    private void SetInactive()
    {
        this.activeGameObject = null;
        xAxisSprite.SetColor(new Vector4f(0, 0, 0, 0));
        yAxisSprite.SetColor(new Vector4f(0, 0, 0, 0));
    }

    public void SetUsing()
    {
        this.using = true;
        this.SetActive();
    }
    public void SetNotUsing()
    {
        this.using = false;
        this.SetInactive();
    }
}
