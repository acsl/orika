package ma.glasnost.orika.converter.builtin;

import ma.glasnost.orika.metadata.Type;


/**
 * @author gdelaet
 * @version $Id$
 */
public class ObjectToIntegerConverter extends BuiltinCustomConverter<Object,Integer>
{
  /**
   * use this converter only if the raw sourceType (during source code generation) `is exactly Object.class
   * Otherwise this converter may be applied unwanted (eg when mapping String -> Integer which should be done by FromStringConverter)
   * @param sourceType
   * @param destinationType
   * @return
   */
  public boolean canConvert(Type<?> sourceType, Type<?> destinationType)
  {
    return sourceType.getRawType().equals(Object.class) && this.destinationType.equals(destinationType);
  }

  public Integer convert(Object source, Type<? extends Integer> destinationType)
  {
    if (source == null)
      return null;
    else if (getBType().getRawType().isInstance(source))
      return (Integer)source;
    else
      throw new RuntimeException("unable to convert object of type " + source.getClass() + " to " + getBType());
  }
}
