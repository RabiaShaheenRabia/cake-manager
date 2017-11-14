package com.waracle.cakemgr.common;

import com.fasterxml.jackson.core.JsonParser;
import com.waracle.cakemgr.entity.CakeEntity;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Rabia on 14/11/2017.
 */
public interface ICakeCRUDOperation {
    void saveCake(CakeEntity cake);
    void saveCakes(List<CakeEntity> cakes) ;
    List<?> getCakes();
    JsonParser initializeJsonParser(InputStream inputStream) throws Exception ;
    List<CakeEntity> initializeCakeEntities(JsonParser parser) throws IOException ;
}
