package admsgenter.simple_crash_menu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class 文本组件 extends Widget {
    private static final FontRenderer 字体 = Minecraft.getInstance().font;
    private final String 下划线;
    private final Runnable 执行;

    public 文本组件(ITextComponent 标题, Runnable 执行) {
        super(0, 0, 字体.width(标题.getString()), 字体.lineHeight, 标题.getColoredString());
        this.执行 = 执行;
        下划线 = (active = 执行 != null)? 标题.copy().setStyle(标题.getStyle().copy().setUnderlined(true)).getColoredString():getMessage();
    }

    @Override
    public void onClick(double 鼠标x, double 鼠标y) { if(执行 != null) 执行.run(); }

    @Override
    public void renderButton(int 鼠标x, int 鼠标y, float 刻) { drawString(字体, isHovered? 下划线:getMessage(), x, y, -1); }
}