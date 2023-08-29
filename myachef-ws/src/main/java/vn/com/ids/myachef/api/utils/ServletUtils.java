package vn.com.ids.myachef.api.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;

@Slf4j
public class ServletUtils {

    private static final String DEFAULT_EXTENSION = ".xlsx";

    private ServletUtils() {
        throw new IllegalStateException("ServletUtils class");
    }

    public static void setResponseHeader(HttpServletResponse response, String fileName) {
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("UTF-8");

        String name = fileName + "_" + LocalDate.now().toString();
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + name + DEFAULT_EXTENSION;
        response.setHeader(headerKey, headerValue);
    }

    public static void writeOutputStream(HttpServletResponse response, XSSFWorkbook workbook) throws IOException {
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.close();
    }

    public static void writeOutputStreamByWorkbook(HttpServletResponse response, Workbook workbook) throws IOException {
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.close();
    }

    public static void writeDataFromPath(HttpServletResponse response, String filePath) {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new URL(filePath).openStream())) {
            writeDataFromResource(response, bufferedInputStream);
        } catch (Exception ex) {
            throw new ResourceNotFoundException("Customer edited file not found");
        }

    }

    public static void writeDataFromResource(HttpServletResponse response, InputStream inputStream) {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream); //
                ServletOutputStream outputStream = response.getOutputStream();) {

            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = bufferedInputStream.read(dataBuffer, 0, 1024)) != -1) {
                outputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    public static InputStream getFileFromResource(String fileName) {
        ClassLoader classLoader = ServletUtils.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new IllegalArgumentException(String.format("File '%s' is not found! ", fileName));
        } else {
            return inputStream;
        }
    }
}
