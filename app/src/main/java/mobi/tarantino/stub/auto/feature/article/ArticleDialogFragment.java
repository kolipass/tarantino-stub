package mobi.tarantino.stub.auto.feature.article;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import mobi.tarantino.stub.auto.model.additionalData.pojo.Article;
import mobi.tarantino.stub.auto.model.database.dbo.ArticleDBO;

import static mobi.tarantino.stub.auto.Consts.Key.ARTICLE;
import static mobi.tarantino.stub.auto.Consts.Key.ARTICLE_TYPE;

public class ArticleDialogFragment extends DialogFragment implements SwipeRefreshLayout
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
    private Unbinder unbinder;
    private Article article;
    private AnalyticReporter analyticReporter;

    public static Fragment newInstance(String articleType, Article article) {
        Bundle args = new Bundle();
        args.putParcelable(ARTICLE, article);
        args.putString(ARTICLE_TYPE, articleType);
        ArticleDialogFragment fragment = new ArticleDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_traffic_law, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        injectDependencies();
        super.onViewCreated(view, savedInstanceState);

        article = getArguments().getParcelable(ARTICLE);
        unbinder = ButterKnife.bind(this, view);

        swipeRefreshLayout.setOnRefreshListener(this);

        picasso.load(AdditionalApiHelper.getImagePath(article))
                .placeholder(R.drawable.icon_police_cap_dark)
                .into(image);

        webView.setWebViewClient(new WebViewClient() {
            WeakReference<View> progress = new WeakReference<>(loadingView);
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

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
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
        String articleType = getArguments().getString(ARTICLE_TYPE);
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

    }

    protected void injectDependencies() {
        MobiApplication
                .get(getContext())
                .getComponentContainer()
                .getAnalyticComponent()
                .inject(this);
        initAnalyticReporter();
    }

    private void initAnalyticReporter() {
        analyticReporter = MobiApplication.get(getContext())
                .getComponentContainer()
                .getAnalyticComponent()
                .provideAnalyticReporter();
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }


    @Override
    public void onRefresh() {
        webView.loadUrl(AdditionalApiHelper.getArticleUrl(article));
    }

    @OnClick(R.id.changes_traffic_laws_close_button)
    void close() {
        dismiss();
    }
}

