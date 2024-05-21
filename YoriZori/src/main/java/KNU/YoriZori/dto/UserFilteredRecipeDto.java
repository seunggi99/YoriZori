package KNU.YoriZori.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserFilteredRecipeDto {
    private Long id;
    private String name;
    private int bookmarkCount;
    private String imageUrl;
    private Long categoryId;
    private String categoryName;
    private int insufficientIngredientsCount;
    private List<IngredientInfoDto> insufficientIngredients;
    private int ownedIngredientsCount;
}
