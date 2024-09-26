package Engine;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window
{
    private int width, height;
    private String title;
    private long glfwWindow;

    private static Window window = null;

    private Window()
    {
        this.width = 1920;
        this.height = 1080;
        this.title = "Mario";
    }

    public static Window Get()
    {
        if (Window.window == null)
        {
            Window.window = new Window();
        }
        return Window.window;
    }

    public void Run()
    {
        System.out.println("Hellow LWJGL " + Version.getVersion() + "!");

        Init();
        Loop();
    }

    private void Init()
    {
        // Setup an Error Callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit())
            throw new IllegalStateException("Unable to Initialize GLFW!");

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create the Window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL)
            throw new IllegalStateException("Failed to create the Window!");

        // Make the Opengl Context current
        glfwMakeContextCurrent(glfwWindow);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
    }

    private void Loop()
    {
        while(!glfwWindowShouldClose(glfwWindow))
        {
            // Poll events
            glfwPollEvents();

            glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            glfwSwapBuffers(glfwWindow);
        }
    }
}