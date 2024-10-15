package Components;

import Engine.Transform;
import Renderer.Texture;
import imgui.ImGui;
import imgui.flag.ImGuiColorEditFlags;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component
{
    private Vector4f color = new Vector4f(1, 1, 1, 1);
    private Sprite sprite = new Sprite();

    private transient Transform lastTransform;
    private transient boolean isDirty = true;

//    public SpriteRenderer(Vector4f color) {
//        this.color = color;
//        this.sprite = new Sprite(null);
//        this.isDirty = true;
//    }
//
//    public SpriteRenderer(Sprite sprite)
//    {
//        this.sprite = sprite;
//        this.color = new Vector4f(1, 1, 1, 1);
//        this.isDirty = true;
//    }

    @Override
    public void Start()
    {
        this.lastTransform = gameObject.transform.Copy();
    }

    @Override
    public void Update(float dt)
    {
        if (!this.lastTransform.equals(this.gameObject.transform))
        {
            this.gameObject.transform.Copy(this.lastTransform);
            isDirty = true;
        }

    }

    @Override
    public void GUI()
    {
        float[] imColor = {color.x, color.y, color.z, color.w};
        if (ImGui.colorPicker4("Color Picker: ", imColor, ImGuiColorEditFlags.AlphaBar))
        {
            this.color.set(imColor[0], imColor[1], imColor[2], imColor[3]);
            this.isDirty = true;
        }
    }

    public Vector4f GetColor()
    {
        return this.color;
    }

    public void SetColor(Vector4f color)
    {
        if (!this.color.equals(color))
        {
            this.color = color;
            this.isDirty = true;
        }
    }

    public Texture GetTexture()
    {
        return sprite.GetTexture();
    }

    public Vector2f[] GetTexCoords()
    {
        return sprite.GetTexCoords();
    }

    public void SetSprite (Sprite sprite)
    {
        this.sprite = sprite;
        isDirty = true;
    }

    public boolean IsDirty()
    {
        return this.isDirty;
    }

    public void SetClean()
    {
        this.isDirty = false;
    }

}