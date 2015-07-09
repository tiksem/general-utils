package com.utils.framework.network;

import com.utils.framework.io.Network;
import com.utils.framework.network.RequestExecutor;

import java.io.IOException;
import java.util.Map;

/**
 * Created by CM on 6/16/2015.
 */
public class GetRequestExecutor implements RequestExecutor {
    private String encoding;

    public GetRequestExecutor(String encoding) {
        this.encoding = encoding;
    }

    public GetRequestExecutor() {

    }

    @Override
    public String executeRequest(String url, Map<String, Object> args) throws IOException {
        return Network.executeGetRequest(url, args, encoding);
    }
}
