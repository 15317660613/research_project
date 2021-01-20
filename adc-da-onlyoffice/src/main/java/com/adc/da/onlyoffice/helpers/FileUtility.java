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
import com.adc.da.util.utils.StringUtils;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtility {

    private FileUtility(){

    }

    public static FileType getFileType(String fileName) {
        String ext = getFileExtension(fileName);
        if (StringUtils.isNotEmpty(ext)){
            ext = ext.toLowerCase();
        }

        if (extsDocument.contains(ext)) {
            return FileType.TEXT;
        }

        if (extsSpreadsheet.contains(ext)){
            return FileType.SPREADSHEET;
        }

        if (extsPresentation.contains(ext)) {
            return FileType.PRESENTATION;
        }
        return FileType.TEXT;
    }

    public static List<String> extsDocument = Arrays.asList
            (
                    ".doc", ".docx", ".docm",
                    ".dot", ".dotx", ".dotm",
                    ".odt", ".fodt", ".ott", ".rtf", ".txt",
                    ".html", ".htm", ".mht",
                    ".pdf", ".djvu", ".fb2", ".epub", ".xps"
            );

    public static List<String> extsSpreadsheet = Arrays.asList
            (
                    ".xls", ".xlsx", ".xlsm",
                    ".xlt", ".xltx", ".xltm",
                    ".ods", ".fods", ".ots", ".csv"
            );

    public static List<String> extsPresentation = Arrays.asList
            (
                    ".pps", ".ppsx", ".ppsm",
                    ".ppt", ".pptx", ".pptm",
                    ".pot", ".potx", ".potm",
                    ".odp", ".fodp", ".otp"
            );


    public static String getFileName(String url) {
        if (url == null) {
            return "";
        }

        //for external file url
        String tempstorage = ConfigManager.getProperty("files.docservice.url.tempstorage");
        if (!tempstorage.isEmpty() && url.startsWith(tempstorage)) {
            Map<String, String> params = getUrlParams(url);
            return params == null ? "" : params.get("filename");
        }

        String fileName = url.substring(url.lastIndexOf('/') + 1, url.length());
        return fileName;
    }

    public static String getFileNameWithoutExtension(String url) {
        String fileName = getFileName(url);
        if (StringUtils.isEmpty(fileName)) {
            return "";
        }
        String fileNameWithoutExt = fileName.substring(0, fileName.lastIndexOf('.'));
        return fileNameWithoutExt;
    }

    public static String getFileExtension(String url) {
        String fileName = getFileName(url);
        if (StringUtils.isEmpty(fileName)) {
            return "";
        }
        int index = fileName.lastIndexOf( '.') ;
        if (index > -1) {
            String fileExt = fileName.substring(fileName.lastIndexOf('.'));
            return fileExt.toLowerCase();
        }else {
            return null ;
        }
    }

    public static Map<String, String> getUrlParams(String url){
        try {
            String query = new URL(url).getQuery();
            String[] params = query.split("&");
            Map<String, String> map = new HashMap<>();
            for (String param : params) {
                String name = param.split("=")[0];
                String value = param.split("=")[1];
                map.put(name, value);
            }
            return map;
        }catch (Exception ex) {
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(getFileType(null));
        System.out.println(getFileType("dkalj"));
        System.out.println(getFileType("dkalj."));
    }
}
