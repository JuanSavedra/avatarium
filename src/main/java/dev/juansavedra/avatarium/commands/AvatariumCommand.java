package dev.juansavedra.avatarium.commands;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

/**
 * /avatarium — will open the character customization menu. For now it only confirms the plugin is loaded.
 */
public class AvatariumCommand extends AbstractCommand {

    public AvatariumCommand() {
        super("avatarium", "Opens the Avatarium character customization menu");
    }

    @Nullable
    @Override
    protected CompletableFuture<Void> execute(@Nonnull CommandContext context) {
        context.sendMessage(Message.raw("Avatarium is loaded."));
        return CompletableFuture.completedFuture(null);
    }
}
