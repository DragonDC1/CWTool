package de.fida.cwtool;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public enum CW_Build implements Serializable {
    SCORP {
        @Override
        public List<Category> getCategory() {
            List<Category> categories = new ArrayList<>();

            categories.add(Category.RANGE);

            return categories;
        }

        public String toString() {
            return "Scorp";
        }
    },
    DOG {
        @Override
        public List<Category> getCategory() {
            List<Category> categories = new ArrayList<>();

            categories.add(Category.MELEE);

            return categories;
        }

        public String toString() {
            return "Dog";
        }
    },
    SPINNE {
        @Override
        public List<Category> getCategory() {
            List<Category> categories = new ArrayList<>();

            categories.add(Category.SPINNE);

            return categories;
        }

        public String toString() {
            return "Spinne";
        }
    },
    PORC {
        @Override
        public List<Category> getCategory() {
            List<Category> categories = new ArrayList<>();

            categories.add(Category.PORC);

            return categories;
        }

        public String toString() {
            return "Porc";
        }
    },
    PHOON {
        @Override
        public List<Category> getCategory() {
            List<Category> categories = new ArrayList<>();

            categories.add(Category.RANGE);

            return categories;
        }

        public String toString() {
            return "Phoon";
        }
    },
    RETCHER {
        @Override
        public List<Category> getCategory() {
            List<Category> categories = new ArrayList<>();

            categories.add(Category.MID_RANGE);

            return categories;
        }

        public String toString() {
            return "Retcher";
        }
    },
    HELIOS {
        @Override
        public List<Category> getCategory() {
            List<Category> categories = new ArrayList<>();

            categories.add(Category.MID_RANGE);

            return categories;
        }

        public String toString() {
            return "Helios";
        }
    };

    public List<Category> getCategory() {
        return null;
    }
}
