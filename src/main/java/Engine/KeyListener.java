package Engine;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener
{
    private static KeyListener instance;
    private boolean keyPressed[] = new boolean[350];

    private KeyListener()
    {

    }

    public static KeyListener Get()
    {
        if (KeyListener.instance == null)
        {
            KeyListener.instance = new KeyListener();
        }
        return KeyListener.instance;
    }

    public static void KeyCallback(long window, int key, int scancode, int action, int mods)
    {
        if (action == GLFW_PRESS)
        {
            Get().keyPressed[key] = true;
        }
        else if (action == GLFW_RELEASE)
        {
            Get().keyPressed[key] = false;
        }
    }

    public static boolean IsKeyPressed (int keyCode)
    {
        return Get().keyPressed[keyCode];
    }
}
