package in.kbfg.ucb.login;

import in.kbfg.ucb.mybatis.entity.UserEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "Login", tags = "로그인, 로그인사용자, 접속망 관련")
@Controller
public class LoginController {

    @Value("${kbfg.connection.path.ip.important}")
    private List<String> importantIpList;

    @Autowired
    private LoginService loginService;

    String SSO_DEV = "10.210.168.54";	//개발 SSO URL
    String SSO_OP = "10.107.31.32";		//운영 SSO URL
    String agentId_DEV = "271";			//개발 SSO 서비스ID
    String agentId_OP = "256";			//운영 SSO 서비스ID

    String DEV_IP = "10.216.34.59";		//개발서버 IP
    String OP_IP = "10.216.37.87";		//운영서버 IP

    String AUTHORIZATION_URL;
    String agentId;
    String requestData = "id,pwdLastSet";
    private void setProp() {
//        if(OP_IP.equals(request.getServerName())){				//업무시스템이 운영서버일 경우 분기처리
//            AUTHORIZATION_URL = "http://" + SSO_OP + "/";
//            agentId = agentId_OP;
//        }
//        else{													//업무시스템이 운영서버가 아닐 경우 분기처리
            AUTHORIZATION_URL = "http://" + SSO_DEV + "/";
            agentId = agentId_DEV;
//        }
    }

    @ApiOperation("[ajax 호출이 아닌 리다이렉트 페이지] iSign+ 로그아웃 (개발모드에서 로그인 전 필수사용)")
    @GetMapping("/login/logout")
    public String logout(@ApiIgnore HttpSession session, @ApiIgnore Model model) {
        setProp();

        session.invalidate();
        String sendUrl = AUTHORIZATION_URL + "logout.html";
        System.out.println("logout");

        model.addAttribute("sendUrl", sendUrl);

        return "logout";
    }

    @ApiOperation("[ajax 호출이 아닌 리다이렉트 페이지] iSign+ 로그인 페이지")
    @GetMapping("/login/business")
    public String business(HttpSession session, Model model) {
        setProp();

        /**
         * penta라는 context path가 존재한다면 path + "origin value"로 입력한다.
         * ex) String path = "/penta";
         * session.setAttribute("SERVICE_BUSINESS_PAGE", path + "/sso/business.jsp");
         */
        String sendUrl = AUTHORIZATION_URL + "login.html";
        System.out.println("sendUrl : " + sendUrl);
        System.out.println("business page sessionId : " + session.getId());

        String CHECK_SERVER_URL = AUTHORIZATION_URL + "openapi/checkserver";
        String resultCode = "unknown code";
        String resultMessage = "server not response";
        boolean isErrorPage = false;
        try {
            int conTimeOut = 5000;
            int soTimeOut = 5000;
            HttpClient httpClient = new HttpClient();
            GetMethod getMethod = new GetMethod(CHECK_SERVER_URL);
            httpClient.setConnectionTimeout(conTimeOut);
            httpClient.setTimeout(soTimeOut);
            httpClient.executeMethod(getMethod);

            String httpResponse = getMethod.getResponseBodyAsString();
            getMethod.releaseConnection();

            System.out.println("httpResponse : " + httpResponse);

            JSONParser parser = new JSONParser();
            Object object = parser.parse(httpResponse);
            JSONObject jsonObject = (JSONObject)object;

            resultCode = (String)jsonObject.get("resultCode");
            resultMessage = (String)jsonObject.get("resultMessage");

            System.out.println("resultCode :" + resultCode);
            System.out.println("resultMessage :" + resultMessage);

            if(resultCode == null || resultCode.equals("000000") == false) {
                throw new Exception();
            }

        } catch (Exception e) {
            isErrorPage = true;
            session.setAttribute("isErrorPage", isErrorPage);
            session.setAttribute("resultCode", resultCode);
            session.setAttribute("resultMessage", resultMessage);

            System.out.println(e.toString());
            System.out.println(resultCode);
            System.out.println(resultMessage);
            System.out.println(e.toString());

            return "error";
//            response.sendRedirect("error.jsp");
        }
        model.addAttribute("agentId", agentId);
        model.addAttribute("sendUrl", sendUrl);
        return "business";
    }

