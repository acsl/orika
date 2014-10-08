package ma.glasnost.orika.converter.builtin;

import ma.glasnost.orika.metadata.Type;


/**
 * @author gdelaet
 * @version $Id$
 */
public class ObjectToDoubleConverter extends BuiltinCustomConverter<Object,Double>
{
  /**
   * use this converter only if the raw sourceType (during source code generation) `is exactly Object.class
   * @param sourceType
   * @param destinationType
   * @return
   */
  public boolean canConvert(Type<?> sourceType, Type<?> destinationType)
  {
    return sourceType.getRawType().equals(Object.class) && this.destinationType.equals(destinationType);
  }

  public Double convert(Object source, Type<? extends Double> destinationType)
  {
    if (source == null)
      return null;
    else if (getBType().getRawType().isInstance(source))
      return (Double)source;
    else if (source instanceof Integer)
      return new Double((Integer)source);
    else
      throw new RuntimeException("unable to convert object of type " + source.getClass() + " to " + getBType());
  }
}
