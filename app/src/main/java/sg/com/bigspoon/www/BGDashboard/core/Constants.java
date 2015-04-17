

package sg.com.bigspoon.www.BGDashboard.core;

/**
 * Bootstrap constants
 */
public final class Constants {
    private Constants() {}

    public static final class Auth {
        private Auth() {}
        public static final String PREFS_NAME = "MyPrefsFile";
        /**
         * Account type id
         */
        public static final String BOOTSTRAP_ACCOUNT_TYPE = "sg.com.bigspoon.www.BGDashboard";

        /**
         * Account name
         */
        public static final String BOOTSTRAP_ACCOUNT_NAME = "BGDashboard";

        /**
         * Provider id
         */
        public static final String BOOTSTRAP_PROVIDER_AUTHORITY = "sg.com.bigspoon.www.BGDashboard.sync";

        /**
         * Auth token type
         */
        public static final String AUTHTOKEN_TYPE = BOOTSTRAP_ACCOUNT_TYPE;
    }

    /**
     * All HTTP is done through a REST style API built for demonstration purposes on Parse.com
     * Thanks to the nice people at Parse for creating such a nice system for us to use for bootstrap!
     */
    public static final class Http {
        private Http() {}


        /**
         * Base URL for all requests
         */
        public static final String URL_BASE = "https://api.parse.com";


        /**
         * Authentication URL
         */
        public static final String URL_AUTH_FRAG = "/1/login";
        public static final String URL_AUTH = URL_BASE + URL_AUTH_FRAG;

        /**
         * List Users URL
         */
        public static final String URL_USERS_FRAG =  "/1/users";
        public static final String URL_USERS = URL_BASE + URL_USERS_FRAG;


        /**
         * List News URL
         */
        public static final String URL_NEWS_FRAG = "/1/classes/News";
        public static final String URL_NEWS = URL_BASE + URL_NEWS_FRAG;


        /**
         * List Checkin's URL
         */
        public static final String URL_CHECKINS_FRAG = "/1/classes/Locations";
        public static final String URL_CHECKINS = URL_BASE + URL_CHECKINS_FRAG;

        /**
         * PARAMS for auth
         */
        public static final String PARAM_USERNAME = "username";
        public static final String PARAM_PASSWORD = "password";


        public static final String PARSE_APP_ID = "zHb2bVia6kgilYRWWdmTiEJooYA17NnkBSUVsr4H";
        public static final String PARSE_REST_API_KEY = "N2kCY1T3t3Jfhf9zpJ5MCURn3b25UpACILhnf5u9";
        public static final String HEADER_PARSE_REST_API_KEY = "X-Parse-REST-API-Key";
        public static final String HEADER_PARSE_APP_ID = "X-Parse-Application-Id";
        public static final String CONTENT_TYPE_JSON = "application/json";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String SESSION_TOKEN = "sessionToken";

        public static final String BASE_URL =  "http://bigspoon.biz/"; // public static final String BASE_URL =  "http://54.255.0.38/";
        public static final String USER_SIGNUP =  "http://bigspoon.biz/api/v1/user";
        public static final String USER_LOGIN =  "http://bigspoon.biz/api/v1/login";
        public static final String USER_LOGIN_WITH_FB =  "http://bigspoon.biz/api/v1/fblogin";
        public static final String LIST_OUTLETS =  "http://bigspoon.biz/api/v1/outlets";
        public static final String REQUEST_URL =  "http://bigspoon.biz/api/v1/request";
        public static final String PROFILE_URL =  "http://bigspoon.biz/api/v1/profile";
        public static final String ORDER_URL =  "http://bigspoon.biz/api/v1/meal";
        public static final String CLEAR_BILL_URL =  "http://bigspoon.biz/api/v1/clearbill";
        public static final String BILL_URL =  "http://bigspoon.biz/api/v1/askbill";
        public static final String RATING_URL =  "http://bigspoon.biz/api/v1/rating";
        public static final String FEEDBACK_URL =  "http://bigspoon.biz/api/v1/review";
        public static final String DISH_CATEGORY_URL =  "http://bigspoon.biz/api/v1/categories";
        public static final String ORDER_HISTORY_URL =  "http://bigspoon.biz/api/v1/mealhistory";
        public static final String SOCKET_URL =  "bigspoon.biz";

        public static final int PORT = 8000;


    }


    public static final class Extra {
        private Extra() {}

        public static final String NEWS_ITEM = "news_item";

        public static final String USER = "user";

    }

    public static final class Intent {
        private Intent() {}

        /**
         * Action prefix for all intents created
         */
        public static final String INTENT_PREFIX = "sg.com.bigspoon.www.BGDashboard.";

    }

    public static class Notification {
        private Notification() {
        }

        public static final int TIMER_NOTIFICATION_ID = 1000; // Why 1000? Why not? :)
    }

}


