package Engine;

public class LevelEditor extends Scene
{
    public LevelEditor()
    {
        System.out.println("Inside LevelEditorScene!");
    }

    @Override
    public void Update(float dt)
    {
        System.out.println("FPS: " + (1.0f/dt));
    }
}
