package Components;

import Editor.PropertiesWindow;
import Engine.GameObject;
import Engine.Prefabs;
import Engine.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class TranslateGizmo extends Component
{
    private Vector4f xAxisColor = new Vector4f(1, 0, 0, 1);
    private Vector4f xAxisColorHover = new Vector4f();
    private Vector4f yAxisColor = new Vector4f(0, 1, 0, 1);
    private Vector4f yAxisColorHover = new Vector4f();

    private GameObject xAxisObject;
    private GameObject yAxisObject;
    private SpriteRenderer xAxisSprite;
    private SpriteRenderer yAxisSprite;
    private GameObject activeGameObject = null;

    private Vector2f xAxisOffset = new Vector2f(64, -5);
    private Vector2f yAxisOffset = new Vector2f(16, 61);

    private PropertiesWindow propertiesWindow;

    public TranslateGizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow)
    {
        xAxisObject = Prefabs.GenerateSpriteObject(arrowSprite, 16, 48);
        yAxisObject = Prefabs.GenerateSpriteObject(arrowSprite, 16, 48);
        xAxisSprite = xAxisObject.GetComponent(SpriteRenderer.class);
        yAxisSprite = yAxisObject.GetComponent(SpriteRenderer.class);

        this.propertiesWindow = propertiesWindow;

        Window.GetScene().AddGameObjectToScene(xAxisObject);
        Window.GetScene().AddGameObjectToScene(yAxisObject);
    }

    @Override
    public void Start()
    {
        xAxisObject.transform.rotation = 90.0f;
        yAxisObject.transform.rotation = 180.0f;
        xAxisObject.SetNoSerialize();
        yAxisObject.SetNoSerialize();
    }

    @Override
    public void Update(float dt)
    {
        if (activeGameObject != null)
        {
            xAxisObject.transform.position.set(activeGameObject.transform.position);
            yAxisObject.transform.position.set(activeGameObject.transform.position);
            xAxisObject.transform.position.add(xAxisOffset);
            yAxisObject.transform.position.add(yAxisOffset);
        }

        this.activeGameObject = propertiesWindow.GetActiveGameObject();
        if (activeGameObject != null)
            SetActive();
        else
            SetInactive();
    }

    private void SetActive()
    {
        xAxisSprite.SetColor(xAxisColor);
        yAxisSprite.SetColor(yAxisColor);
    }

    private void SetInactive()
    {
        xAxisSprite.SetColor(new Vector4f());
        yAxisSprite.SetColor(new Vector4f());
    }
}
