package Editor;

import Engine.Window;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;

public class GameViewWindow
{
    public static void GUI()
    {
        ImGui.begin("Game Viewport", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse);

        ImVec2 windowSize = GetLargestSizeforViewport();
        ImVec2 windowPos = GetCenteredPositionForWindow(windowSize);

        ImGui.setCursorPos(windowPos.x, windowPos.y);
        int textureID = Window.GetFramebuffer().GetTextureId();
        ImGui.image(textureID, windowSize.x, windowSize.y, 0, 1, 1, 0);

        ImGui.end();
    }

    private static ImVec2 GetLargestSizeforViewport()
    {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float aspectWidth = windowSize.x;
        float aspectHeight = aspectWidth / Window.GetTargetAspectRatio();
        if (aspectHeight > windowSize.y)
        {
            aspectHeight = windowSize.y;;
            aspectWidth = aspectHeight * Window.GetTargetAspectRatio();
        }

        return new ImVec2(aspectWidth, aspectHeight);
    }

    private static ImVec2 GetCenteredPositionForWindow(ImVec2 aspectSize)
    {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float viewPortPosX = (windowSize.x / 2.0f) - (aspectSize.x / 2.0f);
        float viewPortPosY = (windowSize.y / 2.0f) - (aspectSize.y / 2.0f);

        return new ImVec2(viewPortPosX + ImGui.getCursorPosX(), viewPortPosY + ImGui.getCursorPosY());
    }
}
