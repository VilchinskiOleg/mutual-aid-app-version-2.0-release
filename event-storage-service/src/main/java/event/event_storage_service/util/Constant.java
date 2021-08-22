package event.event_storage_service.util;

public class Constant {

    public static class Errors {

        public static final String EVENT_INCORRECT_ORDER_ID = "event.incorrect.order.id";
    }

    public static class Kafka {

        //default value : 300000
        public static final Integer MAX_POLL_INTERVAL_MS = 900000;
        //default value : 500
        public static final Integer MAX_POLL_RECORDS = 100;
    }
}
