package mobi.tarantino.stub.auto;

public class Consts {


    public enum ScreenComponents {
        DASHBOARD, CARS, DRIVERS, SESSION, FINES, FINE_PAYMENT, NOTIFICATIONS
    }

    public class Key {
        public static final String PHONE = "phone";
        public static final String CODE = "code";
        public static final String TOKEN = "token";
        public static final String ARTICLE = "article";
        public static final String ARTICLE_TYPE = "article_type";
        public static final String DRIVER_LICENSE = "driver_license";
        public static final String VIEW_STATE = "view_state";
        public static final String EXCEPTION = "exception";
        public static final String POOL_TO_REFRESH = "pool_to_refresh";
        public static final String TRAFFIC_FINE = "fine";
        public static final String TRAFFIC_FINES = "fines";
        public static final String DOCUMENT_NAME = "docName";
        public static final String RECEIPT = "receipt";
        public static final String CAR_CERTIFICATE = "car_certificate";
        public static final String SEND_PHONE_ANSWER = "send_phone_answer";
        public static final String SEND_SMS_CODE_ANSWER = "send_sms_code_answer";
        public static final String TRANSACTION_STATE = "transaction_state";
        public static final String TITLE = "title";
        public static final String TRANSACTION_RESULT = "TransactionResult";
        public static final String NOTIFICATION_DTO = "notification_dto";
    }

    public class RequestCode {
        public static final int AUTH = 1;
        public static final int PAYMENT_CONFIRMATION = 1;
    }

    public class PreferencesKey {
        public static final String TOKEN = "token";
        public static final String PHONE = "phone";
        public static final String DEVICE_TOKEN = "device_token";
        public static final String DEMO_TOUR_FINISHED = "demo_tour_enabled";
        public static final String LAST_AUTH_SMS_QUERY_TIME = "last_auth_sms_query_time";
        public static final String GA_CLIENT_ID = "ga_client_id";
        public static final String USER_ID = "user_id";
        public static final String FINES_PAY_COUNT = "fines_pay_count";
        public static final String FINES_NOT_PAY_COUNT = "fines_not_pay_count";
        public static final String LAST_FINES_PUSH_DATE = "last_fines_push_date";
        public static final String DRIVERS_COUNT = "drivers_count";
        public static final String CARS_COUNT = "cars_count";
        public static final String NEW_FINES_COUNT = "new_fines_count";
        public static final String GCM_TOKEN = "gcm_token";
        public static final String GCM_IS_REGISTERED_ON_SERVER = "gcm_is_registered_on_server";
    }

    public class Actions {
        public static final String ADD_DRIVER_LICENSE = "add_driver_license";
        public static final String ADD_VEHICLE_CERTIFICATE = "add_vehicle_certificate";
    }

    public class Database {
        public static final int DB_VERSION = 3;
        public static final String DB_NAME = "mobi_db";
    }

    public class Sms {
        public static final String mobi_SMS = "mobiWallet";
    }

    public class Analytics {
        public static final String TRACKER_ID = "UA-82792067-2";

        public static final String OPEN_SCREEN = "openScreen";
        public static final String SIMPLE_EVENT = "simpleEvent";

        public static final String TAG_GA_CLIENT_ID = "gaClientId";
        public static final String TAG_USER_ID = "userId";
        public static final String TAG_FINE_NOT_PAY = "FineNotPay";
        public static final String TAG_FINE_PAY = "FinePay";
        public static final String TAG_CARS = "Cars";
        public static final String TAG_DRIVERS = "Drivers";
        public static final String TAG_SCREEN_NAME = "screenName";
        public static final String TAG_EVENT_CATEGORY = "eventCategory";
        public static final String TAG_EVENT_ACTION = "eventAction";
        public static final String TAG_EVENT_LABEL = "eventLabel";
    }

    public class Notification {
        public static final String CATEGORY_NEW_FINE = "tf_unpaid_fine";
        public static final String CATEGORY_NEW_LAW = "tf_new_law";
        public static final String CATEGORY_NEW_EVENT = "tf_new_event";
        public static final String KEY_CATEGORY = "category";
        public static final String KEY_BODY = "body";
        public static final String KEY_BADGE = "badge";
        public static final int ID_FINE = 1;
        public static final int ID_EVENT = 2;
        public static final int ID_LAW = 3;
        public static final String KEY_URL = "url";
        public static final String KEY_TITLE = "title";
        public static final String KEY_PREVIEW = "preview";
        public static final String KEY_ID = "id";
    }

    public class GCMRegistration {
        public static final String ACTION_REGISTRATION = "registration";
        public static final String ACTION_SAVE_TO_SERVER = "save_to_server";
        public static final String ACTION_LOGOUT = "logout";
    }

    public class Receipt {
        public static final String GET_PAYMENTS_REPORT = "get-payments-report";
        public static final String GET_PAYMENTS_PAGINABLE = "get-payments-paginable";
        public static final String CLIENT_SOFTWARE = "client-software";
        public static final String CLIENT_SOFTWARE_VALUE = "Mobi Auto Android " + BuildConfig
                .VERSION_NAME;
        public static final String TOKEN = "token";
        public static final String TERMINAL_ID = "terminal-id";
        public static final String PERIOD_CUSTOM = "custom";
        public static final String PERIOD_WEEK = "week";

    }
}
