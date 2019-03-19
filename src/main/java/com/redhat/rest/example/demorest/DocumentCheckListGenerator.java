package com.redhat.rest.example.demorest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.myspace.documentgenerator.criteria;
import com.myspace.documentgenerator.documentResult;
import org.apache.camel.Body;
import org.apache.commons.io.IOUtils;
import org.kie.api.KieServices;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public class DocumentCheckListGenerator {

    private static final KieServices KIE_SERVICES = KieServices.Factory.get();

    private static KieContainer kieContainer;

    public String process(@Body InputStream body) {
        String inputJson = null;
        try {
            inputJson =  IOUtils.toString(body, "UTF-8") ;
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("inputJson"+inputJson);

        FormData formData =new Gson().fromJson(inputJson,FormData.class);
        ObjectMapper mapper = new ObjectMapper();

        ReleaseId rId1 = KIE_SERVICES.newReleaseId("com.myspace", "DocumentGenerator", "1.0.0");
        kieContainer = KIE_SERVICES.newKieContainer(rId1);

        criteria documentConfiguration = new criteria();
        documentConfiguration.setProduct(formData.getProduct());
        documentConfiguration.setSubProduct(formData.getSubProduct());
        documentConfiguration.setService(formData.getClientAction());
        documentConfiguration.setSubService(formData.getClientActionSubType());
        documentConfiguration.setAudience(formData.getAudience());
        documentConfiguration.setResidency(formData.getResidency());
        documentConfiguration.setCountry(formData.getCountry());
        documentConfiguration.setRegion(formData.getRegion());
        documentConfiguration.setSubChannel(formData.getSubChannel());



        KieSession kieSession = kieContainer.newKieSession();
        kieSession.insert(documentConfiguration);

        kieSession.fireAllRules();

        Collection<?> documents = kieSession.getObjects(new ClassObjectFilter(documentResult.class));
        documentResult documentResult = new documentResult();
        if(documents.size() > 1) {
            for(Object obj:documents) {
                documentResult input = (documentResult) obj;
                if(input.getDocumentName() != null && input.getDocumentName().equals(documentResult.getDocumentName())) {
                    if (null != input.getCertification()) {
                        documentResult.setCertification(input.getCertification());
                    }
                    if (null != input.getDescription()) {
                        documentResult.setDescription(input.getDescription());
                    }


                    if (null != input.getDocumentCategory()) {
                        documentResult.setDocumentCategory(input.getDocumentCategory());
                    }
                }else if (input.getDocumentName() == null || !input.getDocumentName().equals(documentResult.getDocumentName())) {
                    documentResult = new documentResult();
                    if (null == input.getCertification() ) {
                        documentResult.setCertification(input.getCertification());
                    }
                    if (null != input.getDescription()) {
                        documentResult.setDescription(input.getDescription());
                    }


                    if (null != input.getDocumentCategory()) {
                        documentResult.setDocumentCategory(input.getDocumentCategory());
                    }
                }
            }
        }



        System.out.println(new Gson().toJson(documents));



        return new Gson().toJson(documents);


    }

}
