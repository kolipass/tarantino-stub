package mobi.tarantino.stub.auto.feature.dashboard;

import mobi.tarantino.stub.auto.model.additionalData.pojo.Article;

/**

 */
public interface ArticleListener {
    void showArticle(String articleType, Article data);

    void showArticles();
}
