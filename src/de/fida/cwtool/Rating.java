package de.fida.cwtool;

import java.io.Serializable;

public enum Rating implements Serializable {
    GOLD {
        @Override
        public String toString() {
            return "Gold";
        }
    },
    SEHR_GUT {
        @Override
        public String toString() {
            return "Sehr gut";
        }
    },
    GUT {
        @Override
        public String toString() {
            return "Gut";
        }
    },
    MITTEL {
        @Override
        public String toString() {
            return "Mittel";
        }
    },
    SCHLECHT {
        @Override
        public String toString() {
            return "Schlecht";
        }
    },
    TROLL {
        @Override
        public String toString() {
            return "Troll";
        }
    },
    PAK4 {
        @Override
        public String toString() {
            return "PAK4";
        }
    };


}
