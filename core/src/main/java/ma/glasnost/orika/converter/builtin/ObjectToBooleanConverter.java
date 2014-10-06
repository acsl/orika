package ma.glasnost.orika.converter.builtin;

import ma.glasnost.orika.metadata.Type;


/**
 * @author gdelaet
 * @version $Id$
 */
public class ObjectToBooleanConverter extends BuiltinCustomConverter<Object,Boolean>
{
  public Boolean convert(Object source, Type<? extends Boolean> destinationType)
  {
    if (source == null)
      return null;
    else if (getBType().getRawType().isInstance(source))
      return (Boolean)source;
    else
      throw new RuntimeException("unable to convert object of type " + source.getClass() + " to " + getBType());
  }
}
