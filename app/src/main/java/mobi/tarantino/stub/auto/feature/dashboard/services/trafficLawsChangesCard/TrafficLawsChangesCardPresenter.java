package mobi.tarantino.stub.auto.feature.dashboard.services.trafficLawsChangesCard;

import java.util.List;

import mobi.tarantino.stub.auto.model.additionalData.MobiAdditionalModel;
import mobi.tarantino.stub.auto.model.additionalData.pojo.Article;
import mobi.tarantino.stub.auto.mvp.MvpLceRxPresenter;
import rx.Observable;
import rx.functions.Func1;

/**

 */

public class TrafficLawsChangesCardPresenter extends
        MvpLceRxPresenter<TrafficLawsChangesCardView, Article> {

    private MobiAdditionalModel model;
    private Article data;

    public TrafficLawsChangesCardPresenter(MobiAdditionalModel model) {
        this.model = model;
    }

    public TrafficLawsChangesCardPresenter() {
    }

    public void loadData(boolean pullToRefresh) {
        if (data == null || pullToRefresh) {
            Observable<Article> observable =
                    model.getArticlesDecSort(0).map(new Func1<List<Article>, Article>() {
                        @Override
                        public Article call(List<Article> articles) {
                            if (articles != null && articles.size() > 0) {
                                return articles.get(0);
                            }
                            return null;
                        }
                    });
            subscribe(observable, pullToRefresh);
        } else {
            if (isViewAttached() && getView() != null) {
                getView().setData(data);
                getView().showContent();
            }
        }
    }

    @Override
    protected void onNext(Article data) {
        super.onNext(data);
        this.data = data;
    }
}
