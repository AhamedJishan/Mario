package Editor;

import Observers.EventSystem;
import Observers.Events.Event;
import Observers.Events.EventType;
import imgui.ImGui;

public class MenuBar
{
    public void GUI()
    {
        ImGui.beginMenuBar();

        if (ImGui.beginMenu("File"))
        {
            if (ImGui.menuItem("Save", "Ctrl+S"))
            {
                EventSystem.Notify(null, new Event(EventType.SaveLevel));
            }
            if (ImGui.menuItem("Load", "Ctrl+O"))
            {
                EventSystem.Notify(null, new Event(EventType.LoadLevel));
            }
            ImGui.endMenu();
        }

        ImGui.endMenuBar();
    }
}
