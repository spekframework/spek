package org.spekframework.gradle.jvm

import org.gradle.api.NamedDomainObjectContainer

class SpekPluginExtension {
    @Delegate
    private final NamedDomainObjectContainer<SpecSources> speks

    private String apiVersion = "1+"

    SpekPluginExtension(NamedDomainObjectContainer<SpecSources> speks) {
        this.speks = speks
    }

    String getApiVersion() {
        return apiVersion
    }

    void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion
    }
}
