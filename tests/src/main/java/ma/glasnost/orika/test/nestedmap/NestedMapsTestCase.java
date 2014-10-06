package ma.glasnost.orika.test.nestedmap;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.test.MappingUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @author gdelaet
 * @version $Id$
 */


public class NestedMapsTestCase
{
  public static class ExampleDTO
  {
    private String aString;
    private Integer anInteger;
    private Double aDouble;
    private Boolean aBoolean;

    public ExampleDTO getChildDTO()
    {
      return childDTO;
    }

    public void setChildDTO(ExampleDTO childDTO)
    {
      this.childDTO = childDTO;
    }

    private ExampleDTO childDTO;

    public Date getaDate()
    {
      return aDate;
    }

    public void setaDate(Date aDate)
    {
      this.aDate = aDate;
    }

    private Date aDate;

    public String getaString()
    {
      return aString;
    }

    public void setaString(String aString)
    {
      this.aString = aString;
    }

    public Integer getAnInteger()
    {
      return anInteger;
    }

    public void setAnInteger(Integer anInteger)
    {
      this.anInteger = anInteger;
    }

    public Double getaDouble()
    {
      return aDouble;
    }

    public void setaDouble(Double aDouble)
    {
      this.aDouble = aDouble;
    }

    public Boolean getaBoolean()
    {
      return aBoolean;
    }

    public void setaBoolean(Boolean aBoolean)
    {
      this.aBoolean = aBoolean;
    }
  }

  public static class ExampleDO
  {
    private String aString;
    private Integer anInteger;
    private Double aDouble;
    private Boolean aBoolean;

    public String getaString()
    {
      return aString;
    }

    public void setaString(String aString)
    {
      this.aString = aString;
    }

    public Integer getAnInteger()
    {
      return anInteger;
    }

    public void setAnInteger(Integer anInteger)
    {
      this.anInteger = anInteger;
    }

    public Double getaDouble()
    {
      return aDouble;
    }

    public void setaDouble(Double aDouble)
    {
      this.aDouble = aDouble;
    }

    public Boolean getaBoolean()
    {
      return aBoolean;
    }

    public void setaBoolean(Boolean aBoolean)
    {
      this.aBoolean = aBoolean;
    }
  }


  @Test
  public void testNestedMapToObject()
  {
    HashMap<String,Object> aMap = new HashMap<String,Object>();
    aMap.put("aString", "theString");
    aMap.put("aBoolean", Boolean.TRUE);
    aMap.put("anInteger", 35);
    aMap.put("aDouble",5.0d);
    aMap.put("aDate",new Long(1392076800000l));
    aMap.put("childDTO",aMap.clone());

    MapperFactory mapperFactory = MappingUtil.getMapperFactory();

    mapperFactory.classMap(java.util.Map.class,ExampleDTO.class).mapNulls(true).byDefault().register();
    mapperFactory.classMap(ExampleDTO.class,ExampleDO.class).mapNulls(true).byDefault().register();

    MapperFacade mapper = mapperFactory.getMapperFacade();

    ExampleDTO exampleDTO = new ExampleDTO();
    exampleDTO.setaDouble(2.0d);
    exampleDTO.setAnInteger(30);
    exampleDTO.setaString("foo");
    exampleDTO.setaBoolean(Boolean.FALSE);
    exampleDTO.setaDate(new Date());


    mapper.map(aMap,exampleDTO);

    Assert.assertEquals("theString",exampleDTO.getaString());
    Assert.assertEquals(Boolean.TRUE,exampleDTO.getaBoolean());
    Assert.assertEquals(new Integer(35),exampleDTO.getAnInteger());
    Assert.assertEquals(new Double(5.0d),exampleDTO.getaDouble());


    /*
    mapper.map(personMap,person);

    Assert.assertNotNull(person);
    Assert.assertNotNull(person.getFirstname());
    Assert.assertEquals("John",person.getFirstname());
    Assert.assertNull(person.getLastname());
    Assert.assertNotNull(person.getAddress());
    Assert.assertNotNull(person.getAddress().getStreet());
    Assert.assertEquals("The Street",person.getAddress().getStreet());
    Assert.assertNull(person.getAddress().getHouseNumber());
    Assert.assertNotNull(person.getAddress().getCity());
    Assert.assertNotNull(person.getAddress().getCity().getCityName());
    Assert.assertEquals("The City",person.getAddress().getCity().getCityName());
    Assert.assertNull(person.getAddress().getCity().getPostalCode());


    person = mapper.map(personMap,PersonDO.class);

    Assert.assertNotNull(person);
    Assert.assertNotNull(person.getFirstname());
    Assert.assertEquals("John",person.getFirstname());
    Assert.assertNull(person.getLastname());
    Assert.assertNotNull(person.getAddress());
    Assert.assertNotNull(person.getAddress().getStreet());
    Assert.assertEquals("The Street",person.getAddress().getStreet());
    Assert.assertNull(person.getAddress().getHouseNumber());
    Assert.assertNotNull(person.getAddress().getCity());
    Assert.assertNotNull(person.getAddress().getCity().getCityName());
    Assert.assertEquals("The City",person.getAddress().getCity().getCityName());
    Assert.assertNull(person.getAddress().getCity().getPostalCode());
       */
  }
}
