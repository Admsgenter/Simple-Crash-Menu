package admsgenter.simple_crash_menu.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.Main;
import net.minecraft.util.MemoryReserve;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Main.class)
public class MainMixin {
    @Redirect(at = @At(target = "Lnet/minecraft/client/Minecraft;run()V", value = "INVOKE"), method = "run")
    private static void SCM$运行(Minecraft 游戏) {
        while(游戏.isRunning()) {
            PoseStack 偏移 = RenderSystem.getModelViewStack();
            while(!偏移.clear()) 偏移.popPose();
            游戏.run();
        }
    }
}