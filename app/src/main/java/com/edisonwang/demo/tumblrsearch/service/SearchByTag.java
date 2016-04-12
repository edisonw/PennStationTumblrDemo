package com.edisonwang.demo.tumblrsearch.service;

import android.content.Context;

import com.edisonwang.demo.tumblrsearch.service.SearchByTag_.PsSearchByTag;
import com.edisonwang.ps.annotations.ClassField;
import com.edisonwang.ps.annotations.EventClass;
import com.edisonwang.ps.annotations.EventProducer;
import com.edisonwang.ps.annotations.Kind;
import com.edisonwang.ps.annotations.ParcelableClassField;
import com.edisonwang.ps.annotations.RequestAction;
import com.edisonwang.ps.annotations.RequestActionHelper;
import com.edisonwang.ps.lib.Action;
import com.edisonwang.ps.lib.ActionRequest;
import com.edisonwang.ps.lib.ActionResult;
import com.edisonwang.ps.lib.RequestEnv;

import java.io.IOException;

import retrofit2.Call;

/**
 * @author edi
 */
@RequestActionHelper(variables = {
        @ClassField(name = "tag", kind = @Kind(clazz = String.class), required = true)
})
@EventProducer(generated = {
        @EventClass(classPostFix = "Success", fields = {
                @ParcelableClassField(name = "results", kind = @Kind(clazz = Post[].class))
        }),
        @EventClass(classPostFix = "Failure")
})
@RequestAction
public class SearchByTag implements Action {

    @Override
    public ActionResult processRequest(Context context, ActionRequest request, RequestEnv env) {
        final TagSearchResult obj = searchByTag(getTag(request));
        if (obj != null) {
            return new SearchByTagEventSuccess(obj.response);
        } else {
            return new SearchByTagEventFailure();
        }
    }

    private String getTag(ActionRequest request) {
        return PsSearchByTag.helper(request.getArguments(this)).tag();
    }

    public static TagSearchResult searchByTag(final String tag) {
        Call<TagSearchResult> result = TumblrUtil.getTumblrService().tagged(tag, TumblrUtil.apiKey());
        try {
            return result.execute().body();
        } catch (IOException e) {
            return null;
        }
    }

}
