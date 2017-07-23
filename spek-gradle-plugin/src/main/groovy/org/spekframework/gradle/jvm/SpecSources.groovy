package org.spekframework.gradle.jvm

class SpecSources {
    final String name

    private boolean enabled = true

    SpecSources(String name) {
        this.name = name
    }

    boolean isEnabled() {
        return enabled
    }

    void setEnabled(boolean enabled) {
        this.enabled = enabled
    }
}
