package mobi.tarantino.stub.auto.analytics;

import java.util.ArrayList;
import java.util.List;

import mobi.tarantino.stub.auto.IPreferencesManager;

public class AnalyticReporter implements Reporter {

    private List<AbstractAnalytic> analyticTypes;
    private IPreferencesManager preferencesManager;

    public AnalyticReporter(IPreferencesManager preferencesManager) {
        this.preferencesManager = preferencesManager;
        analyticTypes = new ArrayList<>();
    }

    public void registerAnalytic(AbstractAnalytic analytics) {
        analyticTypes.add(analytics);
    }

    public void clearAnalytics() {
        analyticTypes.clear();
    }

    @Override
    public void authSuccess(String screenName) {
        for (AbstractAnalytic analytic : analyticTypes) {
            analytic.pushSimpleEvent(getGaClientId(), getUserId(),

                    screenName, CATEGORY_AUTH, "Success", getUserId());
        }

    }

    @Override
    public void authFailed(String screenName, String description) {

        for (AbstractAnalytic analytic : analyticTypes) {
            analytic.pushSimpleEvent(getGaClientId(), getUserId(),

                    screenName, CATEGORY_AUTH, "Error", description);
        }

    }

    @Override
    public void phoneNextButtonClick(String screenName) {

        for (AbstractAnalytic analytic : analyticTypes) {
            analytic.pushSimpleEvent(getGaClientId(), getUserId(),

                    screenName, CATEGORY_AUTH, "Далее", "");
        }

    }

    @Override
    public void whatPhoneNumberClick(String screenName) {

        for (AbstractAnalytic analytic : analyticTypes) {
            analytic.pushSimpleEvent(getGaClientId(), getUserId(),

                    screenName, "LinkClick", "Зачем приложения Ваш номер?", "");
        }

    }

    @Override
    public void offerClick(String screenName) {

        for (AbstractAnalytic analytic : analyticTypes) {
            analytic.pushSimpleEvent(getGaClientId(), getUserId(),

                    screenName, "LinkClick", "Оферты", "");
        }

    }

    @Override
    public void retrySendCodeClick(String screenName) {

        for (AbstractAnalytic analytic : analyticTypes) {
            analytic.pushSimpleEvent(getGaClientId(), getUserId(),

                    screenName, CATEGORY_AUTH, "Выслать код снова", "");
        }

    }

    @Override
    public void errorEnterPhone(String screenName, String description) {

        for (AbstractAnalytic analytic : analyticTypes) {
            analytic.pushSimpleEvent(getGaClientId(), getUserId(),

                    screenName, CATEGORY_AUTH, "Error", description);
        }

    }

    @Override
    public void errorEnterCode(String screenName, String description) {

        for (AbstractAnalytic analytic : analyticTypes) {
            analytic.pushSimpleEvent(getGaClientId(), getUserId(),

                    screenName, CATEGORY_AUTH, "Error", description);
        }

    }

    @Override
    public void commonError(String screenName, String errorDescription) {

        for (AbstractAnalytic analytic : analyticTypes) {
            analytic.pushSimpleEvent(getGaClientId(), getUserId(),

                    screenName, "Error", errorDescription, screenName);
        }

    }

    @Override
    public void toastEvent(String screenName, String text) {

        for (AbstractAnalytic analytic : analyticTypes) {
            analytic.pushSimpleEvent(getGaClientId(), getUserId(),

                    screenName, "Message", text, "Show");
        }

    }

    @Override
    public void showNotificationEvent(String screenName, String description) {

        for (AbstractAnalytic analytic : analyticTypes) {
            analytic.pushSimpleEvent(getGaClientId(), getUserId(),

                    screenName, CATEGORY_NOTIFICATION, description, "Show");
        }

    }

    @Override
    public void notificationUserEvent(String screenName, String notificationDescription, String
            buttonName) {

        for (AbstractAnalytic analytic : analyticTypes) {
            analytic.pushSimpleEvent(getGaClientId(), getUserId(),

                    screenName, CATEGORY_NOTIFICATION, notificationDescription, "Клик_" +
                            buttonName);
        }

    }

    @Override
    public void menuClickEvent(String screenName, String menuName) {

        for (AbstractAnalytic analytic : analyticTypes) {
            analytic.pushSimpleEvent(getGaClientId(), getUserId(),

                    screenName, "Menu", menuName, screenName);
        }

    }

    @Override
    public void widgetEvent(String screenName, String widgetCategory) {

        for (AbstractAnalytic analytic : analyticTypes) {
            analytic.pushSimpleEvent(getGaClientId(), getUserId(),

                    screenName, CATEGORY_WIDGET, widgetCategory, ACTION_FOCUS);
        }

    }

    @Override
    public void refreshWidgetEvent(String screenName, String widgetCategory) {

        for (AbstractAnalytic analytic : analyticTypes) {
            analytic.pushSimpleEvent(getGaClientId(), getUserId(),

                    screenName, CATEGORY_WIDGET, widgetCategory, ACTION_REFRESH);
        }
    }

    public void widgetClickEvent(String screenName, String widgetCategory, String eventLabel) {

        for (AbstractAnalytic analytic : analyticTypes) {
            analytic.pushSimpleEvent(getGaClientId(), getUserId(),

                    screenName, CATEGORY_WIDGET,
                    widgetCategory,
                    ACTION_BUTTON_CLICK + " - " + eventLabel);
        }
    }


