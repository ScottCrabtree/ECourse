package com.happybrainscience.ecourse.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author timothyheider
 */
@WebServlet(name = "ContentServlet", urlPatterns = {"/"})
public class ContentServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ContentServlet.class);

    private String getContentTypeByRequest(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        if (requestURI.endsWith(".js")) {
            return "text/javascript";
        } else if (requestURI.endsWith(".css")) {
            return "text/css";
        } else if (requestURI.endsWith(".json")) {
            return "application/json";
        } else if (requestURI.endsWith(".svg")) {
            return "image/svg+xml";
        } else if (requestURI.endsWith(".png")) {
            return "image/png";
        } else if (requestURI.endsWith(".ico")) {
            return "image/x-icon";
        } else if (requestURI.endsWith(".html")) {
            return "text/html";
        } else if (requestURI.endsWith("/")) {
            // instruction.html
            return "text/html";
        } else {
            return null;
        }
    }

    private static final String RESOURCE_NAMESPACE = "/WEB-INF/classes/";
    
    private static final String CONTEXT_PATH = "/ECourse/";

    private InputStream getResourceStream(HttpServletRequest request, String name) {
        // asset path means "development mode" for running locally
        String assetPath = System.getProperty("com.happybrainscience.thehappinesstest.assetpath");
        if(assetPath == null) {
            String sourceName = name;
            if(sourceName.startsWith(CONTEXT_PATH)) {
                sourceName = sourceName.substring(CONTEXT_PATH.length());
            }
            String resourceName = RESOURCE_NAMESPACE + sourceName;
            if(LOGGER.isTraceEnabled()) {
                LOGGER.trace("get resource " + resourceName);
            }
            return request.getServletContext().getResourceAsStream(resourceName);
        } else {
            try {                
                String sourceName = name;
                if(sourceName.startsWith(CONTEXT_PATH)) {
                    sourceName = sourceName.substring(CONTEXT_PATH.length());
                }
                String fileName = assetPath + File.separatorChar + sourceName;
                if(LOGGER.isTraceEnabled()) {
                    LOGGER.trace("get file " + fileName);
                }
                return new FileInputStream(fileName);
            } catch(FileNotFoundException ex) {
                return null;
            }
        }
    }
    
    private InputStream getResourceByRequest(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        if(LOGGER.isTraceEnabled()) {
            LOGGER.trace("get resource by URI:" + requestURI);
        }
        if (requestURI.equals(CONTEXT_PATH)) {
            return getResourceStream(request, "pages/main.html");
        } else if (requestURI.contains("/css")) {
            return getResourceStream(request, requestURI);
        } else if (requestURI.contains("/favicon")) {
            return getResourceStream(request, requestURI);
        } else if (requestURI.contains("/js")) {
            return getResourceStream(request, requestURI);
        } else if (requestURI.contains("/assets")) {
            return getResourceStream(request, requestURI);
        } else { 
            return null;
        }
    }

    private static final int RESOURCE_BUFFER_SIZE_BYTES = 65536;
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {        
        String requestURI = request.getRequestURI();
        if(requestURI.equals("/")) {
            response.sendRedirect("/host");
            LOGGER.debug("redirecting to host...");
            return;
        }
        if(LOGGER.isTraceEnabled()) {
            LOGGER.trace("received request " + request.getRequestURI());
        }
        InputStream in = getResourceByRequest(request);
        String contentType = this.getContentTypeByRequest(request);
        if (contentType == null) {
            LOGGER.debug("unsupported content type " + requestURI);
            response.sendError(415);
            return;
        }        
        response.setContentType(contentType);
        if (in == null) {
            LOGGER.debug("resource not found " + requestURI);
            response.sendError(404);
        } else {
            try (OutputStream out = response.getOutputStream()) {
                int c = 0;
                byte[] data = new byte[RESOURCE_BUFFER_SIZE_BYTES];
                int r;
                do {
                    r = in.read(data);
                    if (r > 0) {
                        out.write(data, 0, r);
                        c += r;
                    }
                } while (r > 0);
                LOGGER.trace(" ... wrote " + c + " bytes");
                out.flush();
            } finally {
                in.close();
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
