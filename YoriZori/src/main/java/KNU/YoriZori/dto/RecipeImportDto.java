package KNU.YoriZori.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RecipeImportDto {
    private String name;
    private String recipeCategory;
    private String imageUrl;
    private List<String> ingredients;
}
