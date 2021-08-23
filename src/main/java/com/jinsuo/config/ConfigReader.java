package com.jinsuo.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * 配置读取类
 */
public class ConfigReader {
    private static Config config = ConfigFactory.load();
    public static String websiteUrl = config.getString("websiteUrl");
    public static String searchBaseUrl = config.getString("searchBaseUrl");
    public static int pageIndexMax = config.getInt("pageIndexMax");
    public static int searchId = config.getInt("searchId");
    public static String localImagesDirPrefix = config.getString("localImagesDirPrefix");
    public static String imageSuffix = config.getString("imageSuffix");

}
