package dev.juansavedra.avatarium;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import dev.juansavedra.avatarium.commands.AvatariumCommand;

import javax.annotation.Nonnull;

/**
 * Entry point. Only wires services, registries and commands; gameplay logic lives in its own packages.
 */
public class AvatariumPlugin extends JavaPlugin {

    public AvatariumPlugin(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        this.getCommandRegistry().registerCommand(new AvatariumCommand());
    }
}
