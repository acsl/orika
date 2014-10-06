package ma.glasnost.orika.converter.builtin;

import ma.glasnost.orika.metadata.Type;


/**
 * @author gdelaet
 * @version $Id$
 */
public class ObjectToDoubleConverter extends BuiltinCustomConverter<Object,Double>
{
  public Double convert(Object source, Type<? extends Double> destinationType)
  {
    if (source == null)
      return null;
    else if (getBType().getRawType().isInstance(source))
      return (Double)source;
    else
      throw new RuntimeException("unable to convert object of type " + source.getClass() + " to " + getBType());
  }
}
