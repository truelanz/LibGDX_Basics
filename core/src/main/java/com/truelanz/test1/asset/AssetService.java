package com.truelanz.test1.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Disposable;

/**
 * Serviço responsável por gerenciar o carregamento, acesso e descarte
 * de recursos (assets) do jogo utilizando o {@link AssetManager} do libGDX.
 *
 * <p>Assets incluem:</p>
 * <ul>
 *     <li>Texturas</li>
 *     <li>Mapas do Tiled</li>
 *     <li>Áudios</li>
 *     <li>Fontes</li>
 *     <li>Sprites</li>
 * </ul>
 *
 * <p>Este serviço centraliza o gerenciamento de recursos em um único ponto
 * da aplicação, evitando duplicação de memória e facilitando a manutenção.</p>
 *
 * <p>O {@link AssetManager} realiza carregamento assíncrono por padrão,
 * permitindo que o jogo mostre telas de carregamento enquanto os assets
 * são carregados em background.</p>
 *
 * <p>O AssetManager também realiza contagem de referências (reference counting),
 * garantindo que um asset compartilhado entre múltiplos objetos só seja
 * liberado quando não estiver mais em uso.</p>
 *
 * <p>Esta classe também registra um loader específico para arquivos
 * {@code .tmx} criados no editor Tiled.</p>
 */
public class AssetService implements Disposable {

    /**
     * Gerenciador de assets do libGDX.
     *
     * <p>Responsável por:</p>
     * <ul>
     *     <li>carregar recursos</li>
     *     <li>armazená-los em cache</li>
     *     <li>gerenciar dependências entre assets</li>
     *     <li>descarregar recursos quando não forem mais necessários</li>
     * </ul>
     */
    private final AssetManager assetManager;

    /**
     * Cria um novo serviço de assets utilizando um {@link FileHandleResolver}.
     *
     * <p>O resolver define como os caminhos de arquivos serão resolvidos
     * dentro da aplicação (por exemplo, arquivos dentro da pasta assets).</p>
     *
     * <p>Também registra um loader específico para carregar mapas
     * {@link TiledMap} a partir de arquivos {@code .tmx} usando
     * {@link TmxMapLoader}.</p>
     *
     * @param fileHandleResolver mecanismo usado para resolver caminhos de arquivos
     */
    public AssetService(FileHandleResolver fileHandleResolver) {
        this.assetManager = new AssetManager(fileHandleResolver);

        // registra o loader para arquivos TMX (mapas do Tiled)
        this.assetManager.setLoader(TiledMap.class, new TmxMapLoader());
    }

    /**
     * Carrega um asset imediatamente de forma síncrona.
     *
     * <p>Este método bloqueia a execução até que o asset seja completamente
     * carregado. Ele deve ser usado com cuidado, pois pode causar travamentos
     * temporários se o asset for muito grande.</p>
     *
     * <p>Fluxo interno:</p>
     * <ol>
     *     <li>Enfileira o asset para carregamento</li>
     *     <li>Força o carregamento completo com {@code finishLoading()}</li>
     *     <li>Retorna o asset carregado</li>
     * </ol>
     *
     * @param asset descrição do asset a ser carregado
     * @param <T> tipo do asset
     * @return asset carregado
     */
    public <T> T load(Asset<T> asset) {
        this.assetManager.load(asset.getDescriptor());
        this.assetManager.finishLoading();
        return this.assetManager.get(asset.getDescriptor());
    }

    /**
     * Enfileira um asset para carregamento assíncrono.
     *
     * <p>O asset será carregado posteriormente quando
     * {@link #update()} for chamado repetidamente
     * (normalmente no loop principal do jogo).</p>
     *
     * <p>Este método é ideal para implementar telas de loading.</p>
     *
     * @param asset descrição do asset
     * @param <T> tipo do asset
     */
    public <T> void queue(Asset<T> asset) {
        this.assetManager.load(asset.getDescriptor());
    }

    /**
     * Recupera um asset já carregado.
     *
     * <p>O asset precisa ter sido previamente carregado usando
     * {@link #load(Asset)} ou {@link #queue(Asset)}.</p>
     *
     * @param asset descrição do asset
     * @param <T> tipo do asset
     * @return asset carregado
     */
    public <T> T get(Asset<T> asset) {
        return this.assetManager.get(asset.getDescriptor());
    }

    /**
     * Atualiza o carregamento assíncrono dos assets.
     *
     * <p>Esse método deve ser chamado repetidamente no loop do jogo.
     * Ele carrega gradualmente os assets da fila.</p>
     *
     * @return true se todos os assets já foram carregados
     */
    public boolean update() {
        return this.assetManager.update();
    }

    /**
     * Imprime informações de diagnóstico sobre os assets carregados.
     *
     * <p>Utiliza o sistema de logging do libGDX para mostrar:</p>
     * <ul>
     *     <li>assets carregados</li>
     *     <li>dependências</li>
     *     <li>estado do AssetManager</li>
     * </ul>
     *
     * <p>Útil para depuração de problemas de carregamento.</p>
     */
    public void debugDiagnostics() {
        Gdx.app.debug("AssertService", this.assetManager.getDiagnostics());
    }

    /**
     * Libera todos os assets carregados e limpa a memória utilizada.
     *
     * <p>Esse método deve ser chamado quando o jogo ou aplicação
     * estiver sendo encerrado.</p>
     *
     * <p>Implementa a interface {@link Disposable}, padrão do libGDX
     * para gerenciamento explícito de recursos.</p>
     */
    @Override
    public void dispose() {
        this.assetManager.dispose();
    }
}
