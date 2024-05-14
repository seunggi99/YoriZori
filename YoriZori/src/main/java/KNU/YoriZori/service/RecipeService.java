package KNU.YoriZori.service;

import KNU.YoriZori.controller.RecipeController;
import KNU.YoriZori.domain.*;
import KNU.YoriZori.dto.IngredientInfoDto;
import KNU.YoriZori.dto.RecipeDetailsDto;
import KNU.YoriZori.dto.UserFilteredRecipeDetailsDto;
import KNU.YoriZori.dto.UserFilteredRecipeDto;
import KNU.YoriZori.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final PutInRepository putInRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final AvoidIngredientRepository avoidIngredientRepository;
    private final UserRepository userRepository;
    public Recipe findOne(Long recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("Recipe not found for id: " + recipeId));
    }

    @Transactional
    public Recipe updateBookmarkCount(Long recipeId, int count) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("Recipe not found with id " + recipeId));

        recipe.setBookmarkCount(count);

        return recipeRepository.save(recipe);
    }

    // 부족한 재료 반환
    public List<Ingredient> findInsufficientIngredient(Long recipeId, Long fridgeId) {
        // 레시피에 필요한 재료 목록 조회
        List<RecipeIngredient> recipeIngredients = recipeIngredientRepository.findAllByRecipeId(recipeId);

        // 냉장고에 있는 재료 목록 조회
        List<PutIn> fridgeIngredients = putInRepository.findAllByFridgeId(fridgeId);

        // 레시피 재료 ID 목록
        Set<Long> recipeIngredientIds = recipeIngredients.stream()
                .map(recipeIngredient -> recipeIngredient.getIngredient().getId())
                .collect(Collectors.toSet());

        // 냉장고 재료 ID 목록
        Set<Long> fridgeIngredientIds = fridgeIngredients.stream()
                .map(putIn -> putIn.getIngredient().getId())
                .collect(Collectors.toSet());

        // 부족한 재료 종류의 수 계산
        recipeIngredientIds.removeAll(fridgeIngredientIds);

        // 부족한 재료의 ID를 기반으로 Ingredient 객체 조회 및 리스트 생성
        List<Ingredient> insufficientIngredients = recipeIngredients.stream()
                .map(RecipeIngredient::getIngredient)
                .filter(ingredient -> recipeIngredientIds.contains(ingredient.getId()))
                .collect(Collectors.toList());

        return insufficientIngredients;
    }

    @Transactional
    public List<Recipe> findAllRecipes() {
        return recipeRepository.findAll();
    }


    // 회원 ID를 기반으로 기피 재료가 포함되지 않은 레시피 조회
    @Transactional
    public List<Recipe> findRecipesExcludingAvoidIngredients(Long userId) {
        List<Long> avoidIngredientIds = avoidIngredientRepository.findByUserId(userId)
                .stream()
                .map(avoidIngredient -> avoidIngredient.getIngredient().getId())
                .collect(Collectors.toList());

        // 기피 재료 목록이 비어 있으면 모든 레시피 반환
        if (avoidIngredientIds.isEmpty()) {
            return recipeRepository.findAll();
        }

        // 기피 재료를 포함하는 레시피 ID 목록 조회
        List<Long> avoidRecipeIds = recipeRepository.findRecipesWithIngredients(avoidIngredientIds)
                .stream()
                .map(Recipe::getId)
                .distinct()
                .collect(Collectors.toList());

        // 기피 재료를 포함하지 않는 레시피 목록 조회
        return recipeRepository.findByIdNotIn(avoidRecipeIds);
    }

    // 기피 재료가 포함되지 않은 레시피 및 부족한 재료 목록과 개수 조회
    @Transactional
    public List<UserFilteredRecipeDto> findUserFilteredRecipes(Long fridgeId) {

        Long userId = userRepository.findByFridgeId(fridgeId).getId();

        // 기피 재료를 포함하지 않는 레시피 목록 조회
        List<Recipe> recipesExcludingAvoidIngredients = findRecipesExcludingAvoidIngredients(userId);

        // 결과 DTO 리스트 생성
        List<UserFilteredRecipeDto> filteredRecipes = new ArrayList<>();

        for (Recipe recipe : recipesExcludingAvoidIngredients) {
            // 각 레시피에 대한 부족한 재료 목록 조회
            List<Ingredient> insufficientIngredients = findInsufficientIngredient(recipe.getId(), fridgeId);

            // 부족한 재료의 ID,이름 목록 생성
            List<IngredientInfoDto> ingredientInfoDtos = insufficientIngredients.stream()
                    .map(ingredient -> new IngredientInfoDto(ingredient.getId(), ingredient.getName()))
                    .collect(Collectors.toList());

            // UserFilteredRecipeDto 객체 생성 및 추가
            filteredRecipes.add(new UserFilteredRecipeDto(
                    recipe.getId(),
                    recipe.getName(),
                    recipe.getBookmarkCount(),
                    recipe.getImageUrl(),
                    recipe.getCategory().getId(),
                    recipe.getCategory().getName(),
                    insufficientIngredients.size(), // 부족한 재료의 개수
                    ingredientInfoDtos         // 부족한 재료 ID 목록
            ));
        }

        return filteredRecipes;
    }


    @Transactional
    public UserFilteredRecipeDetailsDto findUserFilteredRecipeDetails(Long fridgeId, Long recipeId) {
        Recipe recipe = findOne(recipeId);

        if (recipe == null) {
            return null; // 레시피가 존재하지 않으면 null 반환
        }

        // 각 레시피에 대한 부족한 재료 목록 조회
        List<Ingredient> insufficientIngredients = findInsufficientIngredient(recipeId, fridgeId);

        // 부족한 재료의 ID, 이름 목록 생성
        List<IngredientInfoDto> ingredientInfoDtos = insufficientIngredients.stream()
                .map(ingredient -> new IngredientInfoDto(ingredient.getId(), ingredient.getName()))
                .collect(Collectors.toList());

        // UserFilteredRecipeDetailsDto 객체 생성 및 반환
        return new UserFilteredRecipeDetailsDto(
                recipe.getId(),
                recipe.getName(),
                recipe.getBookmarkCount(),
                recipe.getImageUrl(),
                recipe.getCategory().getId(),
                recipe.getCategory().getName(),
                insufficientIngredients.size(), // 부족한 재료의 개수
                ingredientInfoDtos,
                null,
                null,
                null
        );
    }

    private final WebClient webClient;

    public void fetchAndMergeExternalData(RecipeDetailsDto recipeDetails) {
        String recipeName = recipeDetails.getName();
        String apiUrl = "http://openapi.foodsafetykorea.go.kr/api/d4ae07f685a7424a9e0e/COOKRCP01/json/1/1/RCP_NM=" + recipeName;
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(apiUrl, String.class);

        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray recipes = jsonResponse.getJSONObject("COOKRCP01").getJSONArray("row");

            if (recipes.length() > 0) {
                JSONObject recipe = recipes.getJSONObject(0);
                recipeDetails.setIngredientDetails(recipe.optString("RCP_PARTS_DTLS"));

                List<String> manualSteps = new ArrayList<>();
                List<String> manualImages = new ArrayList<>();

                for (int i = 1; i <= 20; i++) {
                    String manualStepKey = String.format("MANUAL%02d", i);
                    String manualImgKey = String.format("MANUAL_IMG%02d", i);

                    String manualStep = recipe.optString(manualStepKey);
                    String manualImg = recipe.optString(manualImgKey);

                    if (!manualStep.isEmpty()) {
                        manualSteps.add(manualStep);
                    }

                    if (!manualImg.isEmpty()) {
                        manualImages.add(manualImg);
                    }
                }

                recipeDetails.setManual(manualSteps);
                recipeDetails.setManualImg(manualImages);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchAndMergeExternalData(UserFilteredRecipeDetailsDto recipeDetails) {
        String recipeName = recipeDetails.getName();
        String apiUrl = "http://openapi.foodsafetykorea.go.kr/api/d4ae07f685a7424a9e0e/COOKRCP01/json/1/1/RCP_NM=" + recipeName;
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(apiUrl, String.class);

        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray recipes = jsonResponse.getJSONObject("COOKRCP01").getJSONArray("row");

            if (recipes.length() > 0) {
                JSONObject recipe = recipes.getJSONObject(0);
                recipeDetails.setIngredientDetails(recipe.optString("RCP_PARTS_DTLS"));

                List<String> manualSteps = new ArrayList<>();
                List<String> manualImages = new ArrayList<>();

                for (int i = 1; i <= 20; i++) {
                    String manualStepKey = String.format("MANUAL%02d", i);
                    String manualImgKey = String.format("MANUAL_IMG%02d", i);

                    String manualStep = recipe.optString(manualStepKey);
                    String manualImg = recipe.optString(manualImgKey);

                    if (!manualStep.isEmpty()) {
                        manualSteps.add(manualStep);
                    }

                    if (!manualImg.isEmpty()) {
                        manualImages.add(manualImg);
                    }
                }

                recipeDetails.setManual(manualSteps);
                recipeDetails.setManualImg(manualImages);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
