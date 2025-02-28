package llm;

import web_client.*;

import java.io.IOException;

public class LLM extends WebClient implements ILLM {

    @Override
    public String callAPI(LLMModel model, String body) {
        String path = "";
        HttpMethod method = HttpMethod.GET;
        String[] headers = new String[0];
        String apiKey = "";

        switch (model.platform) {
            case GOOGLE:
                path = "https://generativelanguage.googleapis.com/v1beta/models/%s:%s?key=%s".formatted(model.name, model.action.name, model.platform.apiKey);
                method = HttpMethod.POST;
                headers = new String[]{"Content-Type", "application/json"};
                break;
            case TOGETHER_AI:
                path = "https://api.together.xyz/v1/images/generations";
                method = HttpMethod.POST;
                headers = new String[]{"Content-Type", "application/json", "Authorization", "Bearer %s".formatted(model.platform.apiKey)};
                break;
            case GROQ:
                path = "https://api.groq.com/openai/v1/chat/completions";
                method = HttpMethod.POST;
                headers = new String[]{"Content-Type", "application/json", "Authorization", "Bearer %s".formatted(model.platform.apiKey)};
                break;
        }

        String result = null;
        try {
            result = sendRequest(makeRequest(path, method, body, headers));
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}

interface ILLM {
    enum LLMPlatform {
        GOOGLE(System.getenv("GEMINI_API_KEY")),
        TOGETHER_AI(System.getenv("TOGETHER_AI_API_KEY")),
        GROQ(System.getenv("GROQ_API_KEY"));

        final String apiKey;

        LLMPlatform(String apiKey){
            this.apiKey = apiKey;
        }
    }
    // https://console.groq.com/playground
    // 메인  : llama-3.3-70b-versatile
    // 대안1 : llama-3.1-8b-instant
    // 대안2 : mixtral-8x7b-32768
    //    {
    //        "messages": [],
    //        "model": "llama-3.3-70b-versatile",
    //            "temperature": 1,
    //            "max_completion_tokens": 1024,
    //            "top_p": 1,
    //            "stream": true,
    //            "stop": null
    //    }
    // https://api.together.xyz/playground
    // 메인  : stabilityai/stable-diffusion-xl-base-1.0
    // 대안1 : black-forest-labs/FLUX.1-schnell-Free
    enum LLMModel {
        GEMINI_2_0_FLASH(LLMPlatform.GOOGLE, "gemini-2.0-flash", LLMAction.GENERATE_CONTENT),
        MIXTRAL_8x7b_32768(LLMPlatform.GROQ, "mixtral-8x7b-32768", null),
        STABLE_DIFFUSION_XL_BASE_1_0(LLMPlatform.TOGETHER_AI, "stabilityai/stable-diffusion-xl-base-1.0", null);


        final public LLMPlatform platform;
        final public String name;
        final public LLMAction action;

        LLMModel(LLMPlatform platform, String name, LLMAction action){
            this.platform = platform;
            this.name = name;
            this.action = action;
        }

        enum LLMAction {
            GENERATE_CONTENT("generateContent");

            final String name;

            LLMAction(String name) {
                this.name = name;
            }
        }

    }
    String callAPI(LLMModel model, String body) throws IOException, InterruptedException;
}