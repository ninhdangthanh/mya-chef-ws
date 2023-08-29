package vn.com.ids.myachef.business.zalo.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;

import kong.unirest.HttpResponse;
import kong.unirest.HttpStatus;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.config.ZaloConfiguration;
import vn.com.ids.myachef.business.zalo.article.dto.normal.WrapperNormalArticle;
import vn.com.ids.myachef.business.zalo.article.dto.video.WrapperVideoArticle;
import vn.com.ids.myachef.business.zalo.article.media.WrapperMedia;
import vn.com.ids.myachef.business.zalo.article.response.ArticleIdResponse;
import vn.com.ids.myachef.business.zalo.article.response.ArticleTokenResponse;
import vn.com.ids.myachef.business.zalo.article.response.video.WrapperArticleVideoResponse;
import vn.com.ids.myachef.business.zalo.social.ZaloAccount;

@Service("zaloService")
@Slf4j
public class ZaloService {

    private static final String APPLICATION_JSON = "application/json";
    private static final String CONTENT_TYPE = "Content-Type";

    @Autowired
    private ZaloConfiguration zaloConfiguration;

    /* get zalo article info */
    public ArticleTokenResponse uploadVideo(String accessToken, File file) {
        return Unirest.post(String.format(zaloConfiguration.getZaloArticleVideoUploadUrl(), accessToken)) //
                .field("file", file) //
                .asObject(ArticleTokenResponse.class) //
                .getBody();
    }

    public WrapperArticleVideoResponse getVideoUploadStatus(String accessToken, String videoArticleToken) {
        return Unirest.get(String.format(zaloConfiguration.getZaloArticleVideoVerifyUrl(), accessToken, videoArticleToken)) //
                .asObject(WrapperArticleVideoResponse.class) //
                .getBody();
    }

    public WrapperNormalArticle getNormalArticleDetail(String accessToken, String articleId) {
        return Unirest.get(String.format(zaloConfiguration.getZaloArticleDetailUrl(), accessToken, articleId)) //
                .asObject(WrapperNormalArticle.class) //
                .getBody();
    }

    public WrapperVideoArticle getVideoArticleDetail(String accessToken, String articleId) {
        return Unirest.get(String.format(zaloConfiguration.getZaloArticleDetailUrl(), accessToken, articleId)) //
                .asObject(WrapperVideoArticle.class) //
                .getBody();
    }

    public WrapperMedia findAllArticle(String accessToken, String type, int offset, int limit) {
        return Unirest.get(String.format(zaloConfiguration.getZaloArticleListUrl(), accessToken, offset, limit, type)) //
                .asObject(WrapperMedia.class) //
                .getBody();
    }

    public ArticleTokenResponse createNormalArticle(String accessToken, String articleObject) {
        return Unirest.post(String.format(zaloConfiguration.getZaloArticleCreateUrl(), accessToken)) //
                .header(CONTENT_TYPE, APPLICATION_JSON) //
                .body(articleObject) //
                .asObject(ArticleTokenResponse.class) //
                .getBody();
    }

    public ArticleTokenResponse updateArticle(String accessToken, String articleObject) {
        return Unirest.post(String.format(zaloConfiguration.getZaloArticleUpdateUrl(), accessToken)) //
                .header(CONTENT_TYPE, APPLICATION_JSON) //
                .body(articleObject) //
                .asObject(ArticleTokenResponse.class) //
                .getBody();
    }

    public ArticleTokenResponse deleteArticle(String accessToken, JsonObject articleIdObject) {
        return Unirest.post(String.format(zaloConfiguration.getZaloArticleDeleteUrl(), accessToken)) //
                .header(CONTENT_TYPE, APPLICATION_JSON) //
                .body(articleIdObject) //
                .asObject(ArticleTokenResponse.class) //
                .getBody();
    }

    public ArticleIdResponse getArticleIdAfterCreated(String accessToken, JsonObject articleTokenObject) {
        int reTry = 0;
        ArticleIdResponse articleIdResponse = null;

        HttpResponse<ArticleIdResponse> response = null;
        int status = HttpStatus.BAD_REQUEST;
        while (reTry < 3 && status != HttpStatus.OK) {
            reTry++;
            try {
                response = Unirest.post(String.format(zaloConfiguration.getZaloArticleVerifyUrl(), accessToken)) //
                        .header(CONTENT_TYPE, APPLICATION_JSON) //
                        .body(articleTokenObject) //
                        .asObject(ArticleIdResponse.class);
                status = response.getStatus();
                if (status == 200) {
                    articleIdResponse = response.getBody();
                }
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        }

        return articleIdResponse;
    }

    /* Get zalo account info */
    public ZaloAccount getZaloAccountInfo(String accessToken) {
        return Unirest.get(zaloConfiguration.getZaloAccountInfoUrl()) //
                .header("access_token", accessToken) //
                .asObject(ZaloAccount.class) //
                .getBody();
    }
}
