package Components;

import Engine.Component;

public class SpriteRenderer extends Component
{
    private boolean firstTime = true;

    @Override
    public void Start()
    {
        System.out.println("Starting SpriteRenderer Component!");
    }

    @Override
    public void Update() {
        if (firstTime)
        {
            System.out.println("Updating SpriteRenderer...");
            firstTime = false;
        }
    }
}
