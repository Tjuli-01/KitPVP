package de.tjuli.kitpvp.editor;

import de.tjuli.kitpvp.kit.Kit;

public class Editor {
    private Kit kit;
    public Editor(Kit kit) {
        this.kit = kit;
    }

    public void setKit(Kit kit) {
        this.kit = kit;
    }

    public Kit getKit() {
        return kit;
    }
}


