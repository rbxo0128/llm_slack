package llm;

import web_client.*;

public class LLM extends WebClient implements ILLM {


}

interface ILLM {

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
        GEMINI_2_0_FLASH(LLMPlatform.GOOGLE, "gemini-2.0-flash", "generateContent"),
        MIXTRAL_8x7b_32768(LLMPlatform.GROQ, "mixtral-8x7b-32768"),
        STABLE_DIFFUSION_XL_BASE_1_0(LLMPlatform.TOGETHER_AI, "stabilityai/stable-diffusion-xl-base-1.0");


        private  LLMPlatform platform;
        private String name;
        private String[] functions;

        LLMModel(LLMPlatform platform, String name, String ... functions){
            this.platform = platform;
            this.name = name;
            this.functions = functions;
        }

        public LLMPlatform getPlatform(){
            return platform;
        }

        public void setPlatform(LLMPlatform platform){
            this.platform = platform;
        }

        public String getName(){
            return name;
        }

        public String[] getFunctions(){
            return functions;
        }
    }

    enum LLMPlatform {
        GOOGLE,
        TOGETHER_AI,
        GROQ;
    }

    <T> T callAPI(LLMModel model);
}