package hr.java.web.helloworld.service;

import hr.java.web.helloworld.domain.Article;
import hr.java.web.helloworld.domain.SearchArticle;
import hr.java.web.helloworld.dto.ArticleDTO;
import hr.java.web.helloworld.dto.SearchArticleDTO;

import java.util.List;
import java.util.Optional;

public interface ArticleService {
    List<ArticleDTO> getAllArticles();
    List<ArticleDTO> getArticlesByName(String articleName);
    ArticleDTO saveNewArticle(ArticleDTO article);
    List<ArticleDTO> filterByParameters(SearchArticleDTO searchArticleDTO);
    Optional<ArticleDTO> updateArticle(ArticleDTO articleDTO, Integer id);
    boolean articleByIdExists(Integer id);
    boolean deleteArticleById(Integer id);
}
