package com.emmanuellmota.metamodel;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.processing.Generated;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import lombok.Data;

/**
 * Create meta model class for given Type.
 */
class FilterableClassWriter {

    private static final String SUFFIX           = "__Filterable";

    private final TypeElement beanType;
    private final ClassModel  classModel;
    private final String      metaClassName;
    private final String      filterClassName;

    /**
     * Initialize class with {@link TypeElement} and {@link ClassModel} containing attributes.
     * @param beanType the bean class.
     * @param classModel attribute informations about the bean class.
     */
    FilterableClassWriter(TypeElement beanType, ClassModel classModel, String filterClassName) {
        this.beanType = beanType;
        this.classModel = classModel;
        this.filterClassName = filterClassName;
        metaClassName = beanType.getSimpleName() + SUFFIX;
    }

    /**
     * Create the Metamodel class.
     * @throws IOException if source file cannot be written.
     */
    void invoke() throws IOException {
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(metaClassName)
                                                .addModifiers(Modifier.PUBLIC)
                                                .addAnnotation(Data.class);
        classModel.attributes().forEach((name, type) -> classBuilder.addField(createFieldSpec(name, type)));

        JavaFile javaFile = JavaFile.builder(ClassName.get(beanType).packageName(), classBuilder.build()).indent("    ")
                                    .addFileComment("Generated code. Do not modify!")
                                    .build();
        javaFile.writeTo(classModel.getEnvironment().getFiler());
    }

    private FieldSpec createFieldSpec(String attributeName, AttributeInfo info) {
        final ParameterizedTypeName fieldType = declarationTypeName(info);
        return FieldSpec.builder(fieldType, attributeName)
                        .addModifiers(Modifier.PRIVATE).build();
    }

    private ParameterizedTypeName declarationTypeName(AttributeInfo info) {
        final TypeName attributeTypeName = Utils.getAttributeTypeName(info);
        String[] classArr = filterClassName.split("\\.");
        return ParameterizedTypeName.get(ClassName.get(String.join(".", Arrays.copyOf(classArr, classArr.length - 1)), classArr[classArr.length - 1]), attributeTypeName);
    }

}
