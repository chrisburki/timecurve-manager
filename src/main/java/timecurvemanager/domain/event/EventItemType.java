package timecurvemanager.domain.event;

public enum EventItemType {
  BASIC("subledger", "sublg", "Sub Ledger", false, true),
  IFRS("blocking", "block", "Blocking", true, false),
  AVERAGE("average", "avg", "Average Book Values", false, false),
  FIFO("fifo", "fifo", "First In First Out", true, false);

  private final Object[] values;

  EventItemType(Object... vals) {
    values = vals;
  }

  public String intl_id() {
    return (String) values[0];
  }

  public String abbreviation() {
    return (String) values[1];
  }

  public String description() {
    return (String) values[2];
  }

  public Boolean hasItems() {
    return (Boolean) values[3];
  }

  public Boolean buildApprovedBalance() {
    return (Boolean) values[4];
  }
}
