package com.truelanz.test1.utils;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

/**
* Classe para converter várias imagens (objects, tilesets) de variadas pastas contidas em inputDir
* em um só .png, gerando também o arquivo .atlas. <br>
* Faz-se necessário a criação do arquivo pack.json: "https://libgdx.com/wiki/tools/texture-packer#settings"
*/
public class TexturePackerTool {

    public static void main(String[] args) {
        String inputDir = "assets/asset";
        String outputDir = "assets/graphics";
        String packFileName = "objects";

        TexturePacker.process(inputDir, outputDir, packFileName);
    }
}
