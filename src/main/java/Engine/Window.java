package Engine;

import Renderer.DebugDraw;
import Scenes.LevelEditor;
import Scenes.LevelScene;
import Scenes.Scene;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.awt.event.KeyEvent;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window
{
    private int width, height;
    private String title;
    private long glfwWindow;

    private ImGuiLayer guiLayer;

    private static Window window = null;

    private static Scene currentScene = null;

    private Window()
    {
        this.width = 1920;
        this.height = 1080;
        this.title = "Mario";
    }

    public static void ChangeScene(int newScene)
    {
        switch (newScene)
        {
            case 0:
                currentScene = new LevelEditor();
                break;
            case 1:
                currentScene = new LevelScene();
                break;
            default:
                assert false: "Unknown scene '" + newScene + "'";
                break;
        }
        currentScene.Load();
        currentScene.Init();
        currentScene.Start();
    }

    public static Window Get()
    {
        if (Window.window == null)
        {
            Window.window = new Window();
        }
        return Window.window;
    }

    public static Scene GetScene()
    {
        return Get().currentScene;
    }

    public void Run()
    {
        System.out.println("Hellow LWJGL " + Version.getVersion() + "!");

        Init();
        Loop();

        guiLayer.Dispose();

        // Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
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

        // Setting up input callbacks
        glfwSetCursorPosCallback(glfwWindow, MouseListener::MousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::MouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::MouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::KeyCallback);
        glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight)->{
            Window.SetWidth(newWidth);
            Window.SetHeight(newHeight);
        });

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

        // Enable alpha blending
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Initialising ImGui Layer
        this.guiLayer = new ImGuiLayer();
        this.guiLayer.InitImGUI(glfwWindow);

        Window.ChangeScene(0);
    }

    private void Loop()
    {
        float beginTime = (float) glfwGetTime();
        float endTime;
        float dt = -1.0f;

        while(!glfwWindowShouldClose(glfwWindow))
        {
            // Poll events
            glfwPollEvents();

            DebugDraw.BeginFrame();

            glClearColor(1, 1, 1, 1);
            glClear(GL_COLOR_BUFFER_BIT);

            // Updating the scene
            if (dt >= 0)
            {
                DebugDraw.Draw();
                currentScene.Update(dt);
            }

            // Testing Scene change
            if (KeyListener.IsKeyPressed(KeyEvent.VK_1))
                Window.ChangeScene(0);
            if (KeyListener.IsKeyPressed(KeyEvent.VK_2))
                Window.ChangeScene(1);

            // Update GUI
            this.guiLayer.Update(dt, currentScene);

            glfwSwapBuffers(glfwWindow);

            // deltaTime calculation
            endTime = (float) glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
        currentScene.SaveExit();
    }

    public static int GetWidth()
    {
        return Get().width;
    }

    public static void SetWidth(int newWidth)
    {
        Get().width = newWidth;
    }

    public static int GetHeight()
    {
        return Get().height;
    }

    public static void SetHeight(int newHeight)
    {
        Get().height = newHeight;
    }
}
