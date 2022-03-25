package in.kbfg.ucb.keyword;

import in.kbfg.ucb.dto.KeywordListDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Api(value = "Keyword", tags = "키워드 조회/관리")
@Controller
@RequestMapping("/keyword")
public class KeywordController {

    @Autowired
    KeywordService keywordService;

    @ApiOperation("나의 키워드 정보 조회")
    @PostMapping("/getUserKeywordList")
    @ResponseBody
    public Map<String, Object> getUserKeywordList(@ApiIgnore HttpSession session) {
        Map<String, Object> returnMap = new HashMap<>();
        int userIdx = (int)session.getAttribute("userIdx");
        returnMap.put("keywordList", keywordService.getUserKeywordList(userIdx));
        return returnMap;
    }

    @ApiOperation("추천 키워드 정보 조회")
    @PostMapping("/getRecommendKeywordList")
    @ResponseBody
    public Map<String, Object> getRecommendKeywordList(@ApiIgnore HttpSession session) {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("keywordList", keywordService.getRecommendKeywordList());
        return returnMap;
    }

    @ApiOperation("키워드 업데이트")
    @PostMapping("/updateKeywordList")
    @ResponseBody
    public Map<String, Object> updateKeyword(@ApiIgnore HttpSession session,
                                             @RequestBody KeywordListDto keywordList) {
        Map<String, Object> returnMap = new HashMap<>();
        int userIdx = (int)session.getAttribute("userIdx");
        keywordService.updateKeywordList(userIdx, keywordList.getKeywordList());
        returnMap.put("isOK", true);
        returnMap.put("errorMessage", "");
        return returnMap;
    }

}
