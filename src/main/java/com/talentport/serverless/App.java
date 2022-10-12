package com.talentport.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.talentport.serverless.models.TalentportRequest;
import com.talentport.serverless.responses.GatewayExceptionResponse;
import com.talentport.serverless.responses.GatewayResponse;
import com.talentport.serverless.utilities.TalentportEncriptorKey;
import com.talentport.serverless.utilities.TalentportObjectMapper;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<Object, Object> {

    enum ERROR {
        DATA_CANT_BE_EMPTY,
        STRING_CANT_BE_DECRYPTED
    }

    public Object handleRequest(final Object input, final Context context) {
        try {
            String inputString = parseObjectToString(input);
            TalentportRequest object = TalentportObjectMapper.getObjectMapper().readValue(inputString, TalentportRequest.class);
            if (object.getData() == null)
                return new GatewayResponse(false, null, ERROR.DATA_CANT_BE_EMPTY.name());
            String decryptedString = TalentportEncriptorKey.decode(object.getData());
            if (decryptedString == null)
                return new GatewayResponse(false, null, ERROR.STRING_CANT_BE_DECRYPTED.name());
            return new GatewayResponse(true, TalentportObjectMapper.getObjectMapper().readValue(decryptedString, Object.class), null);
        } catch (Exception e) {
            e.printStackTrace();
            return new GatewayExceptionResponse(e.getMessage());
        }

    }

    /*
     * El input de la Lambda puede ser de tipo String, ArrayList  o LinkedHasMap, para trabajar de manera generica lo parseamos a String
     *
     */
    private String parseObjectToString(Object input) throws Exception {
        String jsonInString = TalentportObjectMapper.getObjectMapper().writeValueAsString(input);
        return jsonInString;
    }

}
