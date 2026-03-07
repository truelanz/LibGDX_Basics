package com.truelanz.test1;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.truelanz.test1.asset.AssetService;
import com.truelanz.test1.asset.MapAsset;
import com.truelanz.test1.systyem.RenderSystem;

import static com.truelanz.test1.T1game.UNIT_SCALE;

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen extends ScreenAdapter {
    private final T1game game;
    private final Batch batch;
    private final AssetService assetService;
    private final Viewport viewport;
    private final OrthographicCamera camera;
    private final Engine engine;

    public FirstScreen(T1game game) {
        this.game = game;
        this.assetService = game.getAssetService();
        this.viewport = game.getViewport();
        this.camera = game.getCamera();
        this.batch = game.getBatch();

        this.engine = new Engine();
        this.engine.addSystem(new RenderSystem(this.batch, this.viewport, this.assetService));

    }

    @Override
    public void show() {
        this.assetService.load(MapAsset.MAIN);
        this.engine.getSystem(RenderSystem.class).setMap(this.assetService.get(MapAsset.MAIN));
    }

    @Override
    public void hide() {
        this.engine.removeAllEntities();
    }

    @Override
    public void render(float delta) {
        delta = Math.min(delta, 1/30f);
        this.engine.update(delta);

    }

    @Override
    public void dispose() {
        for (EntitySystem system : this.engine.getSystems()) {
            if (system instanceof Disposable disposableSystem) {
                disposableSystem.dispose();
            }
        }

        //this.mapRenderer.dispose();
        super.dispose();
    }
}
