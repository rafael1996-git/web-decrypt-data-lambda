package com.talentport.serverless.responses;

import com.talentport.serverless.models.TalentportResponse;
import com.talentport.serverless.utilities.TalentportObjectMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * POJO containing response object for API Gateway.
 */
public class GatewayResponse {

    private final String REQUEST_TYPE = "application/json;charset=UTF-8";
    private Object body;
    private Map<String, String> headers;
    private final int statusCode = 200;


    public GatewayResponse(boolean succes, Object data, String messageException) {
        headers = new HashMap<>();
        headers.put("Content-Type", REQUEST_TYPE);
        headers.put("X-Custom-Header", REQUEST_TYPE);
        //body = data;
        body = TalentportObjectMapper.convertValue(new TalentportResponse(succes, data, messageException), Object.class);

    }

    public Object getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
