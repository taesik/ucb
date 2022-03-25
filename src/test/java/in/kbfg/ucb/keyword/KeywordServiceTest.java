package in.kbfg.ucb.keyword;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class KeywordServiceTest {

    @Autowired
    KeywordService keywordService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getRecommendKeyword() {
        List<String> keywordList = keywordService.getRecommendKeywordList();
        System.out.println("recommend keywordList: " + keywordList);
        assertFalse(ObjectUtils.isEmpty(keywordList));
    }

    @Test
    void getUserKeyword() {
        List<String> keywordList = keywordService.getUserKeywordList(1);
        System.out.println("user keywordList: " + keywordList);
//        assertFalse(ObjectUtils.isEmpty(keywordList));
    }

    @Ignore
    @Test
    void updateKeyword() {
        int userIdx = 1;
        int updateCount = keywordService.updateKeywordList(userIdx, Arrays.asList("KB카드", "통합 상품"));
        System.out.println("keyword updateCount: " + updateCount);
        assertTrue(updateCount > 0);
    }
}