    @ApiIgnore
    @PostMapping("/login/checkAuth")
    public String checkAuth(HttpServletRequest request, HttpSession session, Model model) {

        System.out.println("======= checkauth.jsp =========");
        String resultCode = request.getParameter("resultCode") == null ? "" : request.getParameter("resultCode");
        String secureToken = request.getParameter("secureToken") == null ? "" : request.getParameter("secureToken");
        String secureSessionId = request.getParameter("secureSessionId") == null ? "" : request.getParameter("secureSessionId");
        System.out.println("secureSessionId : " + secureSessionId);

        String clientIp = request.getRemoteAddr();

        String logOutUrl = AUTHORIZATION_URL + "logout";
        String logOutUrlAgentId = logOutUrl + "?agentId=" + agentId;
        String resultMessage = "";
        String resultData = "";
        String id = "";
        String returnUrl = "";

        session.setAttribute("secureSessionId", secureSessionId);

        Boolean isErrorPage=false;
        session.setAttribute("isErrorPage", isErrorPage);
        if (resultCode == "" || secureToken == "" || secureSessionId == "") {
            isErrorPage=true;
            session.setAttribute("isErrorPage", isErrorPage);
            System.out.println("error!!!");
//            response.sendRedirect("error.jsp");
            return "error";
        } else {

            if (resultCode.equals("000000")) {
                System.out.println("checkauth page sessionId : " + session.getId());
                if (AUTHORIZATION_URL == "") {

                    System.out.println("AUTHORIZATION_URL is empty");
                    return "error";
                }

                String TOKEN_AUTHORIZATION_URL = AUTHORIZATION_URL + "token/authorization";

                System.out.println("resultCode : " + resultCode);
                System.out.println("secureToken : " + secureToken);
                System.out.println("secureSessionId : " + secureSessionId);
                System.out.println("TOKEN_AUTHORIZATION_URL : " + TOKEN_AUTHORIZATION_URL);
                System.out.println("agentId : " + agentId);
                System.out.println("requestData : " + requestData);
                System.out.println("clientIp : " + clientIp);

                resultCode = "";
                resultMessage = "";
                resultData = "";
                returnUrl = "";


                try {
                    PostMethod postMethod = new PostMethod(TOKEN_AUTHORIZATION_URL);
                    NameValuePair[] nameValuePair = {
                            new NameValuePair("secureToken", secureToken),
                            new NameValuePair("secureSessionId", secureSessionId),
                            new NameValuePair("requestData", requestData),
                            new NameValuePair("agentId", agentId),
                            new NameValuePair("clientIP", clientIp)
                    };

                    postMethod.setQueryString(nameValuePair);

                    HttpClient httpClient = new HttpClient();
                    httpClient.executeMethod(postMethod);

                    String httpResponse = postMethod.getResponseBodyAsString();
                    postMethod.releaseConnection();

                    System.out.println("httpResponse : " + httpResponse);

                    JSONParser parser = new JSONParser();
                    Object object = parser.parse(httpResponse);
                    JSONObject jsonObject = (JSONObject)object;

                    object = jsonObject.get("user");
                    JSONObject requestDatajsonObject = (JSONObject)object;

                    resultCode = (String)jsonObject.get("resultCode");
                    resultMessage = (String)jsonObject.get("resultMessage");
                    returnUrl = (String)jsonObject.get("returnUrl");
                    boolean useCSMode  = jsonObject.get("useCSMode") == null ? false:Boolean.valueOf(jsonObject.get("useCSMode").toString());

                    if (resultCode.equals("000000")) {
                        String[] keys = requestData.split(",");

                        for (int i = 0; i < keys.length; i++) {
                            String temp = (String)requestDatajsonObject.get(keys[i]);
                            //temp 가 null이면 ldapData에 요청한 데이터가 있는지 검색한다.
                            if (temp == null) {
                                JSONObject ldapObject = (JSONObject)requestDatajsonObject.get("ldapData");
                                if (ldapObject != null) {
                                    System.out.println(ldapObject);
                                    temp = (String)ldapObject.get(keys[i]);
                                } else {
                                    continue;
                                }
                            }

                            if (resultData == "") {
                                resultData =  temp;
                                id = temp;
                            } else {
                                resultData = resultData + "," + temp;
                            }
                        }

                        if (useCSMode) {
                            returnUrl = AUTHORIZATION_URL + "token/saveToken.html";
                        }

                    } else {
                        if (resultCode.equals("310019")) {
                            String useISignPage = (String)jsonObject.get("useISignPage");
                            if (useISignPage.equals("true")) {
                                returnUrl = AUTHORIZATION_URL + returnUrl + "&secureSessionId=" + secureSessionId;
                            }
                        } else {
                            System.out.println("call error.jsp");
                            returnUrl = "error";
                        }

                    }

                    UserEntity user = UserEntity.builder().id(id).build();
                    loginService.mergeUserInfo(user);
                    session.setAttribute("userIdx", user.getIdx());

                    System.out.println("resultCode :" + resultCode);
                    System.out.println("resultMessage :" + resultMessage);
                    System.out.println("resultData :" + resultData);
                    System.out.println("returnUrl : " + returnUrl);

                    session.setAttribute("resultCode", resultCode);
                    session.setAttribute("resultMessage", resultMessage);
                    session.setAttribute("resultData", resultData);
                    session.setAttribute("userId", id);

                    if(returnUrl == null || returnUrl.equals("")) {
                        returnUrl = "/login/return";
                    }


                } catch (Exception e) {
                    System.out.println(e.toString());
                    isErrorPage = true;
                    session.setAttribute("isErrorPage", isErrorPage);
                    session.setAttribute("resultCode", resultCode);
                    session.setAttribute("resultMessage", resultMessage);
                    System.out.println(resultCode);
                    System.out.println(resultMessage);

//                    response.sendRedirect("error.jsp");
                    return "error";
                }
            }
        }
        model.addAttribute("agentId", agentId);
        model.addAttribute("resultCode", resultCode);
        model.addAttribute("secureSessionId", secureSessionId);
        model.addAttribute("returnUrl", returnUrl);
        return "checkAuth";
    }

