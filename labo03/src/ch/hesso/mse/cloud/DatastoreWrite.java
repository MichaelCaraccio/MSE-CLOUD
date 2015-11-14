package ch.hesso.mse.cloud;

import com.google.appengine.api.datastore.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;

@SuppressWarnings("serial")
public class DatastoreWrite extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/plain");
        PrintWriter pw = resp.getWriter();

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        // Insert new entry in DataStore
        NewEntry(req, resp, datastore);

        // Display stored objects in DataStore
        DisplayAllEntries(datastore, pw);
    }

    /**
     * Display stored objects in Datastore
     *
     * @param datastore DatastoreService
     * @param pw        PrintWriter
     */
    public void DisplayAllEntries(DatastoreService datastore, PrintWriter pw) {

        // Use PreparedQuery interface to retrieve results
        PreparedQuery pq = datastore.prepare(new Query());

        for (Entity result : pq.asIterable()) {
            Map<String, Object> properties = result.getProperties();

            //loop a Map
            for (Map.Entry<String, Object> entry : properties.entrySet()) {
                pw.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
            }
        }
    }

    /**
     * Insert new entry in DataStore
     *
     * @param req       HttpServletRequest
     * @param resp      HttpServletResponse
     * @param datastore DatastoreService
     * @throws IOException
     */
    public void NewEntry(HttpServletRequest req, HttpServletResponse resp, DatastoreService datastore) throws IOException {

        String pkey;
        String pval;

        Enumeration parameters = req.getParameterNames();

        // Get every parameters and store them in datastore
        Entity object = new Entity(req.getParameter("_kind"));
        while (parameters.hasMoreElements()) {
            pkey = (String) parameters.nextElement();
            pval = req.getParameter(pkey);

            object.setProperty(pkey, pval);
        }
        datastore.put(object);
    }
}