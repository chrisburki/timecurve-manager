package timecurvemanager.domain.timecurve;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import timecurvemanager.domain.objecttimecurve.ObjectTimecurveRelation;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Timecurve {

  private Long id;

  // @NotNull
  private String tenantId;

  // @NotNull
  private String name;

  private String clearingReference;

  // @NotNull
  private Boolean needBalanceApproval;

  private List<ObjectTimecurveRelation> timecurveRelations = new ArrayList<>();

  public void addTimecurveRelation(ObjectTimecurveRelation relation) {
    this.timecurveRelations.add(relation);
    relation.setTimecurve(this);
  }

  public Timecurve(Long id, String tenantId, String name, String clearingReference,
      Boolean needBalanceApproval) {
    this.id = id;
    this.tenantId = tenantId;
    this.name = name;
    this.clearingReference = clearingReference;
    this.needBalanceApproval = needBalanceApproval;
  }

  public String validateForNull(Timecurve timecurve)
      throws IllegalArgumentException, IllegalAccessException {
    // Get the attributes of the class
    Field[] fs = timecurve.getClass().getFields();
    // System.out.println("Fields CNT: "+ fs.length);
    for (Field f : fs) {
      // make the attribute accessible if it's a private one
      f.setAccessible(true);

      // Get the value of the attribute of the instance received as parameter
      Object value = f.get(timecurve);
      if (value == null || f.getName() == new String("clearingReference")) {
        return f.getName();
      }
    }
    return null;
  }

}

