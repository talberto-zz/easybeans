package com.github.talberto.easybeans.gen;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;

public class MetaType {
  private static final Function<Type, String> sTypeToTypename = new Function<Type, String>() {
    @Override
    public String apply(Type pType) {
      return getName(pType);
    }
  };

  private static final Function<Type, Set<MetaImport>> sTypeToMetaimport = new Function<Type, Set<MetaImport>>() {
    @Override
    public Set<MetaImport> apply(Type pType) {
      return getImports(pType);
    }
  };
  
  protected String mName;
  protected Set<MetaImport> mImports; 
  
  public MetaType(String pName, Set<MetaImport> pImports) {
    mName = pName;
    mImports = pImports;
  }
  
  /**
   * @return the name
   */
  public String getName() {
    return mName;
  }

  public static MetaType of(TypeToken<?> pTypeToken) {
    Type type = pTypeToken.getType();
    String name = getName(type);
    Set<MetaImport> imports = getImports(type);
    
    return new MetaType(name, imports);
  }

  private static Set<MetaImport> getImports(Type pType) {
    Set<MetaImport> imports = Sets.newHashSet();
    
    if(pType instanceof Class) {
      Class<?> klazz = (Class<?>) pType;
      imports.addAll(getImportsFromClass(klazz));
    } else if(pType instanceof ParameterizedType) {
      ParameterizedType paramType = (ParameterizedType) pType;
      imports.addAll(getImportsFromParameterizedType(paramType));
    } else if(pType instanceof TypeVariable) {
      TypeVariable<?> typeVariable = (TypeVariable<?>) pType;
      imports.addAll(getImportsFromTypeVariable(typeVariable));
    }
    
    return imports;
  }

  private static Set<MetaImport> getImportsFromTypeVariable(TypeVariable<?> pTypeVariable) {
    throw new UnsupportedOperationException();
  }

  private static Set<MetaImport> getImportsFromParameterizedType(ParameterizedType pParamType) {
    Type rawType = pParamType.getRawType();
    Set<MetaImport> rawTypeImports = getImports(rawType);
    List<Type> actualParamTypes = Arrays.asList(pParamType.getActualTypeArguments());
    List<Set<MetaImport>> actualParamSetImports = Lists.transform(actualParamTypes, sTypeToMetaimport);
    Set<MetaImport> resultingImports = rawTypeImports;
    
    for(Set<MetaImport> imports : actualParamSetImports) {
      resultingImports.addAll(imports);
    }
    
    return resultingImports;
  }

  private static Set<MetaImport> getImportsFromClass(Class<?> pClass) {
    return Sets.newHashSet(new MetaImport(pClass.getName()));
  }

  static protected String getName(Type pType) {
    String name = "";
    
    if(pType instanceof Class) {
      Class<?> klazz = (Class<?>) pType;
      name = getNameFromClass(klazz);
    } else if(pType instanceof ParameterizedType) {
      ParameterizedType paramType = (ParameterizedType) pType;
      name = getNameFromParameterizedType(paramType);
    } else if(pType instanceof TypeVariable) {
      TypeVariable<?> typeVariable = (TypeVariable<?>) pType;
      name = getNameFromTypeVariable(typeVariable);
    }
    return name;
  }

  private static String getNameFromTypeVariable(TypeVariable<?> pTypeVariable) {
    throw new UnsupportedOperationException();
  }

  private static String getNameFromClass(Class<?> klazz) {
    String name;
    name = klazz.getSimpleName();
    return name;
  }

  private static String getNameFromParameterizedType(ParameterizedType pParamType) {
    Type rawType = pParamType.getRawType();
    String rawTypeName = getName(rawType);
    List<Type> actualParamTypes = Arrays.asList(pParamType.getActualTypeArguments());
    List<String> actualParamNames = Lists.transform(actualParamTypes, sTypeToTypename);
    return String.format("%s<%s>", rawTypeName, Joiner.on(",").join(actualParamNames));
  }
  
  public Set<MetaImport> getImports() {
    return mImports;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "MetaType [mName=" + mName + ", mImports=" + mImports + "]";
  } 
}
