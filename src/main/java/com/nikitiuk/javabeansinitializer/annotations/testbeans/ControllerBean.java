package com.nikitiuk.javabeansinitializer.annotations.testbeans;

import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.beans.AutoWire;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.beans.Controller;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.beans.controller.Consumes;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.beans.controller.FormDataParam;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.beans.controller.Path;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.http.GET;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.http.POST;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.http.PUT;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.security.Context;
import com.nikitiuk.javabeansinitializer.server.request.types.RequestContext;
import com.nikitiuk.javabeansinitializer.server.response.Response;
import com.nikitiuk.javabeansinitializer.server.response.ResponseBuilder;
import com.nikitiuk.javabeansinitializer.server.response.ResponseCode;
import com.nikitiuk.javabeansinitializer.server.utils.MimeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Controller
@Path("/docs")
public class ControllerBean {

    private static final Logger logger = LoggerFactory.getLogger(ControllerBean.class);

    @AutoWire
    private TestBeanDependent someServiceBean;

    @POST
    @Consumes("application/json")
    @Path("/age")
    public int getAgeOfServiceBean(AgeBean ageBean) {
        int age = someServiceBean.getAutoWiredBean().getSomeAge();
        logger.info(Integer.toString(age));
        logger.info("And this is age parsed from json " + ageBean.getAge());
        return age;
    }

    @PUT
    @Consumes("multipart/form-data")
    @Path("/somePathWithString")
    public Response doParseStringAndDouble(@FormDataParam("designatedName") String name,
                                           @FormDataParam("doubleValue") double value,
                                           @Context RequestContext requestContext) throws IOException {
        logger.info("That's string from formDataParam : " + name);
        logger.info("That's double from formDataParam : " + value);
        logger.info("And that's context injected as param : " + requestContext.getSecurityData());
        ResponseBuilder responseBuilder = new ResponseBuilder(ResponseCode.HTTP_200_OK, MimeType.TEXT_HTML,
                "<b>HTTPServer First Attempt.</b>".getBytes().length);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new ByteArrayInputStream("<b>HTTPServer First Attempt.</b>".getBytes()));
        responseBuilder.addBody(bufferedInputStream);
        return responseBuilder.buildResponse();
    }

    @GET
    @Path("/getFile")
    public Response getFile() throws IOException {
        java.nio.file.Path path = Paths.get("/home/npalexey/Downloads/MicroService.docx");
        byte[] data = Files.readAllBytes(path);
        ResponseBuilder responseBuilder = new ResponseBuilder(ResponseCode.HTTP_200_OK,"application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                data.length, "MicroService.docx");
        responseBuilder.addBody(new BufferedInputStream(new ByteArrayInputStream(data)));
        return responseBuilder.buildResponse();
    }

    @POST
    @Consumes("multipart/form-data")
    @Path("/uploadFile")
    public void uploadFile(@FormDataParam("file") InputStream fileStream,
                           @FormDataParam("designatedName") String filename) throws IOException {
        int read;
        byte[] bytes = new byte[1024];
        OutputStream out = new FileOutputStream(new File("/home/npalexey/workenv/Java Beans Initializer Notes/" + filename));
        while ((read = fileStream.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }
        out.flush();
        out.close();
    }

    public double callCountOfServiceBean() {
        return someServiceBean.count();
    }
}