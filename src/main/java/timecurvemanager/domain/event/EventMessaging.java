package timecurvemanager.domain.event;

import timecurvemanager.domain.event.messaging.EventMessage;

public interface EventMessaging {

  /*Publish Event to message broker*/
  void sendEvent(EventMessage event);

}