    @ApiIgnore
    @PostMapping("/login/return")
    public String ssoReturn(HttpSession session, Model model) {

        String resultCode = session.getAttribute("resultCode") == null ? "" : session.getAttribute("resultCode").toString();
        String resultMessage = session.getAttribute("resultMessage") == null ? "" : session.getAttribute("resultMessage").toString();
        String resultData = session.getAttribute("resultData") == null ? "" : session.getAttribute("resultData").toString();
        boolean isErrorPage = session.getAttribute("isErrorPage") == null ? false : (Boolean)session.getAttribute("isErrorPage");

        //String logOutUrl = AUTHORIZATION_URL + "logout";
        //String logOutUrlAgentId = logOutUrl + "?agentId=" + agentId;

        String secureSessionId = session.getAttribute("secureSessionId") == null ? "" : session.getAttribute("secureSessionId").toString();
        String id = session.getAttribute("userId") == null ? "" : session.getAttribute("userId").toString();

        System.out.println("isErrorPage :  " + isErrorPage);
        System.out.println("resultCode :  " + resultCode);

        model.addAttribute("isErrorPage", isErrorPage);
        model.addAttribute("resultCode", resultCode);
        model.addAttribute("id", id);
        model.addAttribute("secureSessionId", secureSessionId);

        return "return";
    }

    @ApiOperation("접속망 구분 (important:중요단말망, business:업무망)")
    @PostMapping("/login/connectionPath")
    @ResponseBody
    public Map<String, Object> connectionPath(HttpServletRequest request) {
        Map<String, Object> returnMap = new HashMap<>();
        boolean isImportantIp = false;
        String requestIp = request.getRemoteAddr();
        for (String importantIp : importantIpList) {
            if (requestIp.contains(importantIp)) {
                isImportantIp = true;
                break;
            }
        }
        returnMap.put("connectionPath", isImportantIp ? "important" : "business");
        return returnMap;
    }

    @ApiOperation("로그인 사용자 정보")
    @PostMapping("/login/getUserInfo")
    @ResponseBody
    public Map<String, Object> getUserInfo(@ApiIgnore HttpServletRequest request, @ApiIgnore HttpSession session) {
        Map<String, Object> returnMap = new HashMap<>();

        String id = session.getAttribute("userId") == null ? "" : session.getAttribute("userId").toString();

        if (id.isEmpty()) {
            returnMap.put("isLogin", false);
            returnMap.put("userInfo", null);
        } else {
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", id);
            returnMap.put("isLogin", true);
            returnMap.put("userInfo", userInfo);
        }
        return returnMap;
    }

    @ApiOperation("[개발용] 로그인 세션 생성")
    @PostMapping("/login/setUserInfo")
    @ResponseBody
    public Map<String, Object> setUserInfo(@ApiIgnore HttpServletResponse response,
                                           @ApiIgnore HttpSession session,
                                           @Parameter(description = "사번") @RequestParam String id) {

        UserEntity user = loginService.getUserInfo(id);
        if (ObjectUtils.isEmpty(user)) {
            response.setStatus(401);
            return null;
        }
        session.setAttribute("userIdx", user.getIdx());
        session.setAttribute("userId", user.getId());

        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("idOk", true);
        return returnMap;
    }

}