    @Override
    public void phoneCallEvent(String screenName, String serviceName) {

        for (AbstractAnalytic analytic : analyticTypes) {
            analytic.pushSimpleEvent(getGaClientId(), getUserId(),

                    screenName, "Call", serviceName, screenName);
        }

    }

    @Override
    public void addCarEvent(String screenName) {

        for (AbstractAnalytic analytic : analyticTypes) {
            analytic.pushSimpleEvent(getGaClientId(), getUserId(),

                    screenName, "AddAuto", ACTION_BUTTON_CLICK, screenName);
        }

    }

    @Override
    public void addCarSuccessEvent(String screenName) {

        for (AbstractAnalytic analytic : analyticTypes) {
            analytic.pushSimpleEvent(getGaClientId(), getUserId(),

                    screenName, "AddAuto", "SuccessAddAuto", getUserId());
        }

    }

    @Override
    public void removeCarSuccess(String screenName) {

        for (AbstractAnalytic analytic : analyticTypes) {
            analytic.pushSimpleEvent(getGaClientId(), getUserId(),

                    screenName, "DeleteAuto", "SuccessDeleteAuto", getUserId());
        }

    }

    @Override
    public void addDriverEvent(String screenName) {

        for (AbstractAnalytic analytic : analyticTypes) {
            analytic.pushSimpleEvent(getGaClientId(), getUserId(),

                    screenName, "ViewFIneReceipt", ACTION_BUTTON_CLICK, screenName);
        }

    }

    @Override
    public void addDriverSuccessEvent(String screenName) {

        for (AbstractAnalytic analytic : analyticTypes) {
            analytic.pushSimpleEvent(getGaClientId(), getUserId(),

                    screenName, "AddDriver", "SuccessDriver", getUserId());
        }

    }

    @Override
    public void removeDriverSuccess(String screenName) {

        for (AbstractAnalytic analytic : analyticTypes) {
            analytic.pushSimpleEvent(getGaClientId(), getUserId(),

                    screenName, "DeleteDriver", "Success", getUserId());
        }

    }

    @Override
    public void finesTabSwitchEvent(String screenName, String tabName, String count) {

        for (AbstractAnalytic analytic : analyticTypes) {
            analytic.pushSimpleEvent(getGaClientId(), getUserId(),

                    screenName, CATEGORY_FINES, tabName, count);
        }

    }

    @Override
    public void payFine(String screenName, String buttonName) {

        for (AbstractAnalytic analytic : analyticTypes) {
            analytic.pushSimpleEvent(getGaClientId(), getUserId(),

                    screenName, CATEGORY_FINES, "Клик_" + buttonName, screenName);
        }

    }

    @Override
    public void payFineSuccess(String screenName, String count, String summ) {

        for (AbstractAnalytic analytic : analyticTypes) {
            analytic.pushSimpleEvent(getGaClientId(), getUserId(),

                    screenName, "Payment", count, summ);
        }

    }

    @Override
    public void showReceipt(String screenName) {

        for (AbstractAnalytic analytic : analyticTypes) {
            analytic.pushSimpleEvent(getGaClientId(), getUserId(),

                    screenName, CATEGORY_RECEIPT, "Квитанция платежа", ACTION_CLICK);
        }

    }

    @Override
    public void saveReceiptInAlbum(String screenName) {

        for (AbstractAnalytic analytic : analyticTypes) {
            analytic.pushSimpleEvent(getGaClientId(), getUserId(),

                    screenName, CATEGORY_RECEIPT, "Сохранить в фотоальбом", "");
        }

    }

    @Override
    public void sendReceiptToMail(String screenName) {

        for (AbstractAnalytic analytic : analyticTypes) {
            analytic.pushSimpleEvent(getGaClientId(), getUserId(),

                    screenName, CATEGORY_RECEIPT, "Отправить на эл. почту", ACTION_CLICK);
        }

    }

    @Override
    public void logout(String screenName) {
        for (AbstractAnalytic analytic : analyticTypes) {
            analytic.pushSimpleEvent(getGaClientId(), getUserId(),

                    screenName, CATEGORY_EXIT, ACTION_CLICK, getUserId());
        }
    }

    @Override
    public void logoutConfirmed(String screenName) {
        for (AbstractAnalytic analytic : analyticTypes) {
            analytic.pushSimpleEvent(getGaClientId(), getUserId(),

                    screenName, CATEGORY_EXIT, ACTION_SUCCESS, getUserId());
        }
    }

    @Override
    public void notificationSwitcherClick(String screenName, String position) {

        for (AbstractAnalytic analytic : analyticTypes) {
            analytic.pushSimpleEvent(getGaClientId(), getUserId(),

                    screenName, "SMS о новых штрафах", position, getUserId());
        }

    }

    @Override
    public void openScreen(String screenName) {

        for (AbstractAnalytic analytic : analyticTypes) {
            analytic.pushOpenScreen(getGaClientId(), getUserId(),

                    screenName);
        }

    }

    private String getGaClientId() {
        return preferencesManager.getGaClientId();
    }

    private String getUserId() {
        return String.valueOf(preferencesManager.getPhone().hashCode());
    }

    private String getFinePayCount() {
        return preferencesManager.getFinesPayCount();
    }

    private String getFineNotPayCount() {
        return preferencesManager.getFinesNotPayCount();
    }

    private String getDriversCount() {

        return preferencesManager.getDriversCount();
    }

    private String getCarsCount() {
        return preferencesManager.getCarsCount();
    }
}
