package KNU.YoriZori.service;

import KNU.YoriZori.domain.*;
import KNU.YoriZori.repository.FridgeRepository;
import KNU.YoriZori.repository.PutInRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PutInServiceTest {

    @Autowired
    private PutInService putInService;

    @Autowired
    private UserService userService;

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private FridgeRepository fridgeRepository;

    @Autowired
    private PutInRepository putInRepository;

    @Test
    public void addIngredientToFridgeTest() {
        // Given
        User user = new User();
        user.setName("testUserx");
        user.setPassword("password");
        user.setNickname("testNickname");
        userService.join(user);

        Ingredient ingredient = new Ingredient();
        ingredient.setName("TestIngredient1");
        ingredient.setDefaultExpDate(7); // 7일 소비기한 설정
        ingredientService.saveIngredient(ingredient);

        LocalDateTime putDate = LocalDateTime.now();
        StoragePlace storagePlace = StoragePlace.COLD;
        Fridge fridge = user.getFridge();

        // When
        putInService.addIngredientToFridge(fridge.getId(), ingredient.getId(), putDate, storagePlace);

        // Then
        List<PutIn> putIns = putInRepository.findAllByFridgeId(fridge.getId());
        assertFalse(putIns.isEmpty()); // 냉장고에 하나 이상의 재료가 추가되었는지 확인
        PutIn result = putIns.get(0); // 테스트를 위해 추가된 첫 번째 재료를 가져옵니다.
        assertEquals(ingredient.getId(), result.getIngredient().getId()); // 재료 ID가 예상한 값과 일치하는지 확인
        assertEquals(putDate, result.getPutDate()); // 추가 날짜가 예상한 값과 일치하는지 확인
        assertEquals((putDate.plusDays(ingredient.getDefaultExpDate())), result.getExpDate()); // 추가 날짜가 예상한 값과 일치하는지 확인
        assertEquals(storagePlace, result.getStoragePlace()); // 보관장소가 예상한 값과 일치하는지 확인

    }

    @Test
    public void DdayTest(){
        // Given: 테스트용 재료 생성
        // Given
        PutIn putIn = new PutIn();
        putIn.setExpDate(LocalDateTime.now().plusDays(5)); // 소비 기한을 오늘로부터 5일 뒤로 설정

        // When
        putInService.updatePutIn(putIn);

        // Then
        assertEquals(5, putIn.getDDay()); // D-Day가 올바르게 계산되었는지 검증
    }
}