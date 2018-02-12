package my.game;

import java.util.ArrayList;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WindowGame extends Application implements EventHandler<ActionEvent> {

	public static void main(String[] args) {
		launch(args);
	}

	Resources resources;
	AnchorPane gamePane;
	boolean runningState;
	boolean jumpingState;
	boolean right = true;
	boolean jumpReleased;
	double delay;
	int enemyLife = 5;
	double decrease;

	boolean editor;
	int typeChange;
	int collisionDelay;
	boolean motionBlurEnabled = true;
	boolean pressed;
	boolean isFalling;
	Line line;
	boolean shift = true;

	QuadCurve jumpCurve;
	TranslateTransition translateAnim;
	PathTransition jumpTransition;
	MotionBlur motionBlur;
	ArrayList<ImageView> list;
	ImageView view;
	Rectangle menRectangle;
	TranslateTransition translateTransition;

	@Override
	public void start(Stage primaryStage) {
		try {
			
			AnchorPane gp = new AnchorPane();

			Image img = new Image("firstImage.jpg");
			ImageView iv = new ImageView(img);

			Button start = new Button("Start");
			Button exit = new Button("Exit");

			start.setVisible(true);
			start.setMinSize(90, 35);
			start.setMaxSize(90, 35);
			start.setPrefSize(90, 35);
			start.setOnAction(rotat -> {
				gameStage(primaryStage);
			});

			exit.setVisible(true);
			exit.setMinSize(90, 35);
			exit.setMaxSize(90, 35);
			exit.setPrefSize(90, 35);

			HBox hb = new HBox(5);
			hb.getChildren().add(start);
			hb.getChildren().add(exit);
			hb.setLayoutX(400);
			hb.setLayoutY(300);

			gp.getChildren().addAll(iv, hb);

			Scene scene = new Scene(gp, 1024, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setTitle("TheGame");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void gameStage(Stage stg) {
		stg.close();
		Stage stageGame = new Stage();
		AnchorPane gamePane = new AnchorPane();
		/*
		 * ImageView iv1.setVisible(true); iv2.setVisible(true); iv1.setLayoutY(500);
		 * iv2.setLayoutY(0);
		 * 
		 * viewMenStop.setVisible(true); viewMenGo.setVisible(true);
		 * 
		 * viewMenStop.setLayoutX(300); viewMenStop.setLayoutY(430);
		 * 
		 * gamePane.getChildren().addAll(iv1, iv2, viewMenStop);
		 */

		line = new Line();
		list = new ArrayList<>();

		gamePane.setOnMouseMoved(e -> {
			if (view != null) {
				view.setX(e.getX());
				view.setY(e.getY() - 25);
				for (int j = 0; j < list.size() - 1; j++) {
					ImageView ivIter = list.get(j);
					Bounds b = ivIter.getBoundsInParent();
					if (b.contains(e.getX() + b.getWidth() / 2, e.getY())) {
						view.setX(ivIter.getX() - b.getWidth() + 1);
						view.setY(ivIter.getY());
						break;
					} else if (b.contains(e.getX() - b.getWidth() / 2, e.getY())) {
						view.setX(ivIter.getX() + b.getWidth() - 1);
						view.setY(ivIter.getY());
						break;
					} else if (b.contains(e.getX(), e.getY() + b.getHeight() / 2)) {
						view.setX(ivIter.getX());
						view.setY(ivIter.getY() - b.getHeight());
						break;
					} else if (b.contains(e.getX(), e.getY() - b.getHeight() / 2)) {
						view.setX(ivIter.getX());
						view.setY(ivIter.getY() + b.getHeight());
						break;
					}
				}
			}
		});

		gamePane.requestFocus();
		gamePane.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.LEFT && pressed == true) {
				pressed = true;
				if ((jumpTransition.getStatus() != Animation.Status.RUNNING)) {
					right = false;
					resources.men.setScaleX(-1.0);
					if (motionBlurEnabled) {
						motionBlur.setAngle(-180);
						motionBlur.setRadius(delay * 0.5);
						resources.sand1.setEffect(motionBlur);
						resources.sand2.setEffect(motionBlur);
					}
					if (jumpTransition.getStatus() == PathTransition.Status.STOPPED
							&& translateAnim.getStatus() != PathTransition.Status.RUNNING
							&& translateTransition.getStatus() != PathTransition.Status.RUNNING) {
						runningState = true;
						translateAnim.setByX(-1000);
						translateAnim.play();
					}
				}
			} else if (e.getCode() == KeyCode.RIGHT && pressed == true) {
				pressed = true;
				if ((jumpTransition.getStatus() != Animation.Status.RUNNING)) {
					resources.men.setScaleX(+1.0);
					right = true;

					if (motionBlurEnabled) {
						motionBlur.setAngle(0);
						motionBlur.setRadius(delay * 0.5);
						resources.sand1.setEffect(motionBlur);
						resources.sand2.setEffect(motionBlur);
					}
					if (jumpTransition.getStatus() == PathTransition.Status.STOPPED
							&& translateAnim.getStatus() != PathTransition.Status.RUNNING
							&& translateTransition.getStatus() != PathTransition.Status.RUNNING) {
						translateAnim.setByX(+1000);
						translateAnim.play();
						runningState = true;
					}
				}
			} else if (e.getCode() == KeyCode.SHIFT) {
				if (jumpTransition.getStatus() == PathTransition.Status.STOPPED) {
				}

			} else if (e.getCode() == KeyCode.LEFT) {

				right = false;
			} else if (e.getCode() == KeyCode.UP && !isFalling) {
				if (right) {
					jumpRight();

				} else {
					jumpLeft();
				}

				translateAnim.stop();
				runningState = false;
				jumpTransition.setCycleCount(1);
				jumpTransition.play();
				jumpTransition.setOnFinished(ev -> {
					motionBlur.setAngle(90);
					delay = 30;
					jumpReleased = true;
				});
			}
		});
		gamePane.setOnKeyReleased(e -> {
			translateAnim.setDuration(Duration.millis(500));
			switch (e.getCode()) {
			case LEFT:
				pressed = true;
				translateAnim.stop();
				decrease = (double) delay / 10.0;
				runningState = true;
				delay = 0;
				if (motionBlurEnabled) {
					motionBlur.setRadius(0);
					resources.sand1.setEffect(motionBlur);
					resources.sand2.setEffect(motionBlur);
				}
				break;
			case RIGHT:
				pressed = true;
				translateAnim.stop();
				decrease = (double) delay / 10.0;
				runningState = true;
				delay = 0;
				if (motionBlurEnabled) {
					motionBlur.setRadius(0);
					resources.sand1.setEffect(motionBlur);
					resources.sand2.setEffect(motionBlur);
				}
				break;
			case UP:
				break;
			default:
				break;
			}
		});

		jumpCurve = new QuadCurve();
		motionBlur = new MotionBlur();
		menRectangle = new Rectangle();
		translateTransition = new TranslateTransition();
		translateTransition.setInterpolator(Interpolator.EASE_IN);
		menRectangle.setFill(Color.TOMATO);
		menRectangle.setStroke(Color.GREY);
		menRectangle.setHeight(50);
		menRectangle.setWidth(10);

		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(35), this));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();

		resources = new Resources();
		resources.loadResources();
		menRectangle.xProperty()
				.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
					if (delay < 40) {
						delay++;
					}
					if (right) {
						if (motionBlurEnabled) {
							motionBlur.setAngle(180);
							motionBlur.setRadius(delay * 0.5);
							resources.sand1.setEffect(motionBlur);
							resources.sand2.setEffect(motionBlur);

						}
					} else {
						if (motionBlurEnabled) {
							motionBlur.setAngle(0);
							motionBlur.setRadius(delay * 0.5);
							resources.sand1.setEffect(motionBlur);
							resources.sand2.setEffect(motionBlur);
						}
					}
					if (resources.men.getTranslateX() + resources.men.getX() < -50 && shift) {
						System.out.println("Go Right");
						System.out.println(resources.men.getX());
						resources.men.setX(resources.men.getX() + 1000);
						shift = false;

					} else if (resources.men.getTranslateX() + resources.men.getX() > 950 && shift) {
						System.out.println("Go Left");
						System.out.println(resources.men.getX());
						resources.men.setX(resources.men.getX() - 1000);
						shift = false;

					} else if (jumpTransition.getStatus() != Animation.Status.RUNNING) {
						shift = true;
						for (ImageView rec : list) {
							if (menRectangle.intersects(rec.getBoundsInParent())) {
								if (rec.getBoundsInParent().getMinY() > menRectangle.getY()
										+ menRectangle.getHeight() * 0.7) {
									resources.men.setTranslateY(resources.men.getTranslateY() - 1);
								} else if (right) {
									translateAnim.stop();
									resources.men.setImage(resources.imgCollide);
									collisionDelay = 5;
									resources.men.setTranslateX(resources.men.getTranslateX() - 1);
									delay = 0;
								} else {
									translateAnim.stop();
									resources.men.setImage(resources.imgCollide);
									collisionDelay = 5;
									resources.men.setTranslateX(resources.men.getTranslateX() + 1);
									delay = 0;
								}
								break;
							}
						}
					}

				});
		menRectangle.yProperty()
				.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
					for (ImageView iv : list) {
						Bounds b = iv.getBoundsInParent();
						if (menRectangle.intersects(b)) {
							jumpTransition.stop();
							jumpReleased = true;
							if (menRectangle.getY() + menRectangle.getHeight() - 1 < iv.getY() + b.getHeight() / 2) {

								resources.men.setTranslateY(resources.men.getTranslateY() - 2);
								if (right) {
									resources.men.setTranslateX(resources.men.getTranslateX() + 0.25);
								} else {
									resources.men.setTranslateX(resources.men.getTranslateX() - 0.25);
								}
								break;
							} else if (b.contains(menRectangle.getX() + menRectangle.getWidth() / 2.0,
									menRectangle.getY() - 5)
									|| b.contains(menRectangle.getX() + menRectangle.getWidth() / 2.0 + 15,
											menRectangle.getY() - 5)
									|| b.contains(menRectangle.getX() + menRectangle.getWidth() / 2.0 - 15,
											menRectangle.getY() - 5)) {
							} else {

								if (right) {
									resources.men.setTranslateX(resources.men.getTranslateX() - 5);
								} else {
									resources.men.setTranslateX(resources.men.getTranslateX() + 5);
								}

								resources.men.setTranslateY(
										resources.men.getTranslateY() + iv.getY() + iv.getBoundsInParent().getHeight()
												- menRectangle.getY() - menRectangle.getHeight());

								break;
							}

						}
					}
				});
		
		Button butBack = new Button("Back");
		butBack.setVisible(true);
		butBack.setMinSize(90, 35);
		butBack.setMaxSize(90, 35);
		butBack.setPrefSize(90, 35);
		butBack.setLayoutX(30);
		butBack.setLayoutY(30);
		butBack.setOnAction(rotat -> {
			start(stg);
		});
		
		gamePane.getChildren().addAll(resources.sky1, resources.sky2, resources.sand1, resources.sand2, resources.men);
		
		gamePane.getChildren().add(butBack);
		Scene scene = new Scene(gamePane, 1024, 600);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		stageGame.setScene(scene);
		stageGame.show();
		stageGame.setTitle("TheGame");

		gamePane.setScaleShape(true);
	}

	@Override
	public void handle(ActionEvent event) {
		if (!runningState && !jumpReleased) {
			if (delay > 0 && !right && motionBlurEnabled) {
				motionBlur.setAngle(-180);
				motionBlur.setRadius(0);
				resources.sand1.setEffect(motionBlur);
				resources.sand2.setEffect(motionBlur);

			} else if (delay > 0 && right && motionBlurEnabled) {
				motionBlur.setAngle(0);
				motionBlur.setRadius(0);
				resources.sand1.setEffect(motionBlur);
				resources.sand2.setEffect(motionBlur);

			}
		} else if (delay > 0 && jumpReleased && motionBlurEnabled) {
			motionBlur.setAngle(90);
			motionBlur.setRadius((delay -= 4) * 0.7);
			resources.sand1.setEffect(motionBlur);
			resources.sand2.setEffect(motionBlur);

		} else {
			jumpReleased = false;
		}

	}

	public void jumpLeft() {
		if (delay > 25) {
			jumpCurve.startXProperty().bind(resources.men.translateXProperty().add(47).add(resources.men.xProperty()));
			jumpCurve.startYProperty().bind(resources.men.translateYProperty().add(resources.men.getFitHeight() / 2)
					.add(resources.men.xProperty()));

			jumpCurve.controlXProperty().bind(
					resources.men.translateXProperty().add(-delay * 10 / 2).add(47).add(resources.men.xProperty()));
			jumpCurve.controlYProperty()
					.bind(resources.men.translateYProperty().add(-80).add(resources.men.xProperty()));

			jumpCurve.endXProperty()
					.bind(resources.men.translateXProperty().add(-delay * 10).add(47).add(resources.men.xProperty()));
			jumpCurve.endYProperty().bind(resources.men.translateYProperty().add(resources.men.getFitHeight() / 2)
					.add(resources.men.xProperty()));
		} else {
			jumpCurve.startXProperty().bind(resources.men.translateXProperty().add(47).add(resources.men.xProperty()));
			jumpCurve.startYProperty().bind(resources.men.translateYProperty().add(resources.men.getFitHeight() / 2)
					.add(resources.men.xProperty()));

			jumpCurve.controlXProperty()
					.bind(resources.men.translateXProperty().add(-25 * 10 / 2).add(47).add(resources.men.xProperty()));
			jumpCurve.controlYProperty()
					.bind(resources.men.translateYProperty().add(-80).add(resources.men.xProperty()));

			jumpCurve.endXProperty()
					.bind(resources.men.translateXProperty().add(-25 * 10).add(47).add(resources.men.xProperty()));
			jumpCurve.endYProperty().bind(resources.men.translateYProperty().add(resources.men.getFitHeight() / 2)
					.add(resources.men.xProperty()));
		}
	}

	public void jumpRight() {
		if (delay > 25) {
			jumpCurve.startXProperty().bind(resources.men.translateXProperty().add(47));
			jumpCurve.startYProperty().bind(resources.men.translateYProperty().add(resources.men.getFitHeight() / 2));

			jumpCurve.controlXProperty().bind(resources.men.translateXProperty().add(delay * 10 / 2).add(27));
			jumpCurve.controlYProperty().bind(resources.men.translateYProperty().add(-80));

			jumpCurve.endXProperty().bind(resources.men.translateXProperty().add(delay * 10).add(47));
			jumpCurve.endYProperty().bind(resources.men.translateYProperty().add(resources.men.getFitHeight() / 2));
		} else {
			jumpCurve.startXProperty().bind(resources.men.translateXProperty().add(47));
			jumpCurve.startYProperty().bind(resources.men.translateYProperty().add(resources.men.getFitHeight() / 2));

			jumpCurve.controlXProperty().bind(resources.men.translateXProperty().add(25 * 10 / 2).add(47));
			jumpCurve.controlYProperty().bind(resources.men.translateYProperty().add(-80));

			jumpCurve.endXProperty().bind(resources.men.translateXProperty().add(25 * 10).add(47));
			jumpCurve.endYProperty().bind(resources.men.translateYProperty().add(resources.men.getFitHeight() / 2));
		}
	}

	public void unbindWorld() {
		for (Node n : gamePane.getChildren()) {
			n.translateXProperty().unbind();
		}
		resources.sky2.setTranslateX(resources.sky2.getTranslateX() - 1000);
		resources.sky1.setTranslateX(resources.sky1.getTranslateX() - 1000);

	}

	public void rebindWorld(boolean left) {

		resources.sky1.translateXProperty().bind(resources.men.translateXProperty().divide(-3.5));
		resources.sky2.translateXProperty().bind(resources.men.translateXProperty().divide(-3.5).add(999));
		resources.sand1.translateXProperty().bind(resources.men.translateXProperty().divide(-2));
		resources.sand2.translateXProperty().bind(resources.men.translateXProperty().divide(-2).add(1000));
	}

}
