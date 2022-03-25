package in.kbfg.ucb.news;


import com.google.gson.Gson;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;


@RestController
//@RequestMapping("api/news")
public class NewsController {

	//private static RestTemplate restTemplate;
	
	@GetMapping("/helloworld")
	public String getHellowWorld() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("value1", 1);
		map.put("value2", "좋아");
		map.put("value3", "완료");

		Gson gson = new Gson();
		return gson.toJson(map);
	}
	
//	@GetMapping("/restTest")
//	public String getRestTest(@RequestParam MultiValueMap<String,String> allRequestParams) {
//		
//		String baseUrl = "http://utlds-khsys.azurewebsites.net/api/utlds/GetEvent_TypeCount"; //?date=2021-09-01&jbId=1&tlId=2";
//		
//		URI requestUrl= UriComponentsBuilder.fromUriString(baseUrl)  			// Build the base link
//											.queryParams(allRequestParams)
//										    .build()                            // Build the URL
//										    .encode()                           // Encode any URI items that need to be encoded
//										    .toUri();                           // Convert to URI
//
//		RestTemplate restTemplate = new RestTemplate();
//		String resp = restTemplate.getForObject(requestUrl, String.class);
//		return resp;
////		return "OK";
//	}
	
//	@PostMapping("/documents")
//	public String getDocuments(@RequestParam MultiValueMap<String,String> allRequestParams) {
//
//		String baseUrl = "http://10.81.31.15:7474//api/v1/documents";
//
//		URI requestUrl= UriComponentsBuilder.fromUriString(baseUrl)  			// Build the base link
//											.queryParams(allRequestParams)
//										    .build()                            // Build the URL
//										    .encode()                           // Encode any URI items that need to be encoded
//										    .toUri();                           // Convert to URI
//
//		RestTemplate restTemplate = new RestTemplate();
//		String resp = restTemplate.getForObject(requestUrl, String.class);
//		return resp;
//	}

	@PostMapping("/documents")
	public String getDocuments(@RequestParam String[] entities, @RequestParam int skip, @RequestParam int limit) {

		String baseUrl = "http://10.81.31.15:7474/api/v1/documents";

		URI requestUrl= UriComponentsBuilder.fromUriString(baseUrl)  			// Build the base link
//											.queryParams(allRequestParams)
				.queryParam("entities", entities)
				.queryParam("skip", skip)
				.queryParam("limit", limit)
										    .build()                            // Build the URL
										    .encode()                           // Encode any URI items that need to be encoded
										    .toUri();                           // Convert to URI

		RestTemplate restTemplate = new RestTemplate();
		String resp = restTemplate.getForObject(requestUrl, String.class);
		return resp;
	}

	@PostMapping("/documents/{id}")
	public String getDocument(@PathVariable("id") String id) {
		String baseUrl = "http://10.81.31.15:7474/api/v1/documents/" + id;
		
		URI requestUrl= UriComponentsBuilder.fromUriString(baseUrl)  			// Build the base link
										    .build()                            // Build the URL
										    .encode()                           // Encode any URI items that need to be encoded
										    .toUri();                           // Convert to URI

		RestTemplate restTemplate = new RestTemplate();
		String resp = restTemplate.getForObject(requestUrl, String.class);
		return resp;
	}
}
