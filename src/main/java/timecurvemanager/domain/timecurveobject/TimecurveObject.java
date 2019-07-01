package timecurvemanager.domain.timecurveobject;

import java.lang.reflect.Field;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TimecurveObject {

  private Long id;

  // @NotNull
  private String tenantId;

  // @NotNull
  private String tag;

  // @NotNull
  private String name;

  // @NotNull
  private TimecurveObjectValueType valueType;

  // @NotNull
  private String valueTag;

  private String clearingReference;

  // @NotNull
  private Boolean needBalanceApproval;

  public String validateForNull(TimecurveObject timecurveObject)
      throws IllegalArgumentException, IllegalAccessException {
    // Get the attributes of the class
    Field[] fs = timecurveObject.getClass().getFields();
    // System.out.println("Fields CNT: "+ fs.length);
    for (Field f : fs) {
      // make the attribute accessible if it's a private one
      f.setAccessible(true);

      // Get the value of the attribute of the instance received as parameter
      Object value = f.get(timecurveObject);
      if (value == null || f.getName() == new String("clearingReference")) {
        return f.getName();
      }
    }
    return null;
  }

}

