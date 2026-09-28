package hr.java.web.helloworld.repository;

import hr.java.web.helloworld.domain.Article;
import hr.java.web.helloworld.domain.Category;
import hr.java.web.helloworld.domain.SearchArticle;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

@Primary
@Repository
@AllArgsConstructor
public class JdbcArticleRepository implements ArticleRepository {

    private JdbcTemplate jdbcTemplate;
    @Override
    public List<Article> getAllArticles() {
        return jdbcTemplate.query("SELECT * FROM ARTICLE", new ArticleMapper());
    }

    @Override
    public List<Article> getArticlesByName(String articleName) {
        return jdbcTemplate.query("SELECT * FROM ARTICLE WHERE NAME = ?",
                new ArticleMapper(), articleName);
    }

    @Override
    public Optional<Article> getArticleById(Integer id) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("articleId", id);
        return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM ARTICLE WHERE ID = :articleId",
                new ArticleMapper(), parameters));
    }

//    @Override
//    public Article saveNewArticle(Article article) {
//        final String SQL =
//                "SELECT ID FROM FINAL TABLE (INSERT INTO ARTICLE (name, description, price, categoryId) VALUES (?, ?, ?, ?)) ARTICLE";
//        Integer generatedId = jdbcTemplate.queryForObject(SQL, Integer.class, article.getName(), article.getDescription(), article.getPrice(), article.getCategory().getId());
//        article.setId(generatedId);
//        return article;
//    }
    @Override
    public Article saveNewArticle(Article article) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("ARTICLE")
                .usingGeneratedKeyColumns("ID");

        Map<String, Object> parameters = Map.of(
                "name", article.getName(),
                "description", article.getDescription(),
                "price", article.getPrice(),
                "categoryId", article.getCategory().getId()
        );

        Number generatedId = insert.executeAndReturnKey(parameters);
        article.setId(generatedId.intValue());
        return article;
    }

    @Override
    public List<Article> filterByParameters(SearchArticle searchArticle) {
        StringJoiner where = new StringJoiner(" AND ", " WHERE ", "").setEmptyValue("");
        Map<String, Object> params = new HashMap<>();

        if(Optional.ofNullable(searchArticle.getId()).isPresent()) {
            where.add("id = ?");
            params.put("id", searchArticle.getId());
        }

        if(!searchArticle.getName().isEmpty()) {
            where.add("name LIKE ?");
            params.put("name", searchArticle.getName());
        }

        if(!searchArticle.getDescription().isEmpty()) {
            where.add("description LIKE ?");
            params.put("description", searchArticle.getDescription());
        }

        where.add("price between ? AND ?");

        if(Optional.ofNullable(searchArticle.getLowerPrice()).isPresent()) {
            params.put("lowerPrice", searchArticle.getLowerPrice());
        }
        else {
            params.put("lowerPrice", BigDecimal.ZERO);
        }

        if(Optional.ofNullable(searchArticle.getUpperPrice()).isPresent()) {
            params.put("upperPrice", searchArticle.getUpperPrice());
        }
        else {
            params.put("upperPrice", BigDecimal.valueOf(Double.MAX_VALUE));
        }

        if(Optional.ofNullable(searchArticle.getCategory()).isPresent()) {
            where.add("categoryId = ?");
            params.put("categoryId", searchArticle.getCategory().getId());
        }

        String sql = "SELECT id, name, description, price, categoryId" +
                " FROM article" +
                where;

        PreparedStatementSetter pss = ps -> {

            int ordinal = 1;

            if(params.containsKey("id")) {
                ps.setInt(ordinal, (Integer) params.get("id"));
                ordinal++;
            }

            if(params.containsKey("name")) {
                ps.setString(ordinal, "'%" + (String) params.get("name") + "%'");
                ordinal++;
            }

            if(params.containsKey("description")) {
                ps.setString(ordinal, "'%" + (String) params.get("description") + "%'");
                ordinal++;
            }

            ps.setBigDecimal(ordinal, (BigDecimal) params.get("lowerPrice"));
            ordinal++;
            ps.setBigDecimal(ordinal, (BigDecimal) params.get("upperPrice"));
            ordinal++;

            if(params.containsKey("categoryId")) {
                ps.setInt(ordinal, (Integer) params.get("categoryId"));
            }
        };

        return jdbcTemplate.query(sql, pss, new ArticleMapper());
    }

    @Override
    public Optional<Article> updateArticle(Article articleToUpdate, Integer id) {
        if(articleByIdExists(id)) {
            final String SQL =
                    "UPDATE ARTICLE SET name = ?, description = ?, price = ?, categoryId = ? WHERE ID = ?";
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL);
                ps.setString(1, articleToUpdate.getName());
                ps.setString(2, articleToUpdate.getDescription());
                ps.setBigDecimal(3, articleToUpdate.getPrice());
                ps.setInt(4, articleToUpdate.getCategory().getId());
                ps.setInt(5, id);
                return ps;
            });
            articleToUpdate.setId(id);
            return Optional.of(articleToUpdate);
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public boolean articleByIdExists(Integer id) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT (*) FROM ARTICLE WHERE ID = ?", Integer.class, id);
        return count > 0;
    }

    @Override
    public boolean deleteArticleById(Integer id) {
        if(articleByIdExists(id)) {
            jdbcTemplate.update(
                    "DELETE FROM ARTICLE WHERE ID = ?", id);
            return true;
        }
        else {
            return false;
        }
    }

    private static class ArticleMapper implements RowMapper<Article> {

        public Article mapRow(ResultSet rs, int i) throws SQLException {

            Article newArticle = new Article();
            newArticle.setId(rs.getInt("ID"));
            newArticle.setName(rs.getString("NAME"));
            newArticle.setDescription(rs.getString("DESCRIPTION"));
            newArticle.setPrice(rs.getBigDecimal("PRICE"));

            Integer categoryId = rs.getInt("CATEGORYID");

            if(Category.CARS.getId().equals(categoryId)) {
                newArticle.setCategory(Category.CARS);
            }
            else {
                newArticle.setCategory(Category.PROPERTIES);
            }

            return newArticle;
        }
    }
}
