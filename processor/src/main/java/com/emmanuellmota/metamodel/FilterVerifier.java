package com.emmanuellmota.metamodel;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@SupportedAnnotationTypes({"com.emmanuellmota.metamodel.Filterable"})
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class FilterVerifier extends AbstractProcessor {

    public static final String ERROR_MESSAGE = "wrong use of annotation: must be used on class or abstract class.";
    private TypeMirror generateModel;
    private Types      typeUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        Elements elementUtils = processingEnv.getElementUtils();
        generateModel = elementUtils.getTypeElement(Filterable.class.getName()).asType();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(Filterable.class).forEach(this::verifyNotAnAnnotation);
        return false;
    }

    private void verifyNotAnAnnotation(Element element) {
        if (element.getKind() == ElementKind.ANNOTATION_TYPE) {
            compilerErrorMessage(element);
        }
    }

    private void compilerErrorMessage(Element element) {
        AnnotationMirror annotation = element.getAnnotationMirrors().stream()
                                             .filter(m -> typeUtils.isSameType(m.getAnnotationType(), generateModel))
                                             .findFirst()
                                             .orElseThrow(() -> new RuntimeException("internal compiler error"));
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, ERROR_MESSAGE, element, annotation);
    }
}
