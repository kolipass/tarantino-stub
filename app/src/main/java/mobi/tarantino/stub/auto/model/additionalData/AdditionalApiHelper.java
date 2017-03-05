package mobi.tarantino.stub.auto.model.additionalData;

import android.support.annotation.NonNull;

import mobi.tarantino.stub.auto.BuildConfig;
import mobi.tarantino.stub.auto.model.additionalData.pojo.Article;
import mobi.tarantino.stub.auto.model.database.dbo.ArticleDBO;

public class AdditionalApiHelper {
    @NonNull
    public static String getArticleUrl(@NonNull Article article) {
        return BuildConfig.ADDITIONAL_DATA_API + article.getUrl();
    }

    @NonNull
    public static String getImagePath(@NonNull Article article) {
        return BuildConfig.ADDITIONAL_DATA_API + article.getPreview();
    }

    @NonNull
    public static String getArticleUrl(@NonNull ArticleDBO article) {
        return BuildConfig.ADDITIONAL_DATA_API + article.getUrl();
    }

    @NonNull
    public static String getImagePath(@NonNull ArticleDBO article) {
        return BuildConfig.ADDITIONAL_DATA_API + article.getPreview();
    }
}