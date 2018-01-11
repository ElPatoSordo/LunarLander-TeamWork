/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Configuration;
import model.ConfigurationJpaController;
import model.Users;
import model.UsersJpaController;

/**
 *
 * @author juanm
 */
public class CreateConfiguration extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CreateConfiguration</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CreateConfiguration at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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

        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("LunarLanderTeamWorkPU");
            UsersJpaController uc = new UsersJpaController(emf);
            ConfigurationJpaController cc = new ConfigurationJpaController(emf);

            String cookieName = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("username")) {
                        cookieName = cookie.getValue();
                    }
                }
            }

            Users a = uc.findUsersByUsername(cookieName);

            if (a == null) {
                response.setContentType("application/json");
                PrintWriter pw = response.getWriter();
                pw.println("{\"mess\":\" El usuario no existe\"}");

            } else if (!cc.existByConfigName(request.getParameter("configName"), a)) {
                Configuration conf = new Configuration();
                conf.setConfigName(request.getParameter("configName"));
                conf.setDifficulty(Integer.parseInt(request.getParameter("nivelDeDificultad")));
                conf.setSpaceship(Integer.parseInt(request.getParameter("modeloNave")));
                conf.setMoon(Integer.parseInt(request.getParameter("modeloLuna")));
                conf.setUserId(a);
                cc.create(conf);
                response.setContentType("application/json");
                PrintWriter pw = response.getWriter();
                pw.println("{\"mess\":\" Creada\"}");
                
            } else {

                response.setContentType("application/json");
                PrintWriter pw = response.getWriter();
                pw.println("{\"mess\":\" Ya existe\"}");
            }

        } catch (Exception e) {

            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println("{\"mess\":\" No se ha podido crear\"}");

        }

//            if ( uc.existUserByUsername(cookieName) && (!cc.existByConfigName(request.getParameter("configName"), aux))) {
//
//                Configuration conf = new Configuration();
//
//                conf.setConfigName(request.getParameter("configName"));
//                conf.setMoon(Integer.parseInt(request.getParameter("modeloLuna")));
//                conf.setSpaceship(Integer.parseInt(request.getParameter("modeloNave")));
//                conf.setDifficulty(Integer.parseInt(request.getParameter("nivelDeDificultad")));
//                cc.create(conf);
//                response.setContentType("application/json");
//                PrintWriter pw = response.getWriter();
//                pw.println("{\"mess\":\" Creada\"}");
//
//            } else {
//                response.setContentType("application/json");
//                PrintWriter pw = response.getWriter();
//                pw.println("{\"mess\":\" No Creada\"}");
//            }
//
//        } catch (Exception e) {
//            response.setContentType("application/json");
//            PrintWriter pw = response.getWriter();
//            pw.println("{\"mess\":\" No se ha podido crear\"}");
//        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
