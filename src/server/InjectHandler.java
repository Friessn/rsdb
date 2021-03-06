package server;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import util.Web;

public class InjectHandler extends AbstractHandler {
	static final Logger log = LogManager.getLogger();

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.setHeader("X-Robots-Tag", "noindex, nofollow");
		
		response.setHeader("Referrer-Policy", "no-referrer");
		response.setHeader("X-Content-Type-Options", "nosniff");
		if("127.0.0.1".equals(baseRequest.getRemoteAddr())) {
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Headers", "content-type");
			if(baseRequest.getMethod().equals("OPTIONS")) {
				baseRequest.setHandled(true);
			}
		} else {
			//response.setHeader("X-Frame-Options", "deny"); // deny all iframes
			response.setHeader("X-Frame-Options", "sameorigin"); // allow iframes on same site (needed for webfiles iframe)
		}
		/*Enumeration<String> hs = request.getHeaderNames();
		while(hs.hasMoreElements()) {
			String h = hs.nextElement();
			log.info("header   " + h + ": " + request.getHeader(h));
		}*/
		log.info(RSDBServer.MARKER_REQ, Web.getRequestLogString("REQ", baseRequest.getMethod() + "   " + baseRequest.getRequestURL().toString(), baseRequest));
	}
}
