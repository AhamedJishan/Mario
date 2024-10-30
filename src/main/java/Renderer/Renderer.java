package Renderer;

import Components.SpriteRenderer;
import Engine.GameObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Renderer
{
    private final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> batches;
    private static Shader currentShader;

    public Renderer()
    {
        this.batches = new ArrayList<>();
    }

    public void Add(GameObject gameObject)
    {
        SpriteRenderer sprite = gameObject.GetComponent(SpriteRenderer.class);
        if (sprite != null)
            AddSpriteToBatch(sprite);
    }

    private void AddSpriteToBatch(SpriteRenderer sprite)
    {
        boolean added = false;
        for (RenderBatch batch : batches)
        {
            if (batch.HasRoom() && batch.ZIndex() == sprite.gameObject.transform.zIndex)
            {
                Texture tex = sprite.GetTexture();
                if (tex == null || (batch.HasTexture(tex) || batch.HasTextureRoom() ))
                {
                    batch.AddSprite(sprite);
                    added = true;
                    break;
                }
            }
        }

        if (!added)
        {
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, sprite.gameObject.transform.zIndex);
            newBatch.Start();
            batches.add(newBatch);
            newBatch.AddSprite(sprite);
            Collections.sort(batches);
        }
    }

    public void DestroyGameObject(GameObject gameObject)
    {
        if (gameObject.GetComponent(SpriteRenderer.class) == null)
            return;

        for (RenderBatch renderBatch : batches)
        {
            if (renderBatch.DestroyIfExists(gameObject))
                return;
        }
    }

    public static void BindShader(Shader shader)
    {
        currentShader = shader;
    }

    public static Shader GetBoundShader()
    {
        return currentShader;
    }

    public void Render()
    {
        currentShader.Use();
        for (RenderBatch batch : batches)
            batch.Render();
        currentShader.Detach();
    }
}
