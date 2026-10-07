package com.universalxp.vibrantgrass;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class VibrantGrassClient implements ClientModInitializer {
    public static final String MOD_ID = "vibrantgrass";
    private static final String PACK_PATH = "vibrant_grass";

    private static KeyBinding toggleKey;

    @Override
    public void onInitializeClient() {
        // 1) Pack ko "built-in" resource pack ki tarah register karo.
        //    Ye Options > Resource Packs me bhi dikhega (wahan se bhi on/off ho sakta hai).
        ModContainer container = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow();
        ResourceManagerHelper.registerBuiltinResourcePack(
                Identifier.of(MOD_ID, PACK_PATH),
                container,
                Text.literal("Vibrant Grass"),
                ResourcePackActivationType.DEFAULT_ENABLED
        );

        // 2) Keybind toggle (default: G)
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.vibrantgrass.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                KeyBinding.Category.create(Identifier.of(MOD_ID, "main"))
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                String msg = apply(client, null);
                if (client.player != null && msg != null) {
                    client.player.sendMessage(Text.literal(msg), true);
                }
            }
        });

        // 3) Chat command: /vibrantgrass [on|off|toggle]  (mobile ke liye easy)
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                dispatcher.register(ClientCommandManager.literal("vibrantgrass")
                        .executes(ctx -> {
                            ctx.getSource().sendFeedback(Text.literal(apply(MinecraftClient.getInstance(), null)));
                            return 1;
                        })
                        .then(ClientCommandManager.literal("on").executes(ctx -> {
                            ctx.getSource().sendFeedback(Text.literal(apply(MinecraftClient.getInstance(), true)));
                            return 1;
                        }))
                        .then(ClientCommandManager.literal("off").executes(ctx -> {
                            ctx.getSource().sendFeedback(Text.literal(apply(MinecraftClient.getInstance(), false)));
                            return 1;
                        }))
                        .then(ClientCommandManager.literal("toggle").executes(ctx -> {
                            ctx.getSource().sendFeedback(Text.literal(apply(MinecraftClient.getInstance(), null)));
                            return 1;
                        }))
                ));
    }

    /**
     * @param want true = on, false = off, null = toggle
     * @return message to show the player
     */
    private static String apply(MinecraftClient client, Boolean want) {
        ResourcePackManager manager = client.getResourcePackManager();

        ResourcePackProfile profile = null;
        for (ResourcePackProfile p : manager.getProfiles()) {
            String id = p.getId();
            if (id.contains(PACK_PATH) || id.contains(MOD_ID)) {
                profile = p;
                break;
            }
        }
        if (profile == null) {
            return "Vibrant Grass pack not found!";
        }

        String packId = profile.getId();
        List<String> enabled = new ArrayList<>(manager.getEnabledIds());
        boolean isOn = enabled.contains(packId);
        boolean wantOn = (want == null) ? !isOn : want;

        if (wantOn == isOn) {
            return "Vibrant Grass is already " + (isOn ? "ON" : "OFF");
        }

        if (wantOn) {
            enabled.add(packId); // end of list = highest priority
        } else {
            enabled.remove(packId);
        }

        manager.setEnabledProfiles(enabled);
        client.options.refreshResourcePacks(manager); // saves options + reloads resources
        return "Vibrant Grass: " + (wantOn ? "ON" : "OFF");
    }
}
