package com.truelanz.test1.input;

public interface ControllerState {
    void keyDown(Command command);

    default void keyUp(Command command) {
    }
}
