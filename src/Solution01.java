import llm.*;
import slack.*;
import web_client.WebClient;

public class Solution01 {
    public static void main(String[] args) {
        LLM llm = new LLM();

        //String prompt = System.getenv("PROMPT");
        String prompt = "미스포츈";
        String title = "";
        String text = "";
        String imageUrl = "";

        try{
            String geminiResponse = llm.callAPI(LLM.LLMModel.GEMINI_2_0_FLASH, prompt);
            String groqResponse = llm.callAPI(LLM.LLMModel.MIXTRAL_8x7b_32768, prompt);
            groqResponse = groqResponse.split("\"content\":")[1].split("}")[0].replace("\"","").replace(".","").trim();
            System.out.println("groqResponse = " + groqResponse);
            String togetherResponse = llm.callAPI(LLM.LLMModel.FLUX_1_SCHNELL_FREE, groqResponse);

            title = prompt;
            text = geminiResponse.split("\"text\": ")[1].split("}")[0].replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").trim();

            System.out.println("text = " + text);

            imageUrl = togetherResponse
                    .split("\"url\": \"")[1]
                    .split("\",")[0];


        }catch (Exception e){
            e.printStackTrace();
        }



        Slack slack = new Slack(title,text,imageUrl);
        slack.callAPI();

    }
}










