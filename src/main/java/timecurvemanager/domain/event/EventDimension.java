package timecurvemanager.domain.event;

public enum EventDimension {
  SUBLEDGER("subledger", "sublg", "Subledger (Standard)", true),
  CLIENT("client", "clt", "Client", true),
  BLOCKING("blocking", "block", "Blocking", false),
  RECONCILIATION("reconciliation", "recon", "Reconciliation", false);

  private final Object[] values;

  EventDimension(Object... vals) {
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

  public Boolean doClearing() {
    return (Boolean) values[3];
  }

}
