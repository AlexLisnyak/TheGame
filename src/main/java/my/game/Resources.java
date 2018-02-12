package my.game;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Resources {
	Image imgMenRunning;
	Image imgMenStopped;
	Image imgMenJumping;
	Image imgsBarriers;
	Image imgSky;
	Image imgSand;
	Image imgCollide;

	ImageView men;
	ImageView sky1;
	ImageView sky2;
	ImageView sand1;
	ImageView sand2;
	ImageView barriers;

	public void loadResources() {
		try {
			imgMenRunning = new Image("animationMen.gif");
			imgMenStopped = new Image("men(1).png");
			imgsBarriers = new Image("barrier(1).png");

			imgSky = new Image("sky.png");
			imgSand = new Image("sand.png");
			imgMenJumping = new Image("menJump.png");
			imgCollide = new Image("menCollide.png");
			attachDefaultImages();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void attachDefaultImages() {
		try {
			men = new ImageView(imgMenStopped);
			sky1 = new ImageView(imgSky);
			sky2 = new ImageView(imgSky);
			sand1 = new ImageView(imgSand);
			sand2 = new ImageView(imgSand);
			barriers = new ImageView(imgsBarriers);

			sand1.setTranslateY(450);
			sand2.setTranslateY(450);

			men.setViewport(new Rectangle2D(0, 0, 180, 315));
			men.setFitHeight(150);
			men.setFitWidth(100);
			men.setSmooth(true);
			men.setCache(true);
			men.setTranslateY(400);
			men.setTranslateX(50);
			


		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
