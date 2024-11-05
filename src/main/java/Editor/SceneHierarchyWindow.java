package Editor;

import Engine.GameObject;
import Engine.Window;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;

import java.util.List;

public class SceneHierarchyWindow
{
    private final static String payloadObjectType = "SceneHierarchy";

    public void GUI()
    {
        ImGui.begin("Scene Hierarchy");

        List<GameObject> gameObjects = Window.GetScene().GetGameObjects();
        int index = 0;
        for (GameObject gameObject : gameObjects)
        {
            if (!gameObject.DoSerialization())
                continue;

            boolean treeNodeOpen = DoTreeNode(gameObject, index);

            if (treeNodeOpen)
                ImGui.treePop();
            index++;
        }

        ImGui.end();
    }

    private boolean DoTreeNode(GameObject gameObject, int index)
    {
        ImGui.pushID(index);
        boolean treeNodeOpen = ImGui.treeNodeEx(
                gameObject.name,
                ImGuiTreeNodeFlags.DefaultOpen |
                        ImGuiTreeNodeFlags.FramePadding |
                        ImGuiTreeNodeFlags.OpenOnArrow |
                        ImGuiTreeNodeFlags.SpanAvailWidth, gameObject.name
        );
        ImGui.popID();

        if (ImGui.beginDragDropSource())
        {
            ImGui.setDragDropPayload(payloadObjectType, gameObject);
            ImGui.text(gameObject.name);
            ImGui.endDragDropSource();
        }

        if (ImGui.beginDragDropTarget())
        {
            Object payloadObj = ImGui.acceptDragDropPayload(payloadObjectType);
            if (payloadObj != null && payloadObj.getClass().isAssignableFrom(GameObject.class))
            {
                GameObject playerObject = (GameObject)payloadObj;
                System.out.println("Payload Accepted: " + playerObject.name);
            }
            ImGui.endDragDropTarget();
        }

        return treeNodeOpen;
    }
}
