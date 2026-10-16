package admsgenter.simple_crash_menu;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class 文本组件 extends Widget {
    private static final FontRenderer 字体 = Minecraft.getInstance().font;
    private final ITextComponent 下划线;
    private final Runnable 执行;

    public 文本组件(ITextComponent 标题, Runnable 执行) {
        super(0, 0, 字体.width(标题), 字体.lineHeight, 标题);
        this.执行 = 执行;
        下划线 = (active = 执行 != null)? 标题.copy().withStyle(TextFormatting.UNDERLINE):标题;
    }

    @Override
    public void onClick(double 鼠标x, double 鼠标y) { if(执行 != null) 执行.run(); }

    @Override
    public void renderButton(MatrixStack 偏移, int 鼠标x, int 鼠标y, float 刻) { drawString(偏移, 字体, isHovered? 下划线:getMessage(), x, y, -1); }
}