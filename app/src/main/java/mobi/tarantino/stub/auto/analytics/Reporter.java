package mobi.tarantino.stub.auto.analytics;

public interface Reporter {
    //    Screen tags
    String SCREEN_UNKNOWN = "service";
    String SCREEN_INPUT_PHONE = "input_phone_screen";
    String SCREEN_INPUT_SMS_CODE = "input_sms_code_screen";
    String SCREEN_DASH_BOARD = "dashboard_screen";
    String SCREEN_SERVICES = "services_screen";
    String SCREEN_FINES_PAYMENT = "fines_payment_screen";
    String SCREEN_FINES_PAY = "fines_pay_screen";
    String SCREEN_PAID_FINE = "paid_fine_screen";
    String SCREEN_FINES_NOT_PAY = "fines_not_pay_screen";
    String SCREEN_TRAFFIC_LAWS = "traffic_laws_screen";
    String SCREEN_FINES_LIST = "fines_list_screen";
    String SCREEN_DRIVER_ASSISTANCE = "driver_assistance_screen";
    String SCREEN_PROFILE = "profile_screen";
    String SCREEN_DRIVER_EDITOR = "driver_editor_screen";
    String SCREEN_DRIVER_LIST = "driver_list_screen";
    String SCREEN_CAR_EDITOR = "car_editor_screen";
    String SCREEN_CAR_LIST = "car_list_screen";
    String SCREEN_RECEIPT = "receipt_screen";

    //    Category tags
    String CATEGORY_AUTH = "Auth";
    String CATEGORY_LINK_CLICK = "LinkClick";
    String CATEGORY_ERROR = "Error";
    String CATEGORY_MESSAGE = "Message";
    String CATEGORY_NOTIFICATION = "Уведомление";
    String CATEGORY_MENU = "Menu";
    String CATEGORY_WIDGET = "Widget";
    String CATEGORY_CALL = "Call";
    String CATEGORY_ADD_DOCUMENTS = "CATEGORY_ADD_DOCUMENTS";
    String CATEGORY_ADD_AUTO = "AddAuto";
    String CATEGORY_DELETE_AUTO = "DeleteAuto";
    String CATEGORY_ADD_DRIVER = "AddDriver";
    String CATEGORY_DELETE_DRIVER = "DeleteDriver";
    String CATEGORY_NEW_FINE_SMS = "SMS о новых штрафах";
    String CATEGORY_FINES = "Штрафы";
    String CATEGORY_FUEL_INFO = "Топливо";
    String CATEGORY_PAYMENT = "Payment";
    String CATEGORY_RECEIPT = "Квитанция";
    String CATEGORY_TRAFFICLAWS = "ПДД";
    String CATEGORY_QUIZ = "Викторина";
    String CATEGORY_EXIT = "Выход";
    String CATEGORY_HELP = "Помощь";

    //    Action tags
    String ACTION_NEXT = "Далее";
    String ACTION_PHONE_NEEDED = "Зачем приложению Ваш номер?";
    String ACTION_OFFER = "Оферты";
    String ACTION_RESEND_CODE = "Выслать код снова";
    String ACTION_ERROR = "Error";
    String ACTION_SUCCESS = "Success";
    String ACTION_BUTTON_CLICK = "ButtonClick";
    String ACTION_REFRESH = "refresh";
    String ACTION_SUCCESS_ADD_AUTO = "SuccessAddAuto";
    String ACTION_SUCCESS_DELETE_AUTO = "SuccessDeleteAuto";
    String ACTION_SUCCESS_DRIVER = "SuccessDriver";
    String ACTION_SAVE_IN_ALBUM = "Сохранить в фотоальбом";
    String ACTION_SEND_TO_EMAIL = "Отправить на эл. почту";
    String ACTION_CLICK = "Click";
    String ACTION_FOCUS = "Focus";

    //    Label tags
    String LABEL_SHOW = "Show";
    String LABEL_CLICK = "Click";
    String LABEL_SEND = "Send";
    String LABEL_USER_ID = "user_id";
    String LABEL_EMPTY = "";

    //    Dashboard menu names
    String MENU_SERVICES = "menu_services";
    String MENU_FINES = "menu_fines";
    String MENU_TRAFFIC_LAWS = "menu_traffic_laws";
    String MENU_DRIVER_ASSISTANCE = "menu_driver_assistance";
    String MENU_PROFILE = "menu_profile";

    //    Fines tabs
    String FINES_TAB_UNPAID = "fines_tab_unpaid";
    String FINES_TAB_PAID = "fines_tab_paid";

    void authSuccess(String screenName);

    void authFailed(String screenInputSmsCode, String description);

    void phoneNextButtonClick(String screenName);

    void whatPhoneNumberClick(String screenName);

    void offerClick(String screenName);

    void retrySendCodeClick(String screenName);

    void errorEnterPhone(String screenName, String description);

    void errorEnterCode(String screenName, String description);

    void commonError(String screenName, String errorDescription);

    void toastEvent(String screenName, String text);

    void showNotificationEvent(String screenName, String description);

    void notificationUserEvent(String screenName, String notificationDescription, String
            buttonName);

    void menuClickEvent(String screenName, String menuName);

    void widgetEvent(String screenName, String eventAction);

    void refreshWidgetEvent(String screenName, String widgetCategory);

    void phoneCallEvent(String screenName, String serviceName);

    void addCarEvent(String screenName);

    void addCarSuccessEvent(String screenName);

    void removeCarSuccess(String screenName);

    void addDriverEvent(String screenName);

    void addDriverSuccessEvent(String screenName);

    void removeDriverSuccess(String screenName);

    void finesTabSwitchEvent(String screenName, String tabName, String count);

    void payFine(String screenName, String buttonName);

    void payFineSuccess(String screenName, String count, String summ);

    void showReceipt(String screenName);

    void saveReceiptInAlbum(String screenName);

    void sendReceiptToMail(String screenName);

    void logout(String screenName);

    void logoutConfirmed(String screenName);

    void notificationSwitcherClick(String screenName, String position);

    void openScreen(String screenName);
}
