package Engine;

import Editor.GameViewWindow;
import Editor.MenuBar;
import Editor.PropertiesWindow;
import Renderer.PickingTexture;
import Scenes.Scene;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.type.ImBoolean;

import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;

public class ImGuiLayer
{
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    private String glslVersion = "#version 330";

    private GameViewWindow gameViewWindow;
    private PropertiesWindow propertiesWindow;
    private MenuBar menuBar;

    public ImGuiLayer(PickingTexture pickingTexture)
    {
        gameViewWindow = new GameViewWindow();
        propertiesWindow = new PropertiesWindow(pickingTexture);
        menuBar = new MenuBar();
    }

    public void InitImGUI(long windowPtr)
    {
        ImGui.createContext();
        imGuiGlfw.init(windowPtr, true);
        imGuiGl3.init(glslVersion);

        final ImGuiIO io = ImGui.getIO();
        io.setIniFilename("imgui.ini"); // We don't want to save .ini file
        io.setConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);
        io.setConfigFlags(ImGuiConfigFlags.DockingEnable);

        io.getFonts().addFontFromFileTTF("assets/fonts/segoeui.ttf", 20);
    }

    public void Dispose()
    {
        imGuiGl3.shutdown();
        imGuiGlfw.shutdown();
        ImGui.destroyContext();
    }

    public void Update(float dt, Scene currentScene)
    {
        BeginFrame();

        SetupDockSpace();
        currentScene.GUI();
        ImGui.showDemoWindow();
        gameViewWindow.GUI();
        propertiesWindow.Update(dt, currentScene);
        propertiesWindow.GUI();
        menuBar.GUI();
        ImGui.end();

        EndFrame();
    }

    private void BeginFrame()
    {
        imGuiGl3.newFrame();
        imGuiGlfw.newFrame();
        ImGui.newFrame();
    }

    private void EndFrame()
    {
        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());
        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupCurrentContext = glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            glfwMakeContextCurrent(backupCurrentContext);
        }
    }

    private void SetupDockSpace()
    {
        int windowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking;
        ImGui.setNextWindowPos(0.0f, 0.0f, ImGuiCond.Always);
        ImGui.setNextWindowSize(Window.GetWidth(), Window.GetHeight());
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 0.0f, 0.0f);
        windowFlags |= ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse
                | ImGuiWindowFlags.NoResize| ImGuiWindowFlags.NoMove
                | ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;

        ImGui.begin("Dockspace Demo", new ImBoolean(true), windowFlags);
        ImGui.popStyleVar(3);

        // Dockspace
        ImGui.dockSpace(ImGui.getID("Dockspace"));
    }

    public PropertiesWindow GetPropertiesWindow()
    {
        return this.propertiesWindow;
    }
}
