package admsgenter.simple_crash_menu;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class 文本组件 extends AbstractWidget {
    private static final Font 字体 = Minecraft.getInstance().font;

    public 文本组件(Component 标题) {
        super(0, 0, 字体.width(标题), 字体.lineHeight, 标题);
        active = false;
    }

    @Override
    public void renderButton(PoseStack 偏移, int 鼠标x, int 鼠标y, float 刻) { drawString(偏移, 字体, getMessage(), x, y, -1); }

    @Override
    public void updateNarration(NarrationElementOutput 输出) { defaultButtonNarrationText(输出); }
}