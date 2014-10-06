package ma.glasnost.orika.converter.builtin;

import ma.glasnost.orika.metadata.Type;

import java.util.Date;


/**
 * @author gdelaet
 * @version $Id$
 */
public class ObjectToDateConverter extends BuiltinCustomConverter<Object,Date>
{
  public Date convert(Object source, Type<? extends Date> destinationType)
  {
    if (source == null)
      return null;
    else if (getBType().getRawType().isInstance(source))
      return (Date)source;
    else if (source instanceof Long)
      return new Date((Long)source);
    else
      throw new RuntimeException("unable to convert object of type " + source.getClass() + " to " + getBType());
  }
}
