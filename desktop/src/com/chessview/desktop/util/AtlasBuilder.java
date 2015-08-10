package com.chessview.desktop.util;

import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

public class AtlasBuilder {
	public static void main(String[] args) {
		Settings settings = new Settings();
		settings.maxWidth = 2048;
		settings.maxHeight = 2048;

		settings.filterMin = TextureFilter.Linear;
		settings.filterMag = TextureFilter.Linear;
		TexturePacker.process(settings, "C:/Users/jneal_000/Desktop/chessview/chessview/raw", 
				"C:/Users/jneal_000/Desktop/chessview/chessview/android/assets", "atlas");
		System.out.println("Done!");
	}
}
