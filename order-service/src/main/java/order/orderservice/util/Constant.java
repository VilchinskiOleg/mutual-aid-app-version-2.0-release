package order.orderservice.util;

public class Constant {

    public static class Errors {

        public static final String ORDER_RULE_BY_TYPE = "validation.order.rule.by.type";
        public static final String ORDER_TITLE_REQUIRED = "validation.order.title.required";
        public static final String ORDER_LOCATION_REQUIRED = "validation.order.location.required";
        public static final String ORDER_TYPE_RULE = "validation.order.type.rule";
        public static final String ORDER_OWNER_REQUIRED = "validation.order.owner.required";

        public static final String SEARCH_ORDER_DETAILS_PAGING_RULE = "search.order.details.paging.rule";
    }

    public static class MongoDb {

        public static final String PRICE = "price";
        public static final String TYPE = "type";
        public static final String PRIORITY = "priority";
        public static final String LOCATION_COUNTRY = "location.country";
        public static final String LOCATION_CITY = "location.city";
        public static final String LOCATION_STREET = "location.street";
        public static final String LOCATION_HOME = "location.home";
    }
}
