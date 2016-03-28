package com.edisonwang.demo.tumblrsearch.service;

import android.os.Bundle;

import com.edisonwang.demo.tumblrsearch.service.SearchAction_.PsSearchAction;
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
import com.edisonwang.ps.lib.EventServiceImpl;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @author edi
 */
@RequestAction
@RequestActionHelper(variables = {
        @ClassField(name = "tag", kind = @Kind(clazz = String.class), required = true)
})
@EventProducer(generated = {
        @EventClass(classPostFix = "Success", fields = {
                @ParcelableClassField(name = "results", kind = @Kind(clazz = Tumblr.Post[].class))
        }),
        @EventClass(classPostFix = "Failure", fields = {
                @ParcelableClassField(name = "message", kind = @Kind(clazz = String.class))
        })
})
public class SearchAction implements Action {

    @Override
    public ActionResult processRequest(EventServiceImpl service, ActionRequest actionRequest, Bundle bundle) {
        SearchActionHelper helper = PsSearchAction.helper(actionRequest.getArguments(getClass().getClassLoader()));
        Tumblr tumblr = TumblrUtil.getTumblrService();
        Call<Tumblr.TagSearchResult> result = tumblr.tagged(helper.tag(), TumblrUtil.apiKey());
        try {
            Response<Tumblr.TagSearchResult> resp = result.execute();
            Tumblr.TagSearchResult obj = resp.body();
            if (obj != null) {
                return new SearchActionEventSuccess(obj.response);
            } else {
                return new SearchActionEventSuccess(new Tumblr.Post[]{});
            }
        } catch (IOException e) {
            return new SearchActionEventFailure(e.getMessage());
        }
    }
}
