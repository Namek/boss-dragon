package net.bossdragon.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.ApplicationLogger;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import net.bossdragon.GdxArtemisGame;

public class HtmlLauncher extends GwtApplication {

    @Override
    public GwtApplicationConfiguration getConfig () {
        return new GwtApplicationConfiguration(900, 600);
    }

    @Override
    public ApplicationListener createApplicationListener() {
        return new GdxArtemisGame();
    }

    @Override
    public void setApplicationLogger(ApplicationLogger applicationLogger) {
    }

    @Override
    public ApplicationLogger getApplicationLogger() {
        return null;
    }
}
