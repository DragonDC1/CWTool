package de.fida.cwtool;

import java.io.Serializable;

public enum CW_Build  implements Serializable {
    SCORP {
        public String toString() {
            return "Scorp";
        }
    },
    DOG {
        public String toString() {
            return "Dog";
        }
    },
    SPINNE {
        public String toString() {
            return "Spinne";
        }
    },
    PORC {
        public String toString() {
            return "Porc";
        }
    },
    PHOON {
        public String toString() {
            return "Phoon";
        }
    },
    RETCHER {
        public String toString() {
            return "Retcher";
        }
    };
}
