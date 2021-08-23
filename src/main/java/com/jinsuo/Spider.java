package com.jinsuo;

import com.jinsuo.bean.ImageCollectionInfo;
import com.jinsuo.config.ConfigReader;
import com.jinsuo.utils.ImagesUtil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.List;

/**
 * 启动类
 */
public class Spider {
    public static void main(String[] args) {
        String baseUrl = ConfigReader.searchBaseUrl;
        for (int i = 0; i <= ConfigReader.pageIndexMax; i++) {
            String indexUrl = baseUrl + "?page=" + i + "&searchid=" + ConfigReader.searchId;
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();

            List<String> indexHtmlList = ImagesUtil.getIndexHtmlList(httpClient, indexUrl);
            for (String imagesHtmlUrl : indexHtmlList) {
                ImageCollectionInfo imageCollectionInfo = ImagesUtil.getImageUrlList(httpClient, imagesHtmlUrl);
                //执行下载
                ImagesUtil.downloadImages(httpClient, imageCollectionInfo);
            }
        }
    }


}
