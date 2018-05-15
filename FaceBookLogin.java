package com.ettie.fblogin;

import android.os.Bundle;

import com.ettie.utils.GSonUtils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

public class FaceBookLogin {

    private CallbackManager callbackManager;

    public FaceBookLogin() {
        this.callbackManager = CallbackManager.Factory.create();
    }

    public void createLogin(final LoginCallback loginCallback) {
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        // Application code
                                        FacebookLoginResponse faceBookLogin = new GSonUtils().getGson().fromJson(String.valueOf(response.getRawResponse()), FacebookLoginResponse.class);

                                        loginCallback.onSuccess(faceBookLogin);
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,birthday,first_name,last_name,picture.type(large)");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        loginCallback.onCancel();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        loginCallback.onError(exception);
                    }
                });
    }

    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

    public interface LoginCallback {
        void onSuccess(FacebookLoginResponse response);

        void onCancel();

        void onError(FacebookException exception);
    }
}