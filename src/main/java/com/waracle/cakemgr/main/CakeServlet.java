package com.waracle.cakemgr.main;

import com.fasterxml.jackson.core.JsonParser;
import com.waracle.cakemgr.common.CakeCRUDOperation;
import com.waracle.cakemgr.common.ICakeCRUDOperation;
import com.waracle.cakemgr.entity.CakeEntity;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.URL;
import java.util.List;

@WebServlet(
        urlPatterns = "/cakes",
        loadOnStartup = 1,
        asyncSupported = true
)
public class CakeServlet extends HttpServlet {

    private ICakeCRUDOperation cakeCRUDOperation = new CakeCRUDOperation();

    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("downloading cake json");
        try (InputStream inputStream = new URL("https://gist.githubusercontent.com/hart88/198f29ec5114a3ec3460/raw/8dd19a88f9b8d24c23d9960f3300d0c917a4f07c/cake.json").openStream()) {

            JsonParser parser = cakeCRUDOperation.initializeJsonParser(inputStream);
            List<CakeEntity> cakes =cakeCRUDOperation.initializeCakeEntities(parser);
            cakeCRUDOperation.saveCakes(cakes);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<?> list = cakeCRUDOperation.getCakes();
        resp.getWriter().println("[");
        for (Object cake : list) {
            resp.getWriter().println("\t{");
            CakeEntity c= (CakeEntity) cake;
            resp.getWriter().println("\t\t\"title\" : " + c.getTitle() + ", ");
            resp.getWriter().println("\t\t\"desc\" : " + c.getDescription() + ",");
            resp.getWriter().println("\t\t\"image\" : " + c.getImage());
            resp.getWriter().println("\t}");
        }
        resp.getWriter().println("]");
    }

    // Method to handle POST method request.
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String title=request.getParameter("title");
        String description = request.getParameter("desc");
        String image = request.getParameter("image");

        CakeEntity c = new CakeEntity(cakeCRUDOperation.getCakes().size(),title,description,image);
        cakeCRUDOperation.saveCake(c);
        doGet(request, response);
    }
}
