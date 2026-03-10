package com.truelanz.test1.tiled;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.truelanz.test1.asset.AssetService;
import com.truelanz.test1.asset.MapAsset;

import java.util.function.Consumer;

/**
* Serviço responsável pelo carregamento, troca e gerenciamento de mapas Tiled. <p>
* Também permite notificar mudanças de mapa e processar objetos definidos no mapa. </p>
*/
public class TiledService {

    /** Serviço utilizado para carregar e descarregar assets. */
    private final AssetService assetService;
    /** Mapa atualmente ativo. */
    private TiledMap currentMap;
    /** Consumidor chamado quando o mapa atual muda. */
    private Consumer<TiledMap> mapChangeConsumer;
    /** Consumidor responsável por processar objetos do mapa. */
    private Consumer<TiledMapTileMapObject> loadObjectConsumer;

    /**
    * Cria o serviço de mapas.
    * @param assetService serviço de assets utilizado para carregar mapas
    */
    public TiledService(AssetService assetService) {
        this.assetService = assetService;
        this.currentMap = null;
        this.mapChangeConsumer = null;
        this.loadObjectConsumer = null;
    }

    /**
    * Carrega um mapa a partir de um {@link MapAsset}.
    * @param mapAsset asset do mapa a ser carregado
    * @return mapa Tiled carregado
    */
    public TiledMap loadMap(MapAsset mapAsset) {
        TiledMap tiledMap = this.assetService.load(MapAsset.MAIN);
        tiledMap.getProperties().put("mapAsset", mapAsset);
        return tiledMap;
    }

    /**
    * Define o mapa atual do jogo.
    * <p>
    * Descarrega o mapa anterior e notifica o consumidor de mudança de mapa.
    * </p>
    * @param map novo mapa ativo
    */
    public void setMap(TiledMap map) {
        if(this.currentMap != null) {
            this.assetService.unload(this.currentMap.getProperties().get("mapAsset", MapAsset.class));
        }

        this.currentMap = map;
        if (this.mapChangeConsumer != null) {
            this.mapChangeConsumer.accept(this.currentMap);
        }
    }

    /**
    * Percorre as camadas do mapa e carrega objetos da camada "objects".
    * @param tiledMap mapa a ser processado
    */
    private void loadMapObjects(TiledMap tiledMap) {
        for (MapLayer layer : tiledMap.getLayers()) {
            if("objects".equals(layer.getName())) {
                loadObjectLayer(layer);
            }
        }
    }

    /**
    * Processa todos os objetos de uma camada do mapa.
    * @param mapLayer camada contendo objetos
    */
    private void loadObjectLayer(MapLayer mapLayer) {
        if(loadObjectConsumer == null) return;

        for (MapObject mapObject : mapLayer.getObjects()) {
            if(mapObject instanceof TiledMapTileMapObject tileMapObject) {
                loadObjectConsumer.accept(tileMapObject);
            } else {
                throw new GdxRuntimeException(
                    "Unsupported MapObject type: " + mapObject.getClass().getName());
            }
        }
    }

    /**
    * Define o consumidor chamado quando o mapa atual muda.
    * @param mapChangeConsumer função executada após mudança de mapa
    */
    public void setMapChangeConsumer(Consumer<TiledMap> mapChangeConsumer) {
        this.mapChangeConsumer = mapChangeConsumer;
    }

    public void setLoadObjectConsumer(Consumer<TiledMapTileMapObject> loadObjectConsumer) {
        this.loadObjectConsumer = loadObjectConsumer;
    }

    /**
    * Define manualmente o mapa atual.
    * @param currentMap mapa a ser definido como ativo
    */
    public void setCurrrentMap(TiledMap currentMap) {
        this.currentMap = currentMap;
    }

}
