package com.luminous.android.coinman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	int manState = 0;
	int pause = 0;
	float gravity = 0.2f;
	float velocity = 0;
	int manY = 0;
	Rectangle manRectangle;

	int score = 0;
	BitmapFont scoreText;
	int gameState = 0;

	Random random;

	ArrayList<Integer> coinXs = new ArrayList<>();
	ArrayList<Integer> coinYs = new ArrayList<>();
	ArrayList<Rectangle> coinRectangles = new ArrayList<>();
	Texture coin;
	int coinCount;

	ArrayList<Integer> bombXs = new ArrayList<>();
	ArrayList<Integer> bombYs = new ArrayList<>();
	ArrayList<Rectangle> bombRectangles = new ArrayList<>();
	Texture bomb;
	int bombCount;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		man = new Texture[4];
		man[0] = new Texture("frame-1.png");
		man[1] = new Texture("frame-2.png");
		man[2] = new Texture("frame-3.png");
		man[3] = new Texture("frame-4.png");
		manY = Gdx.graphics.getHeight() / 3;
//		manRectangle = new Rectangle();

		coin = new Texture("coin.png");
		coinCount = 0;
		random = new Random();

		bomb = new Texture("bomb.png");
		bombCount = 0;

		scoreText = new BitmapFont();
		scoreText.setColor(Color.WHITE);
		scoreText.getData().setScale(10);
	}

	public void makeCoin() {
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		coinYs.add((int)height);
		coinXs.add(Gdx.graphics.getWidth());
	}

	public void makeBomb() {
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		bombYs.add((int)height);
		bombXs.add(Gdx.graphics.getWidth());
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		// game state
		if (1 == gameState) {
			// Game is live
		}

		// coins
		if (coinCount < 100) {
			coinCount++;
		} else {
			coinCount = 0;
			makeCoin();
		}

		coinRectangles.clear();
		for (int i = 0; i < coinXs.size(); i++) {
			batch.draw(coin, coinXs.get(i), coinYs.get(i));
			// moving animation creation
			coinXs.set(i, coinXs.get(i) - 4);
			// for collision
			coinRectangles.add(new Rectangle(coinXs.get(i), coinYs.get(i), coin.getWidth(), coin.getHeight()));
		}

		// bombs
		if (bombCount < 500) {
			bombCount++;
		} else {
			bombCount = 0;
			makeBomb();
		}

		bombRectangles.clear();
		for (int i = 0; i < bombXs.size(); i++) {
			batch.draw(bomb, bombXs.get(i), bombYs.get(i));
			// moving animation creation
			bombXs.set(i, bombXs.get(i) - 4);
			// for collision
			bombRectangles.add(new Rectangle(bombXs.get(i), bombYs.get(i), bomb.getWidth(), bomb.getHeight()));
		}

		if (Gdx.input.justTouched()) {
			velocity = -10;
		}

		if(pause < 8) {
			pause++;
		} else {
			pause = 0;

			if (manState < 3) {
				manState++;
			} else {
				manState = 0;
			}
		}

		velocity = velocity + gravity;
		manY -= velocity;

		if (manY <= 10) {
			manY = 10;
		}

		batch.draw(man[manState], Gdx.graphics.getWidth() / 3, manY);

		// collision detection
		// coins
		manRectangle = new Rectangle(Gdx.graphics.getWidth() / 3, manY, man[manState].getWidth(), man[manState].getHeight());
		for (int i = 0; i< coinRectangles.size(); i++) {
			if (Intersector.overlaps(manRectangle, coinRectangles.get(i))) {
				score++;

				coinRectangles.remove(i);
				coinXs.remove(i);
				coinYs.remove(i);
				break;
			}
		}

		// bomb
		for (int i = 0; i< bombRectangles.size(); i++) {
			if (Intersector.overlaps(manRectangle, bombRectangles.get(i))) {

			}
		}

		scoreText.draw(batch, String.valueOf(score), 100, 175);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
