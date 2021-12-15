package event.event_storage_service.util;

public class Constant {

    public static class Errors {

        public static final String EVENT_INCORRECT_ORDER_ID = "event.incorrect.order.id";
    }

    public static class Kafka {

        //default value : 300000
        public static final Integer MAX_POLL_INTERVAL_MS = 3000000;
        //default value : 500
        public static final Integer MAX_POLL_RECORDS = 100;
        //auto.offset.reset config, default value : latest
        public static final String LATEST = "latest";
        public static final String EARLIEST = "earliest";

        public static final String ORDER_TOPIC = "mutual-aid-order-topic";
        public static final String UPDATE_ORDER_EVENT = "update_order_event";
    }
}