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

import com.adc.da.util.exception.AdcDaBaseException;
import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ServiceConverter {
    private static int convertTimeout = 120000;
    private static final String DOCUMENT_CONVERTER_URL = ConfigManager.getProperty("files.docservice.url.converter");

    private ServiceConverter(){

    }
    public static class ConvertBody {
        public String url;
        public String outputtype;
        public String filetype;
        public String title;
        public String key;
        public Boolean async;
        public String token;
    }

    static {
        try {
            int timeout = Integer.parseInt(ConfigManager.getProperty("files.docservice.timeout"));
            if (timeout > 0) {
                convertTimeout = timeout;
            }
        }catch (Exception e){

        }
    }

    public static String getConvertedUri(String documentUri, String fromExtension, String toExtension, String documentRevisionId, Boolean isAsync) throws Exception
    {
        boolean b = fromExtension == null || fromExtension.isEmpty();
        fromExtension = b ? FileUtility.getFileExtension(documentUri) : fromExtension;
        String title = FileUtility.getFileName(documentUri);
        title = title == null || title.isEmpty() ? UUID.randomUUID().toString() : title;

        b = documentRevisionId == null || documentRevisionId.isEmpty();
        documentRevisionId = b ? documentUri : documentRevisionId;
        documentRevisionId = generateRevisionId(documentRevisionId);

        ConvertBody body = new ConvertBody();
        body.url = documentUri;
        body.outputtype = toExtension.replace(".", "");
        body.filetype = fromExtension.replace(".", "");
        body.title = title;
        body.key = documentRevisionId;
        if (isAsync) {
            body.async = true;
        }
        Gson gson = new Gson();
        String bodyString = gson.toJson(body);

        byte[] bodyByte = bodyString.getBytes(StandardCharsets.UTF_8);

        URL url = new URL(DOCUMENT_CONVERTER_URL);
        java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setFixedLengthStreamingMode(bodyByte.length);
        connection.setRequestProperty("Accept", "application/json");
        connection.setConnectTimeout(convertTimeout);

        if (DocumentManager.tokenEnabled()) {
            Map<String, Object> map = new HashMap<>();
            map.put("payload", body);
            String token = DocumentManager.createToken(map);
            connection.setRequestProperty("Authorization", "Bearer " + token);
        }

        connection.connect();
        try (OutputStream os = connection.getOutputStream()) {
            os.write(bodyByte);
        }

        InputStream stream = connection.getInputStream();

        if (stream == null) {
            throw new IOException("Could not get an answer");
        }
        String jsonString = convertStreamToString(stream);

        connection.disconnect();

        return getResponseUri(jsonString);
    }

    public static String generateRevisionId(String expectedKey) {
        if (expectedKey.length() > 20) {
            expectedKey = Integer.toString(expectedKey.hashCode());
        }
        String key = expectedKey.replace("[^0-9-.a-zA-Z_=]", "_");

        return key.substring(0, Math.min(key.length(), 20));
    }

    private static void processConvertServiceResponceError(int errorCode) throws Exception {
        String errorMessage = "";
        String errorMessageTemplate = "Error occurred in the ConvertService: ";

        switch (errorCode) {
            case -8:
                errorMessage = errorMessageTemplate + "Error document VKey";
                break;
            case -7:
                errorMessage = errorMessageTemplate + "Error document request";
                break;
            case -6:
                errorMessage = errorMessageTemplate + "Error database";
                break;
            case -5:
                errorMessage = errorMessageTemplate + "Error unexpected guid";
                break;
            case -4:
                errorMessage = errorMessageTemplate + "Error download error";
                break;
            case -3:
                errorMessage = errorMessageTemplate + "Error convertation error";
                break;
            case -2:
                errorMessage = errorMessageTemplate + "Error convertation timeout";
                break;
            case -1:
                errorMessage = errorMessageTemplate + "Error convertation unknown";
                break;
            case 0:
                break;
            default:
                errorMessage = "ErrorCode = " + errorCode;
                break;
        }

        throw new AdcDaBaseException(errorMessage);
    }

    private static String getResponseUri(String jsonString) throws Exception {
        JSONObject jsonObj = convertStringToJSON(jsonString);

        Object error = jsonObj.get("error");
        if (error != null) {
            processConvertServiceResponceError((int) error);
        }

        Boolean isEndConvert = (Boolean) jsonObj.get("endConvert");

        Long resultPercent = null;
        String responseUri = null;

        if (isEndConvert) {
            resultPercent = 100l;
            responseUri = (String) jsonObj.get("fileUrl");
        }
        else {
            resultPercent = (Long) jsonObj.get("percent");
            resultPercent = resultPercent >= 100l ? 99l : resultPercent;
        }

        return resultPercent >= 100l ? responseUri : "";
    }

    private static String convertStreamToString(InputStream stream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(stream);
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = bufferedReader.readLine();

        while (line != null) {
            stringBuilder.append(line);
            line = bufferedReader.readLine();
        }

        String result = stringBuilder.toString();

        return result;
    }

    private static JSONObject convertStringToJSON(String jsonString) throws ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(jsonString);
        JSONObject jsonObj = (JSONObject) obj;

        return jsonObj;
    }
}