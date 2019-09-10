package io.github.viscent.mtia.ch6.so;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 该类是一个错误的Servlet类（非线程安全）
 *
 * @author Viscent Huang
 */
public class UnsafeServlet extends HttpServlet {
    private static final long serialVersionUID = -2772996404655982182L;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String strExpiryDate = req.getParameter("expirtyDate");
        try {
            sdf.parse(strExpiryDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 省略其他代码
    }

}
