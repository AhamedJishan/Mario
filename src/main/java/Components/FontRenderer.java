package Components;

import Engine.Component;

public class FontRenderer extends Component
{
    @Override
    public void Start()
    {
        if (gameObject.GetComponent(SpriteRenderer.class) != null) System.out.println("Found Sprite Renderer!");
    }

    @Override
    public void Update() {

    }
}
