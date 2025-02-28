package slack;

import web_client.WebClient;

public class Slack extends WebClient {
    String path = System.getenv("SLACK_WEBHOOK_URL");
    String[] headers = { "Content-Type", "application/json" };

    private String prompt;
    private String text;
    private String imageUrl;

    public Slack(String prompt, String text, String imageUrl){
        this.prompt = prompt;
        this.text = text;
        this.imageUrl = imageUrl;

        try {

        }catch (Exception e){

        }
    }

    public void callAPI(){
        String payload = """
            {"attachments": [{
                "title": "%s",
                "text": "%s",
                "image_url": "%s"
            }]}
        """.formatted(prompt, text, imageUrl);

        try {
            String result = sendRequest(makeRequest(path, HttpMethod.POST, payload, headers));
        }catch (Exception e){
            logger.warning(e.getMessage());
        }

    }




}
