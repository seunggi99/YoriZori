package KNU.YoriZori.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserFilteredRecipeDetailsDto {
    private Long id;
    private String name;
    private String introduction;
    private String cookingInstructions;
    private String imageUrl;
    private int insufficientIngredientsCount;
    private List<IngredientInfoDto> insufficientIngredients;

}
