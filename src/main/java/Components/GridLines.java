package Components;

import Engine.Camera;
import Engine.Window;
import Renderer.DebugDraw;
import org.joml.Vector2f;
import org.joml.Vector3f;
import util.Settings;

public class GridLines extends Component
{
    @Override
    public void Update(float dt)
    {
        Camera camera = Window.GetScene().GetCamera();

        Vector2f cameraPos = camera.position;
        Vector2f projectionSize = camera.GetProjectionSize();

        // Convert the camera position into a multiple of grid_width and grid_height
        int firstX = ((int)(cameraPos.x / Settings.GRID_WIDTH) - 1) * Settings.GRID_WIDTH;
        int firstY = ((int)(cameraPos.y / Settings.GRID_HEIGHT) - 1) * Settings.GRID_HEIGHT;

        // Getting the total number of horizontal and vertical lines
        int numVertLines = (int)((projectionSize.x * camera.GetZoom()) / Settings.GRID_WIDTH) + 2;
        int numHorLines = (int)((projectionSize.y * camera.GetZoom()) / Settings.GRID_HEIGHT) + 2;

        int width = (int)(projectionSize.x * camera.GetZoom()) + Settings.GRID_WIDTH * 2;
        int height = (int)(projectionSize.y * camera.GetZoom()) + Settings.GRID_HEIGHT * 2;

        int maxLines = Math.max(numHorLines, numVertLines);
        Vector3f color = new Vector3f(0.1f, 0.1f, 0.1f);
        for (int i = 0; i < maxLines; i++)
        {
            int x = firstX + (Settings.GRID_WIDTH * i);
            int y = firstY + (Settings.GRID_HEIGHT * i);

            if (i < numVertLines)
                DebugDraw.AddLine2D(new Vector2f(x, firstY), new Vector2f(x, firstY + height), color);

            if (i < numHorLines)
                DebugDraw.AddLine2D(new Vector2f(firstX, y), new Vector2f(firstX + width, y), color);
        }

    }
}
