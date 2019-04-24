package fi.devolon.demo.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

public class ControllerUtility {
     static <T>String linkMaker(UriComponentsBuilder uriComponentsBuilder, int limit , Page<T> pageResult, String path){
        final StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(uriBuilder(uriComponentsBuilder.path(path).replaceQueryParam("page","0").replaceQueryParam("limit",limit).build(),"first"));
        stringBuilder.append(",");
        stringBuilder.append(uriBuilder(uriComponentsBuilder.replaceQueryParam("page",pageResult.getNumber()).replaceQueryParam("limit",limit).build(),"self"));
        stringBuilder.append(",");
        stringBuilder.append(uriBuilder(uriComponentsBuilder.replaceQueryParam("page",pageResult.getTotalPages()-1).replaceQueryParam("limit",limit).build(),"last"));
        if(pageResult.getNumber()<pageResult.getTotalPages()-1){
            stringBuilder.append(",");
            stringBuilder.append(uriBuilder(uriComponentsBuilder.replaceQueryParam("page",pageResult.getNumber()+1).replaceQueryParam("limit",limit).build(),"next"));
        }
        if(pageResult.getNumber()>0){
            stringBuilder.append(",");
            stringBuilder.append(uriBuilder(uriComponentsBuilder.replaceQueryParam("page",pageResult.getNumber()-1).replaceQueryParam("limit",limit).build(),"prev"));
        }

        return stringBuilder.toString();

    }
    private static String uriBuilder(UriComponents uri, String rel){
        return  "<"+uri.toUriString()+">; rel=\""+rel+"\"";
    }
    public static List<String> findLinks(HttpHeaders header, String path){
         List<String> foundedLinks= Arrays.asList(null , null);
         List<String> values=header.get("Link");
         if(values!=null){
             String[] parts=values.get(0).split(",");
             for(String value:parts){
                 String[] tokens=value.split(";");
                 if(tokens[1].trim().equals("rel=\"prev\"")){
                     foundedLinks.set(0,path+tokens[0].substring(tokens[0].indexOf('?'),tokens[0].length()-1));
                 }else if(tokens[1].trim().equals("rel=\"next\"")){
                     foundedLinks.set(1,path+tokens[0].substring(tokens[0].indexOf("?"),tokens[0].length()-1));
                 }
             }
         }
         return foundedLinks;

    }
}
