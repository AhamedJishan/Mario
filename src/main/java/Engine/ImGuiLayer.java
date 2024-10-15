package Engine;

import Scenes.Scene;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;

public class ImGuiLayer
{
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    private String glslVersion = "#version 330";

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

        currentScene.SceneGUI();
        ImGui.showDemoWindow();

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
}
