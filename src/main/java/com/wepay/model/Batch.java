package com.wepay.model;

import java.io.IOException;
import java.util.Map;

import org.json.*;

import com.wepay.WePay;
import com.wepay.net.WePayResource;
import com.wepay.exception.WePayException;
import com.wepay.model.data.*;

public class Batch extends WePayResource {
	
	protected String call;
	protected String reference_id;
	protected Map response;
	
	public static Batch[] create(BatchData[] calls, String access_token) throws JSONException, IOException, WePayException {
		JSONObject batch = new JSONObject();
		batch.put("client_id", WePay.client_id);
		batch.put("client_secret", WePay.client_secret);
		JSONArray callsArray = new JSONArray();
		for (int i = 0; i < calls.length; i++) {
			JSONObject call = new JSONObject();
			if (calls[i].parameters != null) call.put("parameters", calls[i].parameters);
			if (calls[i].reference_id != null) call.put("reference_id", calls[i].reference_id);
			if (calls[i].authorization != null) call.put("authorization", calls[i].authorization);
			String callString = "/".concat(calls[i].callClass.toLowerCase());
			if (calls[i].callFunction.endsWith("()")) calls[i].callFunction = calls[i].callFunction.substring(0, calls[i].callFunction.indexOf("("));
			if (!calls[i].callFunction.equalsIgnoreCase("fetch")) callString = callString.concat("/").concat(calls[i].callFunction.toLowerCase());
			call.put("call", callString);
			callsArray.put(call);
		}
		batch.put("calls", callsArray);
		System.out.println("batch: " + batch);
		JSONObject object = new JSONObject(request("/batch/create", batch, access_token));
		JSONArray responses = object.getJSONArray("calls");
		Batch[] response = new Batch[responses.length()];
		for (int i = 0; i < response.length; i++) {
			Batch b = gson.fromJson(responses.get(i).toString(), Batch.class);
			response[i] = b;
		}
		return response;
	}
	
}
