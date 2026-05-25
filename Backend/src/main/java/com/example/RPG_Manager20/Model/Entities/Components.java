package com.example.RPG_Manager20.Model.Entities;

import jakarta.persistence.Embeddable;

@Embeddable
public class Components {
    private boolean material;
    private String raw;
    private boolean somatic;
    private boolean verbal;

    public Components() {}

    public Components(boolean material, String raw, boolean somatic, boolean verbal) {
        this.material = material;
        this.raw = raw;
        this.somatic = somatic;
        this.verbal = verbal;
    }

    public boolean isMaterial() {
        return material;
    }

    public void setMaterial(boolean material) {
        this.material = material;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public boolean isSomatic() {
        return somatic;
    }

    public void setSomatic(boolean somatic) {
        this.somatic = somatic;
    }

    public boolean isVerbal() {
        return verbal;
    }

    public void setVerbal(boolean verbal) {
        this.verbal = verbal;
    }
}
