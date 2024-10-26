package Components;

import Editor.PropertiesWindow;
import Engine.MouseListener;

public class TranslateGizmo extends Gizmo
{
    public TranslateGizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow)
    {
        super(arrowSprite, propertiesWindow);
    }

    @Override
    public void Update(float dt)
    {
        if (activeGameObject != null)
        {
            if (xAxisActive && !yAxisActive)
                activeGameObject.transform.position.x += MouseListener.GetWorldDx();
            else if (yAxisActive)
                activeGameObject.transform.position.y += MouseListener.GetWorldDy();
        }

        super.Update(dt);
    }
}
