package com.emmanuellmota.metamodel;

import java.io.IOException;
import java.util.Arrays;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

/**
 * Create meta model class for given Type.
 */
class FilterableClassWriter {

    private static final String SUFFIX           = "__Filterable";

    private final TypeElement beanType;
    private final ClassModel  classModel;
    private final String      metaClassName;
    private final ClassName   filterClassName;

    /**
     * Initialize class with {@link TypeElement} and {@link ClassModel} containing attributes.
     * @param beanType the bean class.
     * @param classModel attribute informations about the bean class.
     */
    FilterableClassWriter(TypeElement beanType, ClassModel classModel, String packageName) {
        this.beanType = beanType;
        this.classModel = classModel;
        
        String[] classArr = packageName.split("\\.");
        this.filterClassName = ClassName.get(String.join(".", Arrays.copyOf(classArr, classArr.length - 1)),
                classArr[classArr.length - 1]);

        metaClassName = beanType.getSimpleName() + SUFFIX;
    }

    /**
     * Create the Metamodel class.
     * @throws IOException if source file cannot be written.
     */
    void invoke() throws IOException {
        /* TypeSpec.Builder classBuilder = TypeSpec.classBuilder(metaClassName)
                                                .addModifiers(Modifier.PUBLIC)
                                                .addAnnotation(Data.class);
        classModel.attributes().forEach((name, type) -> classBuilder.addField(createFieldSpec(name, type)));

        JavaFile javaFile = JavaFile.builder(ClassName.get(beanType).packageName(), classBuilder.build()).indent("    ")
                                    .addFileComment("Generated code. Do not modify!")
                                    .build();
        javaFile.writeTo(classModel.getEnvironment().getFiler()); */




        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(metaClassName)
                .addModifiers(Modifier.PUBLIC);
        classModel.attributes().forEach((name, type) -> classBuilder.addField(createFieldSpec(name, type)));

        classBuilder.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .build());

        classModel.attributes()
                .forEach((name, type) -> classBuilder.addMethod(
                        MethodSpec.methodBuilder("get" + name.substring(0, 1).toUpperCase() + name.substring(1))
                                .addModifiers(Modifier.PUBLIC)
                                .returns(filterClassName)
                                .addStatement("return $L", name)
                                .build()));

        classModel.attributes()
                .forEach((name, type) -> classBuilder.addMethod(
                        MethodSpec.methodBuilder("set" + name.substring(0, 1).toUpperCase() + name.substring(1))
                                .addModifiers(Modifier.PUBLIC)
                                .returns(void.class)
                                .addParameter(ParameterizedTypeName.get(filterClassName, Utils.getAttributeTypeName(type)), name)
                                .addStatement("this.$1N = $1N", name)
                                .build()));

        JavaFile javaFile = JavaFile.builder(ClassName.get(beanType).packageName(), classBuilder.build()).indent("    ")
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
        return ParameterizedTypeName.get(filterClassName, attributeTypeName);
    }

}
