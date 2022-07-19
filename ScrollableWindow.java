package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.noosa.ui.Component;

// taken from https://github.com/Zrp200/ScrollOfDebughttps://github.com/Zrp200/ScrollOfDebug
public class ScrollableWindow extends Window {
	private static final int WIDTH_MIN = 120, WIDTH_MAX = 220;

	public ScrollableWindow(String message) {
		int width = WIDTH_MIN;

		RenderedTextBlock text = PixelScene.renderTextBlock(6);
		text.text(message, width);

		while (PixelScene.landscape()
			   && text.bottom() > (PixelScene.MIN_HEIGHT_L - 10)
			   && width < WIDTH_MAX) {
			text.maxWidth(width += 20);
		}

		int height = (int)text.bottom();
		int maxHeight = (int)(PixelScene.uiCamera.height * 0.9);
		boolean needScrollPane = height > maxHeight;

		if (needScrollPane) height = maxHeight;
		resize((int)text.width(), height);

		if (needScrollPane) {
			Component wrapper = new Component();
			wrapper.setSize(text.width(), text.height());
			ScrollPane sp = new ScrollPane(wrapper);
			add(sp);
			wrapper.add(text);
			text.setPos(0, 0);
			sp.setSize(wrapper.width(), height);
		} else {
			add(text);
		}
	}
}
