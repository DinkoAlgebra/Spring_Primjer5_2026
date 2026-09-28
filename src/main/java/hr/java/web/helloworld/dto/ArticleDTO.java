package hr.java.web.helloworld.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArticleDTO {
    @NotBlank(message = "Article name cannot be blank")
    private String articleName;
    @NotBlank(message = "Article description cannot be blank")
    private String articleDescription;
    @DecimalMin(value = "0.0", message = "Article price must be positive")
    private BigDecimal articlePrice;
    @NotBlank(message = "Category name cannot be blank")
    private String categoryName;
}
