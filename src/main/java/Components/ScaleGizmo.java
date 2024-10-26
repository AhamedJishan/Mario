package Components;

import Editor.PropertiesWindow;
import Engine.MouseListener;

public class ScaleGizmo extends Gizmo
{
    public ScaleGizmo(Sprite scaleSprite, PropertiesWindow propertiesWindow)
    {
        super(scaleSprite, propertiesWindow);
    }

    @Override
    public void Update(float dt)
    {
        if (activeGameObject != null)
        {
            if (xAxisActive && !yAxisActive)
                activeGameObject.transform.scale.x += MouseListener.GetWorldDx();
            else if (yAxisActive)
                activeGameObject.transform.scale.y += MouseListener.GetWorldDy();
        }

        super.Update(dt);
    }
}
