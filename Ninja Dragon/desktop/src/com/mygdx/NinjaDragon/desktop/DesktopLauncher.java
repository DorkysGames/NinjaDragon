package com.mygdx.NinjaDragon.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.NinjaDragon.NinjaDragon;
import com.mygdx.NinjaDragon.Tools.AdHandler;

public class DesktopLauncher implements AdHandler {
	private static AdHandler handler;

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = NinjaDragon.WIDTH;;
		config.height = NinjaDragon.HEIGHT;
		config.title = NinjaDragon.TITLE;
		new LwjglApplication(new NinjaDragon(handler), config);
	}

	@Override
	public void showBannerAds(boolean show) {

	}

	@Override
	public boolean isVideoAdReady() {
		return false;
	}

	@Override
	public void playVideo() {

	}

}
