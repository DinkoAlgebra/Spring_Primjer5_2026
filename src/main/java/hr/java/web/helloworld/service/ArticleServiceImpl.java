package hr.java.web.helloworld.service;

import hr.java.web.helloworld.domain.Article;
import hr.java.web.helloworld.domain.Category;
import hr.java.web.helloworld.domain.SearchArticle;
import hr.java.web.helloworld.dto.ArticleDTO;
import hr.java.web.helloworld.dto.SearchArticleDTO;
import hr.java.web.helloworld.repository.ArticleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private ArticleRepository articleRepository;

    @Override
    public List<ArticleDTO> getAllArticles() {
        return articleRepository.getAllArticles().stream()
                .map(this::convertArticleToArticleDTO)
                .toList();
    }

    @Override
    public List<ArticleDTO> getArticlesByName(String articleName) {
        return articleRepository.getArticlesByName(articleName).stream()
                .map(this::convertArticleToArticleDTO)
                .toList();
    }

    @Override
    public ArticleDTO saveNewArticle(ArticleDTO article) {
        return convertArticleToArticleDTO(articleRepository.saveNewArticle(convertArticleDtoToArticle(article)));
    }

    @Override
    public List<ArticleDTO> filterByParameters(SearchArticleDTO searchArticleDTO) {
        return articleRepository.filterByParameters(
                convertSearchArticleDtoToSearchArticle(searchArticleDTO))
                .stream().map(this::convertArticleToArticleDTO)
                .toList();
    }

    @Override
    public Optional<ArticleDTO> updateArticle(ArticleDTO articleDTO, Integer id) {
        Optional<Article> updatedArticleOptional =
                articleRepository.updateArticle(convertArticleDtoToArticle(articleDTO), id);

        if(updatedArticleOptional.isPresent()) {
            return Optional.of(convertArticleToArticleDTO(updatedArticleOptional.get()));
        }

        return Optional.empty();
    }

    @Override
    public boolean articleByIdExists(Integer id) {
        return articleRepository.articleByIdExists(id);
    }

    @Override
    public boolean deleteArticleById(Integer id) {
        return articleRepository.deleteArticleById(id);
    }

    private ArticleDTO convertArticleToArticleDTO(Article article) {
        return new ArticleDTO(article.getName(),
                article.getDescription(), article.getPrice(),
                article.getCategory().getName());
    }

    private Article convertArticleDtoToArticle(ArticleDTO articleDTO) {
        Integer latestId =
                articleRepository.getAllArticles().stream()
                .max((a1, a2) -> a1.getId().compareTo(a2.getId()))
                .get().getId();

        return new Article(latestId + 1, articleDTO.getArticleName(),
                articleDTO.getArticleDescription(), articleDTO.getArticlePrice(),
                Category.valueOf(articleDTO.getCategoryName()));
    }

    private SearchArticle convertSearchArticleDtoToSearchArticle(SearchArticleDTO searchArticleDTO) {
        return new SearchArticle(
                searchArticleDTO.getArticleName(),
                searchArticleDTO.getArticleDescription(),
                searchArticleDTO.getLowerPrice(),
                searchArticleDTO.getUpperPrice(),
                Category.valueOf(searchArticleDTO.getCategoryName()));
    }


}
