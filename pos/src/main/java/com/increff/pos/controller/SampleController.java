package com.increff.pos.controller;

import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@Controller
@RequestMapping("/sample")
public class SampleController {

    @ApiOperation(value = "Download sample TSV file to upload brands")
    @RequestMapping(path = "/sample-brand.tsv")
    public void getSampleBrand(HttpServletResponse response) throws IOException {
        response.setContentType("text/tsv");
        response.addHeader("Content-disposition:", "attachment;filename=sample-brand.tsv");
        String fileClasspath = "src/main/resources/sample-brand.tsv";
        File file = new File(fileClasspath);
        InputStream is = Files.newInputStream(file.toPath());
        try {
            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    @ApiOperation(value = "Download sample TSV file to upload products")
    @RequestMapping(path = "/sample-product.tsv")
    public void getSampleProduct(HttpServletResponse response) throws IOException {
        response.setContentType("text/tsv");
        response.addHeader("Content-disposition:", "attachment; filename=sample-product.tsv");
        String fileClasspath = "src/main/resources/sample-product.tsv";
        File file = new File(fileClasspath);
        InputStream is = Files.newInputStream(file.toPath());
        try {
            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    @ApiOperation(value = "Download sample TSV file to upload inventory")
    @RequestMapping(path = "/sample-inventory.tsv")
    public void getSampleInventory(HttpServletResponse response) throws IOException {
        response.setContentType("text/tsv");
        response.addHeader("Content-disposition:", "attachment; filename=sample-inventory.tsv");
        String fileClasspath = "src/main/resources/sample-inventory.tsv";
        File file = new File(fileClasspath);
        InputStream is = Files.newInputStream(file.toPath());
        try {
            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
}
