package ma.glasnost.orika.test.nestedmap;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.test.MappingUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


/**
 * @author gdelaet
 * @version $Id$
 */


public class NestedMapsTestCase
{
  public static class CityDO
  {
    private Integer postalCode = Integer.MAX_VALUE;
    private String city ="notNull";

    public Integer getPostalCode(){return postalCode;}
    public void setPostalCode(Integer postalCode){this.postalCode = postalCode;}
    public String getCity(){return city;}
    public void setCity(String city){this.city = city;}
  }

  public static class AddressDO
  {
    private String street = "notNull";
    private Integer houseNumber = Integer.MAX_VALUE;
    private CityDO city;

    public String getStreet(){return street;}
    public void setStreet(String street){this.street = street;}
    public Integer getHouseNumber(){return houseNumber;}
    public void setHouseNumber(Integer houseNumber){this.houseNumber = houseNumber;}
    public CityDO getCity(){return city;}
    public void setCity(CityDO city){this.city = city;}
  }

  public static class PersonDO
  {
    private String firstname = "notNull";
    private String lastname = "notNull";
    private AddressDO address;

    public String getFirstname(){return firstname;}
    public void setFirstname(String firstname){this.firstname = firstname;}
    public String getLastname(){return lastname;}
    public void setLastname(String lastname){this.lastname = lastname;}
    public AddressDO getAddress(){return address;}
    public void setAddress(AddressDO address){this.address = address;}
  }

  @Test
  public void testNestedMapToObject()
  {
    Map<String,Object> personMap = new HashMap<String,Object>();
    personMap.put("firstname", "John");
    personMap.put("lastname", null);

    Map<String,Object> addressMap = new HashMap<String,Object>();
    addressMap.put("street", "The Street");
    addressMap.put("houseNumber", null);
    personMap.put("address", addressMap);

    Map<String,Object> cityMap = new HashMap<String,Object>();
    cityMap.put("city", "The City");
    cityMap.put("postcalCode", null);
    addressMap.put("city", cityMap);


    MapperFactory mapperFactory = MappingUtil.getMapperFactory();

    mapperFactory.classMap(java.util.Map.class, PersonDO.class).mapNulls(true).byDefault().register();
    mapperFactory.classMap(java.util.Map.class,AddressDO.class).mapNulls(true).byDefault().register();
    mapperFactory.classMap(java.util.Map.class,CityDO.class).mapNulls(true).byDefault().register();



    MapperFacade mapper = mapperFactory.getMapperFacade();

    PersonDO person = new PersonDO();
    person.setLastname("notNull");

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
    Assert.assertNotNull(person.getAddress().getCity().getCity());
    Assert.assertEquals("The City",person.getAddress().getCity().getCity());
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
    Assert.assertNotNull(person.getAddress().getCity().getCity());
    Assert.assertEquals("The City",person.getAddress().getCity().getCity());
    Assert.assertNull(person.getAddress().getCity().getPostalCode());

  }
}
