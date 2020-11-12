package net.idik.lib.cipher.so.extension

import org.gradle.api.Project

class CipherExt {

    SoExt so

    Project project

    CipherExt(Project project) {
        this.so = new SoExt(project)
        this.project = project
    }

    def so(Closure closure) {
        project.configure(so, closure)
    }
}