package fi.devolon.demo.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
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

}
