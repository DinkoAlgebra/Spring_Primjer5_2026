package hr.java.web.helloworld.repository;

import hr.java.web.helloworld.domain.Article;
import hr.java.web.helloworld.domain.SearchArticle;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository {
    List<Article> getAllArticles();
    List<Article> getArticlesByName(String articleName);
    Optional<Article> getArticleById(Integer id);
    Article saveNewArticle(Article article);
    List<Article> filterByParameters(SearchArticle searchArticle);
    Optional<Article> updateArticle(Article articleToUpdate, Integer id);
    boolean articleByIdExists(Integer id);
    boolean deleteArticleById(Integer id);
}
