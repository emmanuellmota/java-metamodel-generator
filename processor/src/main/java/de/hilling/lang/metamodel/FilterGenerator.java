package de.hilling.lang.metamodel;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * Generate static metamodel for plain java classes.
 */
public class FilterGenerator extends AbstractProcessor {

    private int round;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(Filterable.class).forEach(this::generateMetamodel);
        if (!roundEnv.processingOver() && round > 20) {
            messager().printMessage(Diagnostic.Kind.ERROR, "possible processing loop detected (21)");
        }
        round++;
        return false;
    }

    private void generateMetamodel(Element element) {
        TypeElement typeElement = (TypeElement) element;
        messager().printMessage(Diagnostic.Kind.NOTE, "processing " + element);
        final ClassModel classModel = new ClassHandler(typeElement, processingEnv).invoke();
        writeMetaClass(typeElement, classModel);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Filterable.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_17;
    }

    private void writeMetaClass(TypeElement element, ClassModel classModel) {
        try {
            new FilterableClassWriter(element, classModel).invoke();
        } catch (IOException e) {
            messager().printMessage(Diagnostic.Kind.ERROR, "Writing metaclass failed", element);
        }
    }

    private Messager messager() {
        return processingEnv.getMessager();
    }

}
