package my.game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Barriers {
	enum Type {
		Water, Fence, Crocodile,

	}

	public Image imgs[];

	public Barriers(Image[] imgs) {
		this.imgs = imgs;
	}

	public ImageView returnBarrier(Type blockType) {
		switch (blockType) {
		case Water:
			return new ImageView(imgs[0]);
		case Fence:
			return new ImageView(imgs[1]);
		default:
			return null;
		}
	}

	public Image returnBarrier(int type) {
		if (type < imgs.length) {
			return imgs[type];
		} else {
			return null;
		}

	}

}
