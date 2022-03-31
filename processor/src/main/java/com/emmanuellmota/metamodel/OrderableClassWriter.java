package com.emmanuellmota.metamodel;

import java.io.IOException;
import java.util.Arrays;

import javax.annotation.processing.Generated;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import com.squareup.javapoet.AnnotationSpec;
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
    private final String      orderClassName;

    /**
     * Initialize class with {@link TypeElement} and {@link ClassModel} containing attributes.
     * @param beanType the bean class.
     * @param classModel attribute informations about the bean class.
     */
    OrderableClassWriter(TypeElement beanType, ClassModel classModel, String orderClassName) {
        this.beanType = beanType;
        this.classModel = classModel;
        this.orderClassName = orderClassName;
        metaClassName = beanType.getSimpleName() + SUFFIX;
    }

    /**
     * Create the Metamodel class.
     * @throws IOException if source file cannot be written.
     */
    void invoke() throws IOException {
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(metaClassName)
                                                .addModifiers(Modifier.PUBLIC)
                                                .addAnnotation(Data.class)
                                                .addAnnotation(AnnotationSpec.builder(Generated.class).addMember("value", "$S", OrderGenerator.class.getName()).build());
        classModel.attributes().forEach((name, type) -> classBuilder.addField(createFieldSpec(name, type)));

        JavaFile javaFile = JavaFile.builder(ClassName.get(beanType).packageName(), classBuilder.build()).indent("    ")
                                    .build();
        javaFile.writeTo(classModel.getEnvironment().getFiler());
    }

    private FieldSpec createFieldSpec(String attributeName, AttributeInfo info) {
        String[] classArr = orderClassName.split("\\.");
        return FieldSpec.builder(ClassName.get(String.join(".", Arrays.copyOf(classArr, classArr.length - 1)), classArr[classArr.length - 1]), attributeName)
                        .addModifiers(Modifier.PRIVATE).build();
    }

}
