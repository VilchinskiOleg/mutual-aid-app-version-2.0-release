package org.tms.task_executor_service.utils;

public class Constant {

    public static final class Service {

        public static final String MILLI_SEC = "m. sec.";
        public static final double NANO_TO_MILlI_RESOLVER = 0.000001;
    }

    public static class Mongo {

        public static final String INTERNAL_ID = "internalId";
    }

    public static final class Errors {

        public static final String FAIL_PROFILE_CREATING = "fail.profile.creating";
        public static final String TASK_EXECUTION_PROVIDER_NOT_FOUND = "task.execution.provider.not.found";
        public static final String CANNOT_RETRIEVE_COMMAND = "cannot.retrieve.command";
    }
}
