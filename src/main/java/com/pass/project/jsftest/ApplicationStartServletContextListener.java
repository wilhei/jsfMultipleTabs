package com.pass.project.jsftest;

import java.awt.Desktop;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ApplicationStartServletContextListener implements ServletContextListener {
    
    
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {

        
        startWebBrowser(sce);
//        if ("true".equals(System.getProperty("startWebBrowser"))) {
//        }
    }

    private void startWebBrowser(ServletContextEvent sce) {
        try {
            Desktop.getDesktop().browse(new URI("http://" + getIpAndPort() +  sce.getServletContext().getContextPath()));
        } catch (MalformedObjectNameException | IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private String getIpAndPort() throws UnknownHostException, MalformedObjectNameException {
        MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();

        Set<ObjectName> objectNames = beanServer.queryNames(new ObjectName("*:type=Connector,*"), Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));

        String host = InetAddress.getLocalHost().getHostAddress();
        String port = objectNames.iterator().next().getKeyProperty("port");

        return host + ":" + port;
    }


    @Override
    public void contextDestroyed(ServletContextEvent sce) {}

}
