package timecurvemanager.domain.event;

import timecurvemanager.domain.event.publish.EventPublish;

public interface EventMessaging {

  /*Publish Event to message brok*/
  void sendEvent(EventPublish event);

}
