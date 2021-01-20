/*
 *
 * (c) Copyright Ascensio System Limited 2010-2018
 *
 * The MIT License (MIT)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
*/

package com.adc.da.onlyoffice.helpers;

import com.adc.da.onlyoffice.entity.FileType;
import com.adc.da.onlyoffice.utils.Stream2FileUtil;
import org.primeframework.jwt.Signer;
import org.primeframework.jwt.Verifier;
import org.primeframework.jwt.domain.JWT;
import org.primeframework.jwt.hmac.HMACSigner;
import org.primeframework.jwt.hmac.HMACVerifier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DocumentManager {

    private DocumentManager(){

    }

    public static long getMaxFileSize() {

        long size;

        try {
            size = Long.parseLong(ConfigManager.getProperty("filesize-max"));
        } catch (Exception ex) {
            size = 0;
        }
        return size > 0 ? size : 5 * 1024 * 1024;
    }

    public static List<String> getFileExts() {

        List<String> res = new ArrayList<>();
        res.addAll(getViewedExts());
        res.addAll(getEditedExts());
        res.addAll(getConvertExts());
        return res;
    }

    public static List<String> getViewedExts() {

        String exts = ConfigManager.getProperty("files.docservice.viewed-docs");
        return Arrays.asList(exts.split("\\|"));
    }

    public static List<String> getEditedExts() {

        String exts = ConfigManager.getProperty("files.docservice.edited-docs");
        return Arrays.asList(exts.split("\\|"));
    }

    public static List<String> getConvertExts() {

        String exts = ConfigManager.getProperty("files.docservice.convert-docs");
        return Arrays.asList(exts.split("\\|"));
    }

    public static String curUserHostAddress(String userAddress) {

        if(userAddress == null) {
            try {
                userAddress = InetAddress.getLocalHost().getHostAddress();
            } catch (Exception ex) {
                userAddress = "";
            }
        }

        return userAddress.replaceAll("[^0-9a-zA-Z.=]", "_");
    }

    public static String storagePath(String fileName, String userAddress) {

        String serverPath = "";

        String storagePath = ConfigManager.getProperty("storage-folder");

        String hostAddress = curUserHostAddress(userAddress);

        String directory = serverPath + File.separator + storagePath + File.separator;


        File file = new File(directory);
        if (!file.exists()) {
            file.mkdir();
        }
        directory = directory + hostAddress + File.separator;

        file = new File(directory);
        if (!file.exists()) {
            file.mkdir();
        }

        return directory + fileName;
    }

    public static String getCorrectName(String fileName) {

        String baseName = FileUtility.getFileNameWithoutExtension(fileName);
        String ext = FileUtility.getFileExtension(fileName);
        String name = baseName + ext;

        File file = new File(storagePath(name, null));
        for (int i = 1; file.exists(); i++) {
            name = baseName + " (" + i + ")" + ext;
            file = new File(storagePath(name, null));
        }
        return name;
    }

    public static String createDemo(String fileExt) throws Exception {

        String demoName = "sample." + fileExt;
        String fileName = getCorrectName(demoName);

        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(demoName);
        File file = new File(storagePath(fileName, null));

        Stream2FileUtil.doSave(stream,file);

        return fileName;
    }

    public static String getFileUri(String fileName) {

            String serverPath = getServerUrl();

            String storagePath = ConfigManager.getProperty("storage-folder");
            String hostAddress = curUserHostAddress(null);
            // 源程序将+转义为 20%
            String filePath = serverPath + "/" + storagePath + "/" + hostAddress + "/" + fileName;
            return filePath;

    }

    public static String getServerUrl() {
        String serverHost  =  ConfigManager.getProperty("serverHost");
    	return serverHost;
    }

    public static String getCallback(String fileName) {
        String serverPath = getServerUrl();
        String hostAddress = curUserHostAddress(null);
        try {
            String  string= "?type=track&fileName=" + fileName + "&userAddress=";
            String query = string + URLEncoder.encode(hostAddress, java.nio.charset.StandardCharsets.UTF_8.toString());
            return serverPath + "/IndexServlet" + query;
        }catch (UnsupportedEncodingException e){
            return "";
        }
    }

    public static String getInternalExtension(FileType fileType){
        if (fileType.equals(FileType.TEXT)){
            return ".docx";
        }
        if (fileType.equals(FileType.SPREADSHEET)){
            return ".xlsx";
        }
        if (fileType.equals(FileType.PRESENTATION)){
            return ".pptx";
        }

        return ".docx";
    }

    public static String createToken(Map<String, Object> payloadClaims){
        try{
            Signer signer = HMACSigner.newSHA256Signer(getTokenSecret());
            JWT jwt = new JWT();
            for (String key : payloadClaims.keySet()){
                jwt.addClaim(key, payloadClaims.get(key));
            }
            return JWT.getEncoder().encode(jwt, signer);
        }
        catch (Exception e){
            return "";
        }
    }

    public static JWT readToken(String token){
        try{
            Verifier verifier = HMACVerifier.newVerifier(getTokenSecret());
            return JWT.getDecoder().decode(token, verifier);
        } catch (Exception exception) {
            return null;
        }
    }

    public static Boolean tokenEnabled() {
        String secret = getTokenSecret();
        return secret != null && !secret.isEmpty();
    }

    private static String getTokenSecret() {
        return ConfigManager.getProperty("files.docservice.secret");
    }

    public static void init(HttpServletRequest request, HttpServletResponse response) {
        return;
    }
}
