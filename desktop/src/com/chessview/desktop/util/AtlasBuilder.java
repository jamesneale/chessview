package com.chessview.desktop.util;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

public class AtlasBuilder {
	public static void main(String[] args) {
		Settings settings = new Settings();
		settings.maxWidth = 2048;
		settings.maxHeight = 2048;
		TexturePacker.process(settings, "C:/Users/Kiuas/Pictures/solo project images", 
				"C:/Users/Kiuas/Documents/GitHub/chessview/android/assets", "atlas");
		System.out.println("Done!");
	}
}
