package Engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener
{
    private static MouseListener instance;
    private double scrollX, scrollY;
    private double xPos, yPos, lastX, lastY, worldX, worldY, lastWorldX, lastWorldY;
    private boolean mouseButtonPressed[] = new boolean[3];
    private boolean isDragging;
    private int mouseButtonsDown = 0;

    private Vector2f gameViewportPos = new Vector2f();
    private Vector2f gameViewportSize = new Vector2f();

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
        if (Get().mouseButtonsDown > 0)
            Get().isDragging = true;

        Get().lastX = Get().xPos;
        Get().lastY = Get().yPos;
        Get().lastWorldX = Get().worldX;
        Get().lastWorldY = Get().worldY;
        Get().xPos = xpos;
        Get().yPos = ypos;
        CalcOrthoX();
        CalcOrthoY();
    }

    public static void MouseButtonCallback(long window, int button, int action, int mods)
    {
        if (action == GLFW_PRESS)
        {
            if (button < Get().mouseButtonPressed.length)
            {
                Get().mouseButtonPressed[button] = true;
                Get().mouseButtonsDown++;
            }
        }
        else if (action == GLFW_RELEASE)
        {
            if (button < Get().mouseButtonPressed.length)
            {
                Get().mouseButtonPressed[button] = false;
                Get().isDragging = false;
                Get().mouseButtonsDown--;
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
        Get().lastWorldX = Get().worldX;
        Get().lastWorldY = Get().worldY;
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

    public static float GetWorldDx()
    {
        return (float)(Get().worldX - Get().lastWorldX);
    }
    public static float GetWorldDy()
    {
        return (float)(Get().worldY - Get().lastWorldY);
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
            return Get().mouseButtonPressed[button];
        else
            return false;
    }

    public static boolean IsDragging()
    {
        return Get().isDragging;
    }

    public static float GetViewportX()
    {
        float currentX = GetX() - Get().gameViewportPos.x;
        currentX = (currentX / Get().gameViewportSize.x) * 1920.0f;

        return currentX;
    }

    public static float GetViewportY()
    {
        float currentY = GetY() - Get().gameViewportPos.y;
        currentY = 1080.0f - ((currentY / Get().gameViewportSize.y) * 1080.0f);

        return currentY;
    }

    private static void CalcOrthoX()
    {
        float currentX = GetX() - Get().gameViewportPos.x;
        currentX = (currentX / Get().gameViewportSize.x) * 2.0f - 1.0f;
        Vector4f tmp = new Vector4f(currentX, 0, 0, 1);

        Camera camera = Window.GetScene().GetCamera();
        Matrix4f viewProjectionMat = new Matrix4f();
        camera.GetInverseView().mul(camera.GetInverseProjection(), viewProjectionMat);
        tmp.mul(viewProjectionMat);

        Get().worldX = tmp.x;
    }

    public static float GetOrthoX()
    {
        return (float)Get().worldX;
    }

    private static void CalcOrthoY()
    {
        float currentY = GetY() - Get().gameViewportPos.y;
        currentY = -((currentY / Get().gameViewportSize.y) * 2.0f - 1.0f);
        Vector4f tmp = new Vector4f(0, currentY, 0, 1);

        Camera camera = Window.GetScene().GetCamera();
        Matrix4f viewProjectionMat = new Matrix4f();
        camera.GetInverseView().mul(camera.GetInverseProjection(), viewProjectionMat);
        tmp.mul(viewProjectionMat);

        Get().worldY = tmp.y;

    }

    public static float GetOrthoY()
    {
        return (float)Get().worldY;
    }

    public static void SetgameViewportPos(Vector2f gameViewportPos)
    {
        Get().gameViewportPos = gameViewportPos;
    }

    public static void SetgameViewportSize(Vector2f gameViewportSize)
    {
        Get().gameViewportSize = gameViewportSize;
    }
}
