package com.truelanz.test1.asset;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

/**
 * Enum responsável por registrar todos os mapas Tiled utilizados no jogo.
 * <p>
 * Cada constante representa um mapa localizado na pasta {@code assets/maps}.
 * O carregamento é configurado utilizando {@link TmxMapLoader.Parameters},
 * permitindo definir um {@code tiled-project} para resolver corretamente
 * referências a tilesets e outros recursos do Tiled.
 * </p>
 *
 * <p>
 * Os mapas registrados aqui podem ser carregados através do sistema de
 * gerenciamento de assets do LibGDX utilizando seus respectivos
 * {@link AssetDescriptor}.
 * </p>
 */
public enum MapAsset implements Asset<TiledMap> {

    /**
     * Mapa principal do jogo.
     */
    MAIN("main.tmx");

    /**
     * Descriptor usado pelo {@link com.badlogic.gdx.assets.AssetManager}
     * para carregar o mapa.
     */
    private final AssetDescriptor<TiledMap> descriptor;

    /**
     * Constrói um registro de mapa.
     *
     * @param mapName Nome do arquivo .tmx localizado na pasta {@code maps}.
     */
    MapAsset(String mapName) {
        TmxMapLoader.Parameters parameters = new TmxMapLoader.Parameters();

        /*
        * Define o arquivo de projeto do Tiled.
        * Isso permite que o loader resolva corretamente tilesets e outros caminhos relativos definidos no Tiled.
        */
        parameters.projectFilePath = "t1.tiled-project";

        //Cria o descriptor do asset apontando para o mapa dentro da pasta assets/maps.
        this.descriptor = new AssetDescriptor<>(
            "maps/" + mapName,
            TiledMap.class,
            parameters
        );
    }

    /**
    * Retorna o descriptor usado pelo {@link com.badlogic.gdx.assets.AssetManager}
    * para carregar este mapa.
    *
    * @return descriptor do mapa Tiled
    */
    @Override
    public AssetDescriptor<TiledMap> getDescriptor() {
        return this.descriptor;
    }
}
