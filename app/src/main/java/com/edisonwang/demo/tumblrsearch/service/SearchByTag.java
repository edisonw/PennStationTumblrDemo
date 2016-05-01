package com.edisonwang.demo.tumblrsearch.service;

import android.content.Context;
import android.util.Log;

import com.edisonwang.demo.tumblrsearch.service.SearchByTag_.PsSearchByTag;
import com.edisonwang.demo.tumblrsearch.service.events.SearchByTagFailure;
import com.edisonwang.demo.tumblrsearch.service.events.SearchByTagSuccess;
import com.edisonwang.ps.annotations.ActionHelper;
import com.edisonwang.ps.annotations.Event;
import com.edisonwang.ps.annotations.EventProducer;
import com.edisonwang.ps.annotations.Field;
import com.edisonwang.ps.annotations.Kind;
import com.edisonwang.ps.annotations.ParcelableField;
import com.edisonwang.ps.lib.ActionRequest;
import com.edisonwang.ps.lib.ActionResult;
import com.edisonwang.ps.lib.FullAction;
import com.edisonwang.ps.lib.RequestEnv;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @author edi
 */
@ActionHelper(args = {
        @Field(name = "tag", kind = @Kind(clazz = String.class), required = true)
})
@EventProducer(generated = {
        @Event(postFix = "Success", fields = {
                @ParcelableField(name = "results", kind = @Kind(clazz = Post[].class))
        }),
        @Event(postFix = "Failure", fields = {
                @ParcelableField(name = "message", kind = @Kind(clazz = String.class))
        })
})
@com.edisonwang.ps.annotations.Action
public class SearchByTag extends FullAction {

    @Override
    protected ActionResult process(Context context, ActionRequest request, RequestEnv env) throws Throwable {
        final String tag = PsSearchByTag.helper(request.getArguments(this)).tag();
        Call<TagSearchResult> call = TumblrUtil.getTumblrService().tagged(tag, TumblrUtil.apiKey());
        Response<TagSearchResult> resp = call.execute();
        TagSearchResult body = resp.body();
        if (resp.code() == 401) {
            return new SearchByTagFailure("You need to enter a valid API Key in TumblrUtil.java first.");
        } else {
            return new SearchByTagSuccess(body.response);
        }
    }

    @Override
    protected ActionResult onError(Context context, ActionRequest request, RequestEnv env, Throwable e) {
        return new SearchByTagFailure("Something has went wrong: " + e.getMessage());
    }

}
