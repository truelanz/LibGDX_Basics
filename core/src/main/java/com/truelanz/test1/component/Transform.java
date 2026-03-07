package com.truelanz.test1.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Vector2;

public class Transform implements Component, Comparable<Transform> {
    public static final ComponentMapper<Transform> MAPPER = ComponentMapper.getFor(Transform.class);

    private final Vector2 position;
    private final Vector2 scaling;
    private final Vector2 size;
    private final int z; //plano da entity (layer)
    private float rotationDeg; //rotação em graus

    public Transform(Vector2 position, Vector2 scaling, Vector2 size, int z, float rotationDeg) {
        this.position = position;
        this.scaling = scaling;
        this.size = size;
        this.z = z;
        this.rotationDeg = rotationDeg;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getScaling() {
        return scaling;
    }

    public Vector2 getSize() {
        return size;
    }

    public int getZ() {
        return z;
    }

    public float getRotationDeg() {
        return rotationDeg;
    }

    public void setRotationDeg(float rotationDeg) {
        this.rotationDeg = rotationDeg;
    }

    /**
    * <li>Ordene pela profundidade (z).</li>
    * <li>Se empatar, ordene pela posição horizontal (x).</li>
    * <li>Se ainda empatar, ordene pela posição vertical (y).</li>
    * @param other the object to be compared.
    */
    @Override
    public int compareTo(Transform other) {
        if (this.z != other.z)
            return Float.compare(this.z, other.z);
        if (this.position.x != other.position.x)
            return Float.compare(this.position.x, other.position.x);
        return Float.compare(this.position.y, other.position.y);
    }
}
