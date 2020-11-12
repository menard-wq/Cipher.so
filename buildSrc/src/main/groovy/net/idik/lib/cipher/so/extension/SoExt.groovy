package net.idik.lib.cipher.so.extension

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

class SoExt {

    NamedDomainObjectContainer<KeyExt> keys

    String signature = ""
    String encryptSeed = "Cipher.so@DEFAULT"
    String ivSpec = ""

    SoExt(Project project) {
        keys = project.container(KeyExt)
    }

    def keys(Closure closure) {
        keys.configure closure
    }
}