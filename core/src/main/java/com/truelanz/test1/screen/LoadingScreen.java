package com.truelanz.test1.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.truelanz.test1.T1game;
import com.truelanz.test1.asset.AssetService;
import com.truelanz.test1.asset.AtlasAsset;

public class LoadingScreen extends ScreenAdapter {

    private final T1game game;
    private final AssetService assetService;

    public LoadingScreen(T1game game, AssetService assetService) {
        this.game = game;
        this.assetService = assetService;
    }

    @Override
    public void show() {
        for (AtlasAsset atlas : AtlasAsset.values()) {
            assetService.load(atlas);
        }
    }

    @Override
    public void render(float delta) {
        if(this.assetService.update()) {
            Gdx.app.debug("LoadingScreen", "Asset updated... Loading finished!");
            createScreens();
            this.game.removeScreen(this);
            this.dispose();
            this.game.setScreen(GameScreen.class);
        }
    }

    private void createScreens() {
        this.game.addScreen(new GameScreen(this.game));
    }
}
