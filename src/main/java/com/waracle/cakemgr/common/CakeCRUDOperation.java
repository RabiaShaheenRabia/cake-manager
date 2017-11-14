package com.waracle.cakemgr.common;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.waracle.cakemgr.entity.CakeEntity;
import com.waracle.cakemgr.configuration.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rabia on 14/11/2017.
 */
public class CakeCRUDOperation  implements com.waracle.cakemgr.common.ICakeCRUDOperation {

    @Transactional
    public void saveCake(CakeEntity cake) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.save(cake);
        session.flush();
        tx.commit();
        session.close();
    }


    @Transactional
    public void saveCakes(List<CakeEntity> cakes) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        int counter =0;
        for(CakeEntity cake:cakes) {
            session.save(cake);counter++;
            if ( counter % 20 == 0 ) { //20, same as the JDBC batch size
                //flush a batch of inserts and release memory:
                session.flush();
                session.clear();
            }
        }
        tx.commit();
        session.close();
    }

    public List<?> getCakes() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query qry = session.createQuery("from CakeEntity");
        return qry.getResultList();
    }
    public JsonParser initializeJsonParser(InputStream inputStream) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuffer buffer = new StringBuffer();
        String line = reader.readLine();
        while (line != null) {
            buffer.append(line);
            line = reader.readLine();
        }

        System.out.println("parsing cake json");
        JsonParser parser = new JsonFactory().createParser(buffer.toString());
        if (JsonToken.START_ARRAY != parser.nextToken()) {
            throw new Exception("bad token");
        }
        return parser;
    }

    public List<CakeEntity> initializeCakeEntities(JsonParser parser) throws IOException {
        List<CakeEntity> cakes = new ArrayList<>();

        JsonToken nextToken = parser.nextToken();
        while(nextToken == JsonToken.START_OBJECT) {
            System.out.println("creating cake entity");

            CakeEntity cakeEntity = new CakeEntity();
            System.out.println(parser.nextFieldName());
            cakeEntity.setTitle(parser.nextTextValue());

            System.out.println(parser.nextFieldName());
            cakeEntity.setDescription(parser.nextTextValue());

            System.out.println(parser.nextFieldName());
            cakeEntity.setImage(parser.nextTextValue());

            cakes.add(cakeEntity);
            System.out.println(cakeEntity.getTitle());
            nextToken = parser.nextToken();
            nextToken = parser.nextToken();

        }
        return cakes;
    }


}
