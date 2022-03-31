package com.emmanuellmota.metamodel;

import java.io.IOException;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import lombok.Data;

/**
 * Create meta model class for given Type.
 */
class OrderableClassWriter {

    private static final String SUFFIX           = "__Orderable";

    private final TypeElement beanType;
    private final ClassModel  classModel;
    private final String      metaClassName;

    /**
     * Initialize class with {@link TypeElement} and {@link ClassModel} containing attributes.
     * @param beanType the bean class.
     * @param classModel attribute informations about the bean class.
     */
    OrderableClassWriter(TypeElement beanType, ClassModel classModel) {
        this.beanType = beanType;
        this.classModel = classModel;
        metaClassName = beanType.getSimpleName() + SUFFIX;
    }

    /**
     * Create the Metamodel class.
     * @throws IOException if source file cannot be written.
     */
    void invoke() throws IOException {
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(metaClassName)
                                                .addModifiers(Modifier.PUBLIC).addAnnotation(Data.class);
        classModel.attributes().forEach((name, type) -> classBuilder.addField(createFieldSpec(name, type)));

        JavaFile javaFile = JavaFile.builder(ClassName.get(beanType).packageName(), classBuilder.build()).indent("    ")
                                    .build();
        javaFile.writeTo(classModel.getEnvironment().getFiler());
    }

    private FieldSpec createFieldSpec(String attributeName, AttributeInfo info) {
        return FieldSpec.builder(SortField.class, attributeName)
                        .addModifiers(Modifier.PRIVATE).build();
    }

}
