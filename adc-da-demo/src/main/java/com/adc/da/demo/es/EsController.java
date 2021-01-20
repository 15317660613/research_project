package com.adc.da.demo.es;

import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adc.da.demo.service.HelloSender;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/test/es")
@Api(description = "|es测试|")
public class EsController {

	@Autowired
	private HelloSender helloSender;

	@Autowired
	private TransportClient esClient;

	@ApiOperation(value = "|es测试|")
	@GetMapping("")
	public ResponseMessage page() throws Exception {
        SearchResponse response = esClient.prepareSearch().get();
        for (SearchHit hit : response.getHits()) {
            Map<String,Object> map = hit.getSourceAsMap();
            System.out.println(map);
        }
		return Result.success();
	}

}
