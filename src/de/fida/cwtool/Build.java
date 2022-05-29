package de.fida.cwtool;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Build implements Serializable {
    @Serial
    private static final long serialVersionUID = -8169101013045692867L;
    private CW_Build art;       // Art des Builds
    private boolean doppler;    // mit Doppler?

    public Build (CW_Build art, boolean doppler) {
        this.art = art;
        this.doppler = doppler;
    }

    /*
     * Ändert, ob auf dem de.fida.cwtool.Build ein Doppler mitgenommen werden kann
     */
    public void updateDoppler(boolean bool) {
        this.doppler = bool;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Build build = (Build) o;
        return /*doppler == build.doppler &&*/ art == build.art;
    }

    @Override
    public int hashCode() {
        return Objects.hash(art, doppler);
    }

    public String toString() {
        return "(" + art + ", " +(doppler ? "mit" : "ohne") + " Doppler)";
    }

    public CW_Build getArt() {
        return art;
    }

    public boolean isDoppler() {
        return doppler;
    }
}
