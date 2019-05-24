package timecurvemanager.domain.timecurveObject;

import java.lang.reflect.Field;

public class TimecurveObject {

  private Long id;

  private String tenantId;

  private String tag;

  private String name;

  private TimecurveObjectValueType valueType;

  private String valueTag;

  private String clearingReference;

  public TimecurveObject(Long id, String tenantId, String tag, String name,
      TimecurveObjectValueType valueType, String valueTag, String clearingReference) {
    this.id = id;
    this.tenantId = tenantId;
    this.tag = tag;
    this.name = name;
    this.valueType = valueType;
    this.valueTag = valueTag;
    this.clearingReference = clearingReference;
  }

  public Long getId() {
    return id;
  }

  public String getTenantId() {
    return tenantId;
  }

  public String getTag() {
    return tag;
  }

  public String getName() {
    return name;
  }

  public TimecurveObjectValueType getValueType() {
    return valueType;
  }

  public String getValueTag() {
    return valueTag;
  }

  public String getClearingReference() {
    return clearingReference;
  }

  @Override
  public String toString() {
    return "TimecurveObject{" +
        "id=" + id +
        ", tenantId='" + tenantId + '\'' +
        ", tag='" + tag + '\'' +
        ", name='" + name + '\'' +
        ", valueType=" + valueType +
        ", valueTag='" + valueTag + '\'' +
        ", clearingReference='" + clearingReference + '\'' +
        '}';
  }

  public String validateForNull() throws IllegalArgumentException, IllegalAccessException {
    // Get the attributes of the class
    Field[] fs = this.getClass().getFields();
    for (Field f : fs) {
      // make the attribute accessible if it's a private one
      f.setAccessible(true);

      // Get the value of the attibute of the instance received as parameter
      Object value = f.get(this);
      if (value == null) {
        return f.getName();
      }
    }
    return null;
  }

}

