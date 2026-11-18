package com.auca.interfaces;

public interface Identifiable {
    // Interface constants (public static final by default)
    String ID_PREFIX = "AUC";
    int MIN_ID_LENGTH = 4;
    int MAX_ID_LENGTH = 30;
    String ID_SEPARATOR = "-";
    String DEFAULT_STATUS = "ACTIVE";
    int ENTITY_VERSION = 1;

    String getId();
    String getDisplayName();
    boolean isActive();
}
