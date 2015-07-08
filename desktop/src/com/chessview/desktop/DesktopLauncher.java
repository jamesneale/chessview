package com.chessview.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.chessview.ChessView;
import com.chessview.screen.AbstractScreen;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.width = AbstractScreen.kVirtualWidth;
		config.height = AbstractScreen.kVirtualHeight;
		
		new LwjglApplication(new ChessView(), config);
	}
}
