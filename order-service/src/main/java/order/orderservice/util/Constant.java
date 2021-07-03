package order.orderservice.util;

public class Constant {

    public static class Errors {

        public static final String ORDER_RULE_BY_TYPE = "validation.order.rule.by.type";
        public static final String ORDER_PRIORITY_RULE = "validation.order.priority.rule";
        public static final String ORDER_TITLE_REQUIRED = "validation.order.title.required";
        public static final String ORDER_LOCATION_REQUIRED = "validation.order.location.required";
        public static final String ORDER_OWNER_REQUIRED = "validation.order.owner.required";

        public static final String SEARCH_ORDER_DETAILS_PAGING_RULE = "validation.search.order.details.paging.rule";

        public static final String ORDER_NOT_FUND = "order.not.found";
        public static final String CANNOT_ADD_NEW_CANDIDATE = "cannot.add.new.candidate";
        public static final String CANNOT_CLOSE_ORDER = "cannot.close.order";
        public static final String CANNOT_APPROVE_EXECUTION = "cannot.approve.execution";
        public static final String CANNOT_CHANGE_ORDER_EXECUTOR = "cannot.change.order.executor";
    }

    public static class MongoDb {

        public static final String PRICE = "price";
        public static final String TYPE = "type";
        public static final String PRIORITY = "priority";
        public static final String LOCATION_COUNTRY = "location.country";
        public static final String LOCATION_CITY = "location.city";
        public static final String LOCATION_STREET = "location.street";
        public static final String LOCATION_HOME = "location.home";
        public static final String TITLE = "title";
        public static final String EXECUTOR_MEMBER_ID = "executor.memberId";

        public static final String CANDIDATES = "candidates";
        public static final String MEMBER_ID = "memberId";
    }

    public static class ModelMapper {

        public static final String CREATE = "create";
        public static final String UPDATE = "update";
    }

    public static class Service {

        public static final String SUB_TITLE_REGEXP_PART = ".*";
    }
}
