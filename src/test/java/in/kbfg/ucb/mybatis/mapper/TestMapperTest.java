package in.kbfg.ucb.mybatis.mapper;

import in.kbfg.ucb.mybatis.entity.TestEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TestMapperTest {

    @Autowired
    TestMapper testMapper;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void helloName() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "DGK");
        String result = testMapper.helloName(params);
        System.out.println("result: " + result);
        assertEquals("Hello DGK", result);
    }

    @Test
    void testModel() {
        TestEntity paramModel = new TestEntity();
        paramModel.setIdx(1);
        paramModel.setTestName("DGK");
        List<TestEntity> resultList = testMapper.testModel(paramModel);

        TestEntity expectModel = new TestEntity();
        expectModel.setIdx(3);
        expectModel.setTestName("Hello DGK");
        for (TestEntity model : resultList) {
            System.out.println(model);
            assertEquals(expectModel, model);
        }
    }
}