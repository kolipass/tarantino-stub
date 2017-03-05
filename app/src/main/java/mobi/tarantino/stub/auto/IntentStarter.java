package mobi.tarantino.stub.auto;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import mobi.tarantino.stub.auto.di.AuthorizationResolver;
import mobi.tarantino.stub.auto.feature.article.ArticleActivity;
import mobi.tarantino.stub.auto.feature.auth.AuthActivity;
import mobi.tarantino.stub.auto.feature.dashboard.DashBoardActivity;
import mobi.tarantino.stub.auto.feature.dashboard.services.notificationCard.NotificationDTO;
import mobi.tarantino.stub.auto.feature.demoTour.DemoTourActivity;
import mobi.tarantino.stub.auto.feature.notifications.NotificationsActivity;
import mobi.tarantino.stub.auto.model.database.DatabaseHelperFactory;
import mobi.tarantino.stub.auto.model.database.dbo.ArticleDBO;

import static mobi.tarantino.stub.auto.Consts.Key.ARTICLE;
import static mobi.tarantino.stub.auto.Consts.Key.ARTICLE_TYPE;

public class IntentStarter {

    private Context context;
    private AuthorizationResolver authorizationResolver;
    private IPreferencesManager preferencesManager;
    private DatabaseHelperFactory databaseHelperFactory;

    public IntentStarter(Context context, IPreferencesManager preferencesManager,
                         AuthorizationResolver authorizationResolver,
                         DatabaseHelperFactory databaseHelperFactory) {
        this.context = context;
        this.preferencesManager = preferencesManager;
        this.authorizationResolver = authorizationResolver;
        this.databaseHelperFactory = databaseHelperFactory;
    }

    public void showAuthentication() {
        Intent authenticationIntent = new Intent(context, AuthActivity.class);
        authenticationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent
                .FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(authenticationIntent);
    }


    public void showDashBoard() {
        Intent dashBoardIntent = new Intent(context, DashBoardActivity.class);
        dashBoardIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(dashBoardIntent);
    }


    public void showArticle(@NonNull Context context, @NotNull String articleType, @NotNull
            ArticleDBO article) {
        Intent profileIntent = new Intent(context, ArticleActivity.class);

        profileIntent.putExtra(ARTICLE, article);
        profileIntent.putExtra(ARTICLE_TYPE, articleType);

        context.startActivity(profileIntent);
    }

    public void start() {
        if (isAvailableNonAuthorization()) {
            startNonAuthorization();
        } else {
            if (authorizationResolver.isAuthorized()) {
                showDashBoard();
            } else {
                showAuthentication();
            }
        }
    }

    private void startNonAuthorization() {
        Intent demoTourIntent = new Intent(context, DemoTourActivity.class);
        demoTourIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(demoTourIntent);
    }

    private boolean isAvailableNonAuthorization() {
        return !preferencesManager.isDemoTourFinished();
    }

    public void logout() {
        preferencesManager.clearAll();
        databaseHelperFactory.getHelper().clearDB();
        preferencesManager.demoTourFinish(true);
        MobiApplication.get(context).getComponentContainer().releaseScreenComponents();
        MobiApplication.get(context).getComponentContainer().releaseAuthComponent();
        start();
    }

    public void openInBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void updateApp() {
        String appPackageName = context.getPackageName();
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + appPackageName));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public void openNotificationsScreen(@NonNull Context context, @NonNull NotificationDTO
            notificationDTO) {
        Intent intent = NotificationsActivity.createIntent(context, notificationDTO);
        context.startActivity(intent);
    }
}
