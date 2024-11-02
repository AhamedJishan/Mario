package Editor;

import Engine.GameObject;
import Engine.Window;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;

import java.util.List;

public class SceneHierarchyWindow
{
    public void GUI()
    {
        ImGui.begin("Scene Hierarchy");

        List<GameObject> gameObjects = Window.GetScene().GetGameObjects();
        int index = 0;
        for (GameObject gameObject : gameObjects)
        {
            if (!gameObject.DoSerialization())
                continue;

            ImGui.pushID(index);
            boolean treeNodeOpen = ImGui.treeNodeEx(
                    gameObject.name,
                    ImGuiTreeNodeFlags.DefaultOpen |
                    ImGuiTreeNodeFlags.FramePadding |
                    ImGuiTreeNodeFlags.OpenOnArrow |
                    ImGuiTreeNodeFlags.SpanAvailWidth, gameObject.name
            );
            ImGui.popID();

            if (treeNodeOpen)
                ImGui.treePop();
            index++;
        }

        ImGui.end();
    }
}
