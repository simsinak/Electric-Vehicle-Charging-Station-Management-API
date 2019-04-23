package fi.devolon.demo.model.serilizer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import fi.devolon.demo.model.Company;
import fi.devolon.demo.model.Station;

import java.io.IOException;
import java.util.*;

public class SingleCompanySerializer extends JsonSerializer<Company> {
    @Override
    public void serialize(Company temp, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        Map<Object , Object> values = new LinkedHashMap<>();
        Set<Company> subCompanies=temp.getSubCompanies();
        Set<Station> stations=temp.getStations();
        values.put("id",temp.getId());
        values.put("name",temp.getName());
        Company parentCompany = temp.getParentCompany();
        if(parentCompany!=null){
            Map<Object, Object> parentData=new LinkedHashMap<>();
            parentData.put("id",parentCompany.getId());
            parentData.put("name",parentCompany.getName());
            values.put("parentCompany",parentData);
        }
        if(stations!=null && !stations.isEmpty()){
            List<Map<Object , Object>> stationsData=new ArrayList<>();
            stations.forEach(x->{
                Map<Object, Object> stationTemp=new LinkedHashMap<>();
                stationTemp.put("id",x.getId());
                stationTemp.put("name",x.getName());
                stationTemp.put("latitude",x.getLatitude());
                stationTemp.put("longitude",x.getLongitude());
                stationsData.add(stationTemp);
            });
            Object[] stationsArray = new Object[stationsData.size()];
            stationsArray = stationsData.toArray(stationsArray);
            values.put("stations",stationsArray);
        }
        if (subCompanies!=null && !subCompanies.isEmpty()){
            List<Object> subCompaniesData=new ArrayList<>();
            subCompanies.forEach(x->{
                Map<Object, Object> companyTemp = new LinkedHashMap<>();
                companyTemp.put("id",x.getId());
                companyTemp.put("name",x.getName());
                subCompaniesData.add(companyTemp);
            });
            Object[] subCompaniesArray = new Object[subCompaniesData.size()];
            subCompaniesArray = subCompaniesData.toArray(subCompaniesArray);
            values.put("subCompanies",subCompaniesArray);
        }
        jsonGenerator.writeObject(values);
    }
}
