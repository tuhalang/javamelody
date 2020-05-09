package net.bull.javamelody.internal.publish;

import net.bull.javamelody.Parameter;
import net.bull.javamelody.internal.common.LOG;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Publish alert when properties are over threshold (send sms)
 *
 * @author hungpv
 * @date 17/02/2020
 */
public class AlertOverThreshold extends MetricsPublisher {

    private static Map<String, Double> threshold;

    static {
        try {
            String pathFile = Parameter.PATH_FILE_THRESHOLD.getValue();
            if (pathFile != null) {
                File propFile = new File(pathFile);
                if (propFile.exists()) {
                    Properties props = new Properties();
                    props.load(new FileInputStream(propFile));

                    threshold = new HashMap<String, Double>();
                    for (Object key : props.keySet()) {
                        threshold.put(key.toString(), Double.parseDouble(props.getProperty(key.toString())));
                    }
                    LOG.info("LOAD THRESHOLD SUCCESSFULLY !");
                }
            }
        } catch (IOException e) {
            LOG.warn("", e);
        }
    }

    private Map<String, Double> properties;

    AlertOverThreshold() {
        this.properties = new HashMap<String, Double>();

    }

    public static AlertOverThreshold getInstance(String contextPath, String hosts) {
        final Boolean isAlert = Parameter.ALERT_THRESHOLD.getValueAsBoolean();
        if (isAlert) {
            return new AlertOverThreshold();
        }
        return null;
    }

    @Override
    public void addValue(String metric, double value) throws IOException {
        if (threshold.containsKey(metric) && value > threshold.get(metric))
            this.properties.put(metric, value);
    }


    @Override
    public void send() throws IOException {
        if (this.properties.size() > 0) {
            StringBuilder post = new StringBuilder("{\"Alert\":{\"message\":\"");
            final String message = Parameter.MESSAGE_ALERT.getValue();
            final String url = Parameter.SMS_API.getValue();
            if (url == null)
                throw new IOException("Param url-alert not found !");
            if (message != null) {
                post.append(message+"\"");
            } else {
                post.append("Alert Over Threshold\"");
            }
            post.append(",\"properties\":[");
            for (String key : this.properties.keySet()) {
                post.append("{\"" + key + "\":" + this.properties.get(key) + "},");
            }
            post.append("{\"size\":" + this.properties.size() + "}]}}");
            LOG.info(post.toString());
            //sendToServer(url, post.toString());

        }

    }

//    private void sendToServer(String url, String post) {
//        Client client = Client.create();
//        WebResource webResource = client.resource(url);
//        ClientResponse response = webResource.type("application/json").post(ClientResponse.class, post);
//        LOG.info("RESPONSE{status:" + response.getStatus() + ",message:" + response.getEntity(String.class));
//    }

    @Override
    public void stop() {

    }
}
