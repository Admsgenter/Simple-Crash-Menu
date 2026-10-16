package admsgenter.simple_crash_menu;

import com.mojang.blaze3d.matrix.MatrixStack;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.Util;
import net.minecraft.util.text.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.File;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class 崩溃屏幕 extends Screen {
    private static final Minecraft 游戏 = Minecraft.getInstance();
    private static final FontRenderer 字体 = 游戏.font;
    private static final Style 文本 = Style.EMPTY.applyFormat(TextFormatting.GRAY);
    private static final Style 文件 = Style.EMPTY.applyFormat(TextFormatting.GREEN);
    private static final ITextComponent 标题 = new TranslationTextComponent("simple_crash_menu.title");
    private static final ITextComponent 打开报告 = new TranslationTextComponent("simple_crash_menu.crash_report").setStyle(文本);
    private static final ITextComponent 提示信息 = new TranslationTextComponent("simple_crash_menu.message").setStyle(文本);
    private static final ITextComponent 屏幕提供 = new TranslationTextComponent("simple_crash_menu.screen").setStyle(文本);
    private final int 高度;
    private final Widget 退出按钮, 文件按钮;
    private final List<Widget> 组件表 = new ReferenceArrayList<>();
    private int y;

    public 崩溃屏幕(CrashReport 崩溃报告) {
        super(标题);
        int 高度 = 添加(25, new TranslationTextComponent("simple_crash_menu.exception", 崩溃报告.getException().getClass().getName()).setStyle(文本));
        高度 = 添加(高度, new TranslationTextComponent("simple_crash_menu.thread", Thread.currentThread().getName()).setStyle(文本));
        高度 = 添加(高度, 打开报告);
        File 文件路径 = 崩溃报告.getSaveFile();
        ITextComponent 报告链接 = new StringTextComponent(文件路径.getName()).setStyle(文件);
        高度 = 添加(高度, 文件按钮 = new 文本组件(报告链接, () -> Util.getPlatform().openUri(文件路径.toURI())));
        高度 = 添加(高度, 屏幕提供);
        高度 = 添加(高度, 提示信息);
        组件表.add(new Button(0, 高度 += 12, 150, 20, new TranslationTextComponent("gui.toTitle"), 按钮 -> 游戏.setScreen(null)));
        组件表.add(退出按钮 = new Button(0, 高度, 150, 20, new TranslationTextComponent("menu.quit"), 按钮 -> 游戏.stop()));
        this.高度 = 高度+20;
    }

    private int 添加(int 高度, Widget 组件) {
        组件.y = 高度;
        组件表.add(组件);
        return 高度+组件.getHeight()+8;
    }

    private int 添加(int 高度, ITextComponent 组件) { return 添加(高度, new 文本组件(组件, null)); }

    @Override
    protected void init() {
        int x = width-320>>1, 变化 = (height-高度>>1)-y;
        y += 变化;
        for(Widget 组件 : 组件表) {
            组件.x = x;
            组件.y += 变化;
            addButton(组件);
        }
        退出按钮.x = x+170;
        文件按钮.x = width-字体.width(文件按钮.getMessage())>>1;
    }

    @Override
    public void render(MatrixStack 偏移, int 鼠标x, int 鼠标y, float 刻) {
        renderDirtBackground(0);
        drawCenteredString(偏移, 字体, 标题, width>>1, y, -1);
        super.render(偏移, 鼠标x, 鼠标y, 刻);
    }
}