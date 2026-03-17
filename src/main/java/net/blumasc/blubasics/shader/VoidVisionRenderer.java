package net.blumasc.blubasics.shader;

import com.google.gson.JsonSyntaxException;
import net.blumasc.blubasics.BluBasicsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;

// PostProcessor manager
public class VoidVisionRenderer {
    private static PostChain chain = null;

    public static void load() {
        dispose();
        Minecraft mc = Minecraft.getInstance();
        try {
            chain = new PostChain(
                mc.getTextureManager(),
                mc.getResourceManager(),
                mc.getMainRenderTarget(),
                ResourceLocation.fromNamespaceAndPath(BluBasicsMod.MODID, "shaders/post/shadow_post.json")
            );
            chain.resize(mc.getWindow().getWidth(), mc.getWindow().getHeight());
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void apply(float partialTick) {
        if (chain == null) return;
        Minecraft mc = Minecraft.getInstance();
        // Resize every frame to match window — cheap enough to do each tick
        chain.resize(mc.getWindow().getWidth(), mc.getWindow().getHeight());
        chain.process(partialTick);
    }

    public static void dispose() {
        if (chain != null) { chain.close(); chain = null; }
    }
}