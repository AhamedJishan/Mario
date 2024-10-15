package Engine;

import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener
{
    private static MouseListener instance;
    private double scrollX, scrollY;
    private double xPos, yPos, lastX, lastY;
    private boolean mouseButtonPressed[] = new boolean[3];
    private boolean isDragging;

    private MouseListener()
    {
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
    }

    public static MouseListener Get()
    {
        if (MouseListener.instance == null)
        {
            MouseListener.instance = new MouseListener();
        }

        return MouseListener.instance;
    }

    // CALLBACKS----------------------------------------------------------------------
    public static void MousePosCallback(long window, double xpos, double ypos)
    {
        Get().lastX = Get().xPos;
        Get().lastY = Get().yPos;
        Get().xPos = xpos;
        Get().yPos = ypos;

        Get().isDragging = Get().mouseButtonPressed[0] || Get().mouseButtonPressed[1] || Get().mouseButtonPressed[2];
    }

    public static void MouseButtonCallback(long window, int button, int action, int mods)
    {
        if (action == GLFW_PRESS)
        {
            if (button < Get().mouseButtonPressed.length)
            {
                Get().mouseButtonPressed[button] = true;
            }
        }
        else if (action == GLFW_RELEASE)
        {
            if (button < Get().mouseButtonPressed.length)
            {
                Get().mouseButtonPressed[button] = false;
                Get().isDragging = false;
            }
        }
    }

    public static void MouseScrollCallback(long window, double xOffset, double yOffset)
    {
        Get().scrollX = xOffset;
        Get().scrollY = yOffset;
    }

    public static void EndFrame()
    {
        Get().scrollX = 0.0;
        Get().scrollY = 0.0;
        Get().lastX = Get().xPos;
        Get().lastY = Get().yPos;
    }

    // GETTERS-----------------------------------------------------------------------
    public static float GetX()
    {
        return (float)Get().xPos;
    }
    public static float GetY() {
        return (float) Get().yPos;
    }
    public static float GetDx()
    {
        return (float)(Get().xPos - Get().lastY);
    }
    public static float GetDy()
    {
        return (float)(Get().yPos - Get().lastY);
    }

    public static float GetScrollX()
    {
        return (float) Get().scrollX;
    }
    public static float GetScrollY()
    {
        return (float) Get().scrollY;
    }

    public static boolean MouseButtonDown(int button)
    {
        if (button < Get().mouseButtonPressed.length)
        {
            return Get().mouseButtonPressed[button];
        }
        else
        {
            return false;
        }
    }

    public static float GetOrthoX()
    {
        float currentX = GetX();
        currentX = (currentX / (float)Window.GetWidth()) * 2.0f - 1.0f;
        Vector4f tmp = new Vector4f(currentX, 0, 0, 1);
        tmp.mul(Window.GetScene().GetCamera().GetInverseProjection()).mul(Window.GetScene().GetCamera().GetInverseView());
        currentX = tmp.x;

        return currentX;
    }

    public static float GetOrthoY()
    {
        float currentY = Window.GetHeight() - GetY();
        currentY = (currentY / (float)Window.GetHeight()) * 2.0f - 1.0f;
        Vector4f tmp = new Vector4f(0, currentY, 0, 1);
        tmp.mul(Window.GetScene().GetCamera().GetInverseProjection()).mul(Window.GetScene().GetCamera().GetInverseView());
        currentY = tmp.y;

        return currentY;
    }
}
