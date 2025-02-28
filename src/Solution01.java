import llm.*;
import slack.*;
import web_client.WebClient;

public class Solution01 {
    public static void main(String[] args) {
        LLM llm = new LLM();

        String prompt = "주식이 뭐야?";
        String result;
        String title = "";
        String text = "";
        String imageUrl = "";

        try{
            result = llm.callAPI(LLM.LLMModel.GEMINI_2_0_FLASH, """
                {
                                  "contents": [
                                    {
                                      "role": "user",
                                      "parts": [
                                        {
                                          "text": "%s"
                                        }
                                      ]
                                    }
                                  ],
                                }
                """.formatted(prompt));

            String groqBody = """
                {
                    "messages": [
                        {
                            "role": "user",
                            "content": "%s"
                        }
                    ],
                    "model": "%s"
                }
                """.formatted(prompt, LLM.LLMModel.MIXTRAL_8x7b_32768.name);

            String result1 = llm.callAPI(LLM.LLMModel.MIXTRAL_8x7b_32768, groqBody);


            String togetherBody = """
                {
                    "prompt": "%s",
                    "width": 1024,
                    "height": 1024,
                    "model": "%s"
                }
                """.formatted(prompt, LLM.LLMModel.STABLE_DIFFUSION_XL_BASE_1_0.name);
            String result2 = llm.callAPI(LLM.LLMModel.STABLE_DIFFUSION_XL_BASE_1_0, togetherBody);


            title = result.split("\"text\": ")[1].split("}")[0].replace("\\n", " ").replace("\"", "").trim();
            text = result1.split("\"content\":")[1].split("}")[0].replace("\\n", " ").replace("\"", "").trim();

            imageUrl = result2
                    .split("\"url\": \"")[1]
                    .split("\",")[0];
            

        }catch (Exception e){
            e.printStackTrace();
        }



        Slack slack = new Slack(title,text,imageUrl);
        slack.callAPI();

    }
}










