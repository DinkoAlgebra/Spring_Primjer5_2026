package hr.java.web.helloworld.controller;

import hr.java.web.helloworld.dto.ArticleDTO;
import hr.java.web.helloworld.service.ArticleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/web-shop")
@AllArgsConstructor
public class ArticleController {

    private ArticleService articleService;

    @GetMapping
    public ResponseEntity<List<ArticleDTO>> getAllArticles() {
        return ResponseEntity.ok(articleService.getAllArticles().stream().toList());
    }

    @GetMapping("/{articleName}")
    public ResponseEntity<List<ArticleDTO>> filterArticlesByName(@PathVariable String articleName) {
        return ResponseEntity.ok(articleService.getArticlesByName(articleName).stream().toList());
    }

    @PostMapping("/new")
    public ResponseEntity<?> saveNewArticle(@Valid @RequestBody ArticleDTO articleDTO) {
        ArticleDTO savedArticleDTO = articleService.saveNewArticle(articleDTO);
        return ResponseEntity.ok(savedArticleDTO);
    }

    @PutMapping("/article/{articleId}")
    public ResponseEntity<ArticleDTO> updateArticle(@Valid @RequestBody ArticleDTO articleDTO, @PathVariable Integer articleId) {
        if(articleService.articleByIdExists(articleId)) {
            articleService.updateArticle(articleDTO, articleId);
            return ResponseEntity.ok(articleDTO);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/article/{articleId}")
    public ResponseEntity<?> deleteArticle(@PathVariable Integer articleId) {
        if(articleService.articleByIdExists(articleId)) {
            boolean result = articleService.deleteArticleById(articleId);
            if(result) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
