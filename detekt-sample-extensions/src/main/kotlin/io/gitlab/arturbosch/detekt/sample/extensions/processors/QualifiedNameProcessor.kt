package io.gitlab.arturbosch.detekt.sample.extensions.processors

import io.gitlab.arturbosch.detekt.api.DetektVisitor
import io.gitlab.arturbosch.detekt.api.Detektion
import io.gitlab.arturbosch.detekt.api.FileProcessListener
import org.jetbrains.kotlin.com.intellij.openapi.util.Key
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.resolve.BindingContext

class QualifiedNameProcessor : FileProcessListener {

    override fun onProcess(file: KtFile, bindingContext: BindingContext) {
        val packageName = file.packageFqName.asString()
        val nameVisitor = ClassNameVisitor()
        file.accept(nameVisitor)
        val fqNames = nameVisitor.names
            .mapTo(HashSet()) { "$packageName.$it" }
        file.putUserData(fqNamesKey, fqNames)
    }

    override fun onFinish(files: List<KtFile>, result: Detektion, bindingContext: BindingContext) {
        val fqNames = files
            .mapNotNull { it.getUserData(fqNamesKey) }
            .flatMapTo(HashSet()) { it }
        result.putUserData(fqNamesKey, fqNames)
    }

    class ClassNameVisitor : DetektVisitor() {

        val names = mutableSetOf<String>()

        override fun visitClassOrObject(classOrObject: KtClassOrObject) {
            names.add(classOrObject.nameAsSafeName.asString())
        }
    }
}

val fqNamesKey: Key<Set<String>> = Key.create("FQNames")
