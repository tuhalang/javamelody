package net.bull.javamelody.internal.common;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author hungpv
 * @date 18/02/2020
 */

public class RestClient {

    private void sendToServer(String url, String post) {
        Client client = ClientBuilder.newClient();
        //send(client, url, post);
    }

    private void senToServer(String url, String post, String username, String password){
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basicBuilder()
                .nonPreemptive()
                .credentials(username, password)
                .build();

        Client client = ClientBuilder.newClient();
        client.register(feature);
        //send(client, url, post);
    }

    private void send(final Client client, final String url, final String post) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                WebTarget webTarget = client.target(url);
                Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                Response response = invocationBuilder.post(Entity.entity(post, MediaType.APPLICATION_JSON_TYPE));
                LOG.info("RESPONSE{status:" + response.getStatus() + ",message:" + response.getEntity());
            }
        });
        thread.start();
        Long endtime = System.currentTimeMillis()+10000;
        while (thread.isAlive()){
            if(System.currentTimeMillis() > endtime){
                thread.stop();
            }
            Thread.sleep(300);
        }
    }


}
