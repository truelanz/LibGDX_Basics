package com.truelanz.test1;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.truelanz.test1.asset.AssetService;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe principal do jogo.
 *
 * <p>Estende {@link Game} e gerencia:</p>
 * <ul>
 *     <li>Batch de renderização</li>
 *     <li>Câmera e viewport</li>
 *     <li>Gerenciamento de assets</li>
 *     <li>Registro e troca de telas</li>
 * </ul>
 */
public class T1game extends Game {

    /** Largura lógica do mundo em unidades de jogo. */
    public static final float WORLD_WIDTH = 16f;
    /** Altura lógica do mundo em unidades de jogo. */
    public static final float WORLD_HEIGHT = 9f;
    /** Escala usada para converter pixels do tileset em unidades de mundo. */
    public static final float UNIT_SCALE = 1f/16f;
    /** Batch usado para renderizar sprites. */
    private Batch batch;
    /** Câmera ortográfica 2D usada no jogo. */
    private OrthographicCamera camera;
    /**
     * Viewport responsável por ajustar a projeção da câmera
     * quando a janela é redimensionada.
     */
    private Viewport viewport;
    /** Serviço responsável pelo carregamento e descarte de assets. */
    private AssetService assetService;
    /** Mede as coisas da GPU em tempo real, como Draw Calls, Vertices, texturas e Shaders...*/
    private GLProfiler glProfiler;

    private FPSLogger fpsLogger;
    /** Cache de telas do jogo. */
    private final Map<Class<? extends Screen>, Screen> screenCache =
        new HashMap<Class<? extends Screen>, Screen>();

    /**
     * Inicializa os componentes principais do jogo.
     *
     * <p>Executado após o LibGDX iniciar.</p>
     */
    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        this.assetService = new AssetService(new InternalFileHandleResolver());
        this.glProfiler = new GLProfiler(Gdx.graphics);
        this.glProfiler.enable();
        this.fpsLogger = new FPSLogger();

        addScreen(new FirstScreen(this));
        setScreen(FirstScreen.class);
    }

    /**
     * Atualiza o viewport quando a janela muda de tamanho.
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        super.resize(width, height);
    }

    /**
     * Adiciona uma tela ao cache do jogo.
     *
     * @param screen instância da tela
     */
    public void addScreen(Screen screen) {
        screenCache.put(screen.getClass(), screen);
    }

    /**
     * Define a tela atual do jogo usando sua classe.
     *
     * @param screenClass classe da tela desejada
     * @throws GdxRuntimeException se a tela não estiver registrada
     */
    public void setScreen(Class<? extends Screen> screenClass) {
        Screen screen = screenCache.get(screenClass);
        if(screen == null) {
            throw new GdxRuntimeException("Screen class " + screenClass + " not found");
        }
        super.setScreen(screen);
    }

    /**
    * Executado a cada frame do jogo.
    * <p>
    * Limpa o buffer de renderização, reinicia o {@code GLProfiler},
    * delega a renderização para a tela atual e atualiza o título da
    * janela com informações de diagnóstico gráfico.
    * </p>
    */
    @Override
    public void render() {

        glProfiler.reset();
        //limpa tela do jogo (deixa tela preta)
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.render();

        Gdx.graphics.setTitle(
            "DrawCalls: " + glProfiler.getDrawCalls() +
                " | TextureBindings: " + glProfiler.getTextureBindings()
        );
        fpsLogger.log();
    }

    /**
     * Libera recursos utilizados pelo jogo.
     */
    @Override
    public void dispose() {

        /* Descarta todas as telas registradas. */
        screenCache.values().forEach(Screen::dispose);
        screenCache.clear();

        super.dispose();

        /* Mostra diagnósticos de assets antes de liberar memória. */
        this.assetService.debugDiagnostics();
        this.assetService.dispose();
    }

    /** @return batch de renderização */
    public Batch getBatch() {
        return batch;
    }

    /** @return câmera do jogo */
    public OrthographicCamera getCamera() {
        return camera;
    }

    /** @return viewport ativo */
    public Viewport getViewport() {
        return viewport;
    }

    /** @return serviço de assets */
    public AssetService getAssetService() {
        return assetService;
    }

    /** @return cache de telas */
    public Map<Class<? extends Screen>, Screen> getScreenCache() {
        return screenCache;
    }
}
