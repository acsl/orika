package ma.glasnost.orika.impl.generator.specification;

import ma.glasnost.orika.converter.builtin.CopyByReferenceConverter;
import ma.glasnost.orika.impl.generator.SourceCodeContext;
import ma.glasnost.orika.impl.generator.VariableRef;
import ma.glasnost.orika.metadata.FieldMap;

import java.util.Map;

import static java.lang.String.*;
import static ma.glasnost.orika.impl.generator.SourceCodeContext.*;


/**
 * This is a custom Specification copied from ma.glasnost.orika.impl.generator.specification.Convert
 * and modified in order to generate different source code when the source object of the mapping is assignable
 * to java.util.Map. In that case an extra check in the generated related to the mapping of properties from the source
 * Map to the target Object is inserted. The code checks if their is actually a key in the source Map
 * when performing get(key) on the map has returned null. If there isn't
 * no setter for the corresponding property is called on the target Object. If there is, the setter is called with
 * set(null). This is important when the target object is a DO to differentiate between properties which are deliberately
 * set to null and properties which are not sent in the the source map. In this way it is possible to use a source Map
 * to specify only 'changed' properties where changed could also mean set to null.
 * @author gdelaet
 * @version $Id$
 */

public class MapPropertyConvert extends AbstractSpecification
{

  public boolean appliesTo(FieldMap fieldMap) {
    return fieldMap.getSource().isMapKey() &&
        (fieldMap.getConverterId() != null || mapperFactory.getConverterFactory().canConvert(fieldMap.getAType(), fieldMap.getBType()));
  }

  public String generateEqualityTestCode(FieldMap fieldMap, VariableRef source, VariableRef destination, SourceCodeContext code) {

    if (source.getConverter() instanceof CopyByReferenceConverter) {
            /*
             * We apply a shortcut here if we know it's an immutable conversion --
             * no reason to pass it through an extra function call just to return
             * back the original item again.
             */
      if (destination.type().isPrimitive() && source.type().isPrimitive()) {
        return format("(%s == %s)", destination, source);
      } else if (destination.type().isPrimitive()) {
        return format("(%s != null && %s == %s.%sValue())", source, destination, source,  source.type().getName());
      } else if (source.type().isPrimitive()) {
        return format("(%s != null && %s.%sValue() == %s)", destination, destination, destination.type().getPrimitiveType().getName(), source);
      } else {
        return format("(%s != null && %s.equals(%s))", source, source, destination);
      }

    } else {

      if (destination.type().isPrimitive()) {
        String wrapper = source.asWrapper();
        String wrapperType = destination.type().getWrapperType().getSimpleName();
        String primitive = destination.type().getName();
        return format("(%s == ((%s)%s.convert(%s, %s)).%sValue())", destination, wrapperType, code.usedConverter(source.getConverter()), wrapper, code.usedType(destination), primitive);
      } else if (source.type().isPrimitive()) {
        return format("(%s == %s.convert(%s, %s))", destination.asWrapper(), code.usedConverter(source.getConverter()), source.asWrapper(), code.usedType(destination));
      } else {
        return format("(%s != null && %s.equals(%s.convert(%s, %s)))", destination, destination, code.usedConverter(source.getConverter()), source.asWrapper(), code.usedType(destination));
      }
    }
  }

  public String generateMappingCode(FieldMap fieldMap, VariableRef source, VariableRef destination, SourceCodeContext code) {

    String assureInstanceExists = destination.isNestedProperty() ? (statement(code.assureInstanceExists(destination, source)) + "\n") : "";

    String statement;
    boolean canHandleNulls;
    if (source.getConverter() instanceof CopyByReferenceConverter) {
      if (code.isDebugEnabled()) {
        code.debugField(fieldMap, "copying " + source.type() + " by reference");
      }
      statement = destination.assignIfPossible(source);
      canHandleNulls = true;
    } else {
      if (code.isDebugEnabled()) {
        code.debugField(fieldMap, "converting using " + source.getConverter());
      }
      statement = destination.assignIfPossible("%s.convert(%s, %s)", code.usedConverter(source.getConverter()), source.asWrapper(), code.usedType(destination));
      canHandleNulls = false;
    }


    boolean shouldSetNull = shouldMapNulls(fieldMap, code) && !destination.isPrimitive();
    String destinationNotNull = destination.ifPathNotNull();

    if (!source.isNullPossible() || (canHandleNulls && shouldSetNull && "".equals(destinationNotNull)))
    {
      return statement(statement);
    }
    else
    {
      // here we construct the extra if clause that checks for the presence of the key in the map
      String ifContainsKeyInSourceMap = "";
      if (fieldMap.getAType().getRawType().isAssignableFrom(Map.class))
      {
        code.debugField(fieldMap,"adding containsKey if clause");
        ifContainsKeyInSourceMap = " if (source.containsKey(\"" + fieldMap.getSource().getName() + "\"))";
      }

      String elseSetNull = shouldSetNull ? (" else "+ destinationNotNull +"{ \n" + ifContainsKeyInSourceMap + "{\n  " + destination.assignIfPossible("null")) + ";\n  } \n }" : "";

      return statement(source.ifNotNull() + "{ \n" + assureInstanceExists + statement) + "\n}" + elseSetNull;
    }
  }
}

