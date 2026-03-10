# t1

## Arquitetura ECS (Entity Component System)
_*Sistema de componente padrão para LibGDX é o Ashley.*_

| Tipo      | O que faz             |
| --------- | --------------------- |
| Entity    | identifica o objeto   |
| Component | guarda dados          |
| System    | executa comportamento |

## Ideia central do ECS

O ECS separa **dados** e **comportamentos**. 

Ele tem **3 partes principais**:

1. **Entity (Entidade)**
2. **Component (Componente)**
3. **System (Sistema)**

---

# 1️⃣ Entity (Entidade)

A **entidade é apenas um ID**.

Ela não contém lógica nem dados diretamente.

Exemplo:

```
Player = Entity 1
Enemy = Entity 2
Bullet = Entity 3
```
Uma entidade é basicamente um **container de componentes**.

---

# 2️⃣ Component (Componente)

Componentes são **apenas dados**, sem lógica.

Exemplo:

```java
class PositionComponent {
    float x;
    float y;
}

class VelocityComponent {
    float vx;
    float vy;
}

class SpriteComponent {
    Texture texture;
}
```

Agora podemos montar entidades combinando componentes:

### Player

* Position
* Velocity
* Sprite
* Input

### Enemy

* Position
* Velocity
* Sprite
* AI

### Bullet

* Position
* Velocity
* Damage

Perceba:

✔ reutilização total de componentes.

---

# 3️⃣ System (Sistema)
Os **systems contêm a lógica do jogo**.

Eles processam entidades que possuem determinados componentes.

Exemplo:

### MovementSystem

```java
for (Entity e : entitiesWith(Position, Velocity)) {
    position.x += velocity.vx;
    position.y += velocity.vy;
}
```

### RenderSystem

```java
for (Entity e : entitiesWith(Position, Sprite)) {
    draw(sprite, position.x, position.y);
}
```

### AISystem

```java
for (Entity e : entitiesWith(AI, Position)) {
    // lógica do inimigo
}
```

---

# Fluxo do jogo com ECS

Loop do jogo:

```
InputSystem
↓
AISystem
↓
PhysicsSystem
↓
MovementSystem
↓
RenderSystem
```

Cada sistema processa apenas os componentes necessários.

---

## Exemplo
Entidade **Player**:

```
Entity 1
 ├ PositionComponent
 ├ VelocityComponent
 ├ SpriteComponent
 └ InputComponent
```

Sistemas que atuam nela:

* InputSystem
* MovementSystem
* RenderSystem

---

✔ ECS:

```
Entity + Components
```

### Melhor performance

Porque os componentes ficam organizados em **arrays contíguos na memória**.

Isso melhora:

* cache da CPU
* paralelização
* processamento em massa

Por isso ECS é muito usado em **jogos AAA**.

---

### Reutilização extrema

Você reutiliza componentes em qualquer entidade.

Exemplo:

```
HealthComponent
```

Pode existir em:

* player
* inimigos
* boss
* objetos destrutíveis

---

Estrutura:

```
Engine
 ├ Entities
 ├ Components
 └ Systems
```
## Regra :

**Entity = ID**
**Component = Dados**
**System = Lógica**

---

## Resolver lentidão para executar programa:

1 — Daemon do Gradle desativado!?

No `gradle.properties:`
propertiesorg.gradle.daemon=false  ← esse é pode ser problema principal.
Com o daemon desativado, o Gradle inicia uma JVM do zero toda execução, o que custa vários segundos.

Correção:
`propertiesorg.gradle.daemon=true`
O daemon fica em memória entre execuções e as subsequentes ficam muito mais rápidas.

2 — Memória muito baixa
`propertiesorg.gradle.jvmargs=-Xms512M -Xmx1G` ← pode ser pouco dependendo da máquina
Se a máquina tiver RAM disponível, aumentar:
`propertiesorg.gradle.jvmargs=-Xms512M -Xmx2G`

### Configurar no intelliJ:
```
3 — Rodar direto pela IDE em vez de Gradle
Se estiver usando **IntelliJ IDEA**, configure para rodar diretamente pela IDE:
Settings → Build, Execution, Deployment → Build Tools → Gradle`

Mudar:
Build and run using: Gradle  →  IntelliJ IDEA
Run tests using:     Gradle  →  IntelliJ IDEA
```

- Isso elimina o overhead do Gradle no run e cai para 2~4 segundos.

---


A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and a main class extending `Game` that sets the first screen.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.
