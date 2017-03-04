package net.bossdragon;

import com.badlogic.gdx.Game;

public class GdxArtemisGame extends Game {

	private static GdxArtemisGame instance;

	@Override
	public void create() {
		instance = this;
		restart();
	}

	public void restart() {
		setScreen(new net.bossdragon.screen.GameScreen());
	}

	public static GdxArtemisGame getInstance()
	{
		return instance;
	}
}
