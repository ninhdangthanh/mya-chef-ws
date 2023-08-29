package vn.com.ids.myachef.api.utils;

import java.io.PrintWriter;
import java.time.LocalDate;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@AllArgsConstructor
public class ResponseUtils {

	private static final String EXTENSION_VCF = ".vcf";

	private Integer code;
	private String msg;
	private Object data;

	public static void responseJson(HttpServletResponse response, Object data) {
		PrintWriter out = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json");
			out = response.getWriter();
			out.println(mapper.writeValueAsString(data));
			out.flush();
		} catch (Exception e) {
			log.error("Response invalid json format： {}", e.getLocalizedMessage());
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	public static ResponseUtils response(Integer code, String msg, Object data) {
		return new ResponseUtils(code, msg, data);
	}

	public static ResponseUtils success(Object data) {
		return ResponseUtils.response(200, "Success", data);
	}

	public static ResponseUtils fail(Object data) {
		return ResponseUtils.response(500, "Failed", data);
	}

	public static void setResponseHeader(HttpServletResponse response, String fileName) {
		response.setContentType("text/vcard");
		response.setCharacterEncoding("UTF-8");

		String name = fileName + "_" + LocalDate.now().toString();
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + name + EXTENSION_VCF;
		response.setHeader(headerKey, headerValue);
	}

}
