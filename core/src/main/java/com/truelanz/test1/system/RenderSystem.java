package com.truelanz.test1.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.truelanz.test1.component.Graphic;
import com.truelanz.test1.component.Transform;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.truelanz.test1.T1game.UNIT_SCALE;

public class RenderSystem extends SortedIteratingSystem implements Disposable {

    private final OrthogonalTiledMapRenderer mapRenderer;
    private final Batch batch;
    private final Viewport viewport;
    private final OrthographicCamera camera;
    private final List<MapLayer> fdglayers;
    private final List<MapLayer> bdglayers;

    public RenderSystem(Batch batch, Viewport viewport, OrthographicCamera camera) {
        super(
            Family.all(Transform.class, Graphic.class).get(),
            Comparator.comparing(Transform.MAPPER::get)
        );
        this.batch = batch;
        this.viewport = viewport;
        this.camera = camera;
        this.fdglayers = new ArrayList<>();
        this.bdglayers = new ArrayList<>();
        this.mapRenderer = new OrthogonalTiledMapRenderer(null, UNIT_SCALE, this.batch);
    }

    private boolean sortDirty = false;
    public void markSortDirty() {
        this.sortDirty = true;
    }

    // ex: player muda de layer ao entrar em uma casa (z: 0 → 2)
    //transform.setZ(2);
    //renderSystem.markSortDirty(); // avisa que precisa reordenar

    @Override
    public void update(float deltaTime) {

        AnimatedTiledMapTile.updateAnimationBaseTime();

        this.viewport.apply();
        batch.begin();
        this.batch.setColor(Color.WHITE);
        this.mapRenderer.setView(this.camera);
        bdglayers.forEach(mapRenderer::renderMapLayer);

        if (sortDirty) {
            forceSort();
            sortDirty = false;
        }

        super.update(deltaTime);
        this.batch.setColor(Color.WHITE);
        fdglayers.forEach(mapRenderer::renderMapLayer);
        batch.end();
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        Transform transform = Transform.MAPPER.get(entity);
        Graphic graphic = Graphic.MAPPER.get(entity);

        if(graphic.getRegion() == null) return;
        Vector2 position = transform.getPosition();
        Vector2 scaling = transform.getScaling();
        Vector2 size = transform.getSize();

        this.batch.setColor(graphic.getColor());
        this.batch.draw(
            graphic.getRegion(),
            //Mantem a entidade na mesma posição após mudanças de escala.
            position.x - (1f - scaling.x) * size.x * 0.5f,
            position.y - (1f - scaling.y) * size.y * 0.5f,

            size.x * 0.5f, size.y * 0.5f,
            size.x, size.y,
            scaling.x, scaling.y,
            transform.getRotationDeg()
        );
    }

    /**
    * Recupera propriedades das camadas do Tiled
    * @param tiledMap TiledMap
    */
    public void setMap(TiledMap tiledMap) {
        this.mapRenderer.setMap(tiledMap);

        /* Lógica de renderização de layer
        Até encontrar os objects... layer vai ´para bdg, depois que encontra objects... layer vai para fdg.
        Ex: bdglayers:
            - Chão
            - grama
        --- objects: player ---
        fdglayers:
            - Árvore Cobrindo player
            - Telhado */
        List<MapLayer> currentLayers = bdglayers;
        this.fdglayers.clear();
        this.bdglayers.clear();
        for(MapLayer layer : tiledMap.getLayers()) {
            if ("objects".equals(layer.getName())) {
                currentLayers = fdglayers;
                continue;
            }
            //Se a layer NÃO for uma TiledMapTileLayer → ignore ela
            if(!(layer instanceof TiledMapTileLayer)) {
                continue;
            }
            currentLayers.add(layer);
        }
    }

    @Override
    public void dispose() {
        this.mapRenderer.dispose();
    }
}
