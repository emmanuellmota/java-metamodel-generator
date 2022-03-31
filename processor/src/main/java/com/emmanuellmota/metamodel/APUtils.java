package com.emmanuellmota.metamodel;

import java.util.List;

import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;

public class APUtils {

  @FunctionalInterface
  public interface GetClassValue {
    void execute() throws MirroredTypeException, MirroredTypesException;
  }

  public static List<? extends TypeMirror> getTypeMirrorFromAnnotationValue(GetClassValue c) {
    try {
      c.execute();
    } catch (MirroredTypesException ex) {
      return ex.getTypeMirrors();
    }
    return null;
  }

}
