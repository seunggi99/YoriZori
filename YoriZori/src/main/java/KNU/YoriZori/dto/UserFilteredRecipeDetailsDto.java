package KNU.YoriZori.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserFilteredRecipeDetailsDto {
    private Long id;
    private String name;
    private int bookmarkCount;
    private String imageUrl;
    private Long categoryId;
    private String categoryName;

    private int insufficientIngredientsCount;
    private List<IngredientInfoDto> insufficientIngredients;

    private String ingredientDetails; // RCP_PARTS_DTLS 추가
    private List<String> manual; // MANUAL 추가
    private List<String> manualImg; // MANUAL_IMG 추가

}
