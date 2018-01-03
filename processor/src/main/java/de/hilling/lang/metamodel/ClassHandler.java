package de.hilling.lang.metamodel;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

/**
 * Collect information about the given class.
 */
public class ClassHandler {
    private final TypeElement type;
    private final ClassModel  classModel;

    /**
     * @param element the class to check for attributes.
     * @param processingEnvironment environment.
     */
    ClassHandler(TypeElement element, ProcessingEnvironment processingEnvironment) {
        this.classModel = new ClassModel(processingEnvironment);
        this.type = element;
    }

    /**
     * Collect information about class attributes.
     */
    ClassModel invoke() {
        for (Element enclosed : type.getEnclosedElements()) {
            if (enclosed.getKind() == ElementKind.METHOD) {
                ExecutableElement executable = (ExecutableElement) enclosed;
                if (Utils.isGetter(executable)) {
                    String attributeName = Utils.attributeNameForAccessor(executable);
                    AttributeInfo info = classModel.getInfo(attributeName);
                    info.setType(executable.getReturnType());
                } else if (Utils.isSetter(executable)) {
                    String attributeName = Utils.attributeNameForAccessor(executable);
                    classModel.getInfo(attributeName).setWritable(true);
                }
            }
        }
        return classModel;
    }
}
