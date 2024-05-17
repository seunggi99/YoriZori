package KNU.YoriZori;

import KNU.YoriZori.dto.RecipeImportDto;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

public class JsonFileReader {

    public static List<RecipeImportDto> parseJson(String jsonString) {
        List<RecipeImportDto> recipes = new ArrayList<>();

        try {
            JSONObject jsonResponse = new JSONObject(jsonString);
            JSONArray recipeArray = jsonResponse.getJSONObject("COOKRCP01").getJSONArray("row");

            for (int i = 0; i < recipeArray.length(); i++) {
                JSONObject recipeJson = recipeArray.getJSONObject(i);
                RecipeImportDto recipe = new RecipeImportDto(null,null,null,null);

                recipe.setName(recipeJson.optString("RCP_NM", "Unknown Name"));
                recipe.setRecipeCategory(recipeJson.optString("RCP_PAT2", "Unknown Category"));
                recipe.setImageUrl(recipeJson.optString("ATT_FILE_NO_MK", ""));

                String ingredientsStr = recipeJson.optString("RCP_PARTS_DTLS", "");
                recipe.setIngredients(parseIngredients(ingredientsStr));

                recipes.add(recipe);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipes;
    }
    private static List<String> parseIngredients(String ingredientsStr) {
        return Arrays.stream(ingredientsStr.split("[,\n]"))
                .map(part -> part.replaceAll("\\(.*?\\)", "")) // 괄호 내용 제거
                .map(part -> part.replaceAll("\\d+g", "")) // 숫자와 g 제거
                .map(part -> part.replaceAll("\\d+ml", "")) // 숫자와 ml 제거
                .map(part -> part.replaceAll("약간", "")) // "약간" 제거
                .map(part -> part.replaceAll("●.*?:", "")) // "●"부터 ":"까지 제거
                .map(part -> part.replaceAll("[^가-힣a-zA-Z]", "")) // 한글과 알파벳 외 문자 제거
                .map(String::trim)
                .filter(part -> !part.isEmpty())
                .distinct()
                .collect(Collectors.toList());
    }
}