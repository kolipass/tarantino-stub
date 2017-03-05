package mobi.tarantino.stub.auto.feature.article;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import mobi.tarantino.stub.auto.MobiApplication;
import mobi.tarantino.stub.auto.R;
import mobi.tarantino.stub.auto.analytics.AnalyticReporter;
import mobi.tarantino.stub.auto.analytics.Reporter;
import mobi.tarantino.stub.auto.model.additionalData.AdditionalApiHelper;
import mobi.tarantino.stub.auto.model.additionalData.MobiAdditionalModel;
import mobi.tarantino.stub.auto.model.database.dbo.ArticleDBO;

import static mobi.tarantino.stub.auto.Consts.Key.ARTICLE;
import static mobi.tarantino.stub.auto.Consts.Key.ARTICLE_TYPE;

public class ArticleActivity extends AppCompatActivity implements SwipeRefreshLayout
        .OnRefreshListener {
    @BindView(R.id.contentView)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.progress)
    View loadingView;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.changes_traffic_laws_image)
    ImageView image;
    @BindView(R.id.info_textView)
    TextView fragmentTitleTextView;
    @BindView(R.id.article_title_textView)
    TextView articleTitleTextView;
    @Inject
    Picasso picasso;
    @Inject
    MobiAdditionalModel model;
    private Unbinder unbinder;
    private ArticleDBO article;
    private AnalyticReporter analyticReporter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_traffic_law);

        unbinder = ButterKnife.bind(this);
        injectDependencies();

        article = getIntent().getParcelableExtra(ARTICLE);

        swipeRefreshLayout.setOnRefreshListener(this);

        picasso.load(AdditionalApiHelper.getImagePath(article))
                .placeholder(R.drawable.pdd_placeholder)
                .into(image);

        webView.setWebViewClient(new WebViewClient() {
            WeakReference<View> progress = new WeakReference<>(loadingView);
            @NonNull
            WeakReference<SwipeRefreshLayout> swipeRefresh =
                    new WeakReference<>(swipeRefreshLayout);

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                View progressBar = progress.get();
                if (progressBar != null) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                View progressBar = progress.get();
                if (progressBar != null) {
                    progressBar.setVisibility(View.INVISIBLE);
                }


                SwipeRefreshLayout swipeRefreshLayout = swipeRefresh.get();
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            public boolean shouldOverrideUrlLoading(@NonNull WebView view, @Nullable String url) {
                if (url != null && !url.equals(AdditionalApiHelper.getArticleUrl(article))) {
                    view.getContext().startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                } else {
                    return false;
                }
            }
        });
        webView.getSettings().setAllowFileAccess(false);
        webView.loadUrl(AdditionalApiHelper.getArticleUrl(article));
        String articleType = getIntent().getStringExtra(ARTICLE_TYPE);
        switch (articleType) {
            case ArticleDBO.TYPE_LAW:
                fragmentTitleTextView.setText(getString(R.string.traffic_law_change));
                break;
            case ArticleDBO.TYPE_PARTNER_ACTIONS:
                fragmentTitleTextView.setText(getString(R.string.partner_event));
                break;
        }
        articleTitleTextView.setText(article.getTitle());
    }

    @Override
    public void onResume() {
        super.onResume();
        String description = article == null ? "unknown" : article.getTitle();
        analyticReporter.showNotificationEvent(Reporter.SCREEN_TRAFFIC_LAWS, description);
        model.markViewedArticle(article);
    }

    protected void injectDependencies() {
        MobiApplication
                .get(this)
                .getComponentContainer()
                .getAnalyticComponent()
                .inject(this);
        initAnalyticReporter();
    }

    private void initAnalyticReporter() {
        analyticReporter = MobiApplication.get(this)
                .getComponentContainer()
                .getAnalyticComponent()
                .provideAnalyticReporter();
    }

    @Override
    public void onRefresh() {
        webView.loadUrl(AdditionalApiHelper.getArticleUrl(article));
    }

    @OnClick(R.id.changes_traffic_laws_close_button)
    void close() {
        finish();
    }

    @Override
    public void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

}
