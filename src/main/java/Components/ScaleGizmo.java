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
    public void EditorUpdate(float dt)
    {
        if (activeGameObject != null)
        {
            if (xAxisActive && !yAxisActive)
                activeGameObject.transform.scale.x += MouseListener.GetWorldDx();
            else if (yAxisActive)
                activeGameObject.transform.scale.y += MouseListener.GetWorldDy();
        }

        super.EditorUpdate(dt);
    }
}
