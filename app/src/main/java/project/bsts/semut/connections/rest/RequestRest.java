package project.bsts.semut.connections.rest;


import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import project.bsts.semut.R;
import project.bsts.semut.helper.PreferenceManager;
import project.bsts.semut.helper.TagsType;
import project.bsts.semut.pojo.Session;
import project.bsts.semut.setup.Constants;
import project.bsts.semut.utilities.GetCurrentDate;

public class RequestRest extends ConnectionHandler {

    protected static AsyncHttpClient mClient = new AsyncHttpClient();
    private String TAG = this.getClass().getSimpleName();
    private PreferenceManager preferenceManager;


    public RequestRest(Context context, IConnectionResponseHandler handler) {
        this.mContext = context;
        this.responseHandler = handler;
        preferenceManager = new PreferenceManager(mContext);
    }


    @Override
    public String getAbsoluteUrl(String relativeUrl) {
        return Constants.REST_BASE_URL + relativeUrl;
    }


    public void login(String email, String pass){
        RequestParams params = new RequestParams();
        params.put("Email", email);
        params.put("Password", pass);
        Log.i(TAG, params.toString());
        post(Constants.REST_USER_LOGIN, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                Log.i(TAG, "Sending request");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i(TAG, "Success");
                responseHandler.onSuccessRequest(response.toString(), Constants.REST_USER_LOGIN);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, e, errorResponse);
                Log.e(TAG, "Failed");
                responseHandler.onSuccessRequest(String.valueOf(statusCode), Constants.REST_ERROR);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                Log.i(TAG, "Disconnected");
            }

        }, mClient);
    }


    public void insertPost(int id, int subID, String dateExp, String desc){
        Session session = new Gson().fromJson(preferenceManager.getString(Constants.PREF_SESSION_ID), Session.class);
        String[] types = TagsType.get(id, subID);
        RequestParams params = new RequestParams();
        params.put("SessionID", session.getSessionID());
        params.put("IDtype", id+1);
        params.put("IDsub", subID+1);
        params.put("Type", types[0]);
        params.put("SubType", types[1]);
        params.put("Date", GetCurrentDate.now());
        params.put("Description", desc);
        params.put("Latitude", preferenceManager.getDouble(Constants.ENTITY_LATITUDE, 0));
        params.put("Longitude", preferenceManager.getDouble(Constants.ENTITY_LONGITUDE, 0));
        params.put("Expire", dateExp);
        Log.i(TAG, params.toString());
        post(Constants.REST_INSERT_POST, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                Log.i(TAG, "Sending request");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i(TAG, "Success");
                responseHandler.onSuccessRequest(response.toString(), Constants.REST_INSERT_POST);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, e, errorResponse);
                Log.e(TAG, "Failed");
                responseHandler.onSuccessRequest(String.valueOf(statusCode), Constants.REST_ERROR);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                Log.i(TAG, "Disconnected");
            }

        }, mClient);
    }
}
