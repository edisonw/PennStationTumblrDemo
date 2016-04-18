package com.edisonwang.demo.tumblrsearch.service;

import android.content.Context;

import com.edisonwang.demo.tumblrsearch.service.SearchByTag_.PsSearchByTag;
import com.edisonwang.demo.tumblrsearch.service.events.SearchByTagFailure;
import com.edisonwang.demo.tumblrsearch.service.events.SearchByTagSuccess;
import com.edisonwang.ps.annotations.Action;
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

import java.io.IOException;

import retrofit2.Call;

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
        @Event(postFix = "Failure")
})
@Action
public class SearchByTag extends FullAction {
    @Override
    protected ActionResult process(Context context, ActionRequest actionRequest, RequestEnv requestEnv) throws Throwable {
        final TagSearchResult obj = searchByTag(getTag(actionRequest));
        if (obj != null) {
            return new SearchByTagSuccess(obj.response);
        } else {
            return new SearchByTagFailure();
        }
    }

    public static TagSearchResult searchByTag(final String tag) {
        Call<TagSearchResult> result = TumblrUtil.getTumblrService().tagged(tag, TumblrUtil.apiKey());
        try {
            return result.execute().body();
        } catch (IOException e) {
            return null;
        }
    }

    private String getTag(ActionRequest request) {
        return PsSearchByTag.helper(request.getArguments(this)).tag();
    }

    @Override
    protected ActionResult onError(Context context, ActionRequest actionRequest, RequestEnv requestEnv, Throwable throwable) {
        return new SearchByTagFailure();
    }
}
