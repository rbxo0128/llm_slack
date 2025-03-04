package llm;

import web_client.*;

import java.io.IOException;

public class LLM extends WebClient implements ILLM {

    @Override
    public String callAPI(LLMModel model, String prompt) {
        String path = "";
        HttpMethod method = HttpMethod.GET;
        String[] headers = new String[0];
        String apiKey = "";
        String body = "";

        switch (model.platform) {
            case GOOGLE:
                path = "https://generativelanguage.googleapis.com/v1beta/models/%s:%s?key=%s".formatted(model.name, model.action.name, model.platform.apiKey);
                method = HttpMethod.POST;
                headers = new String[]{"Content-Type", "application/json"};
                body = """
                        {
                          "contents": [
                            {
                              "role": "user",
                              "parts": [
                                {
                                  "text": "**[명령 및 지시]**
                        
                                           당신은 리그 오브 레전드 (LoL) 게임에 대한 전문가입니다.
                                           사용자가 특정 챔피언의 이름을 제시하면, 당신은 해당 챔피언에 대한 종합적인 정보를 제공해야 합니다.
                                           정보는 한국어로 제공해야 하며, 다음 항목들을 포함해야 합니다.
                        
                                           **[필수 정보 항목]**
                        
                                           1. **챔피언 설명**:
                                               - 챔피언의 배경 이야기 (lore) 또는 컨셉
                                               - 게임 내 역할 (주 포지션 및 플레이 스타일)
                                               - 챔피언의 주요 특징 및 장단점 (강점, 약점)                      
                        
                                           2. **아이템 트리 (추천 빌드)**:
                                               - **시작 아이템**: 게임 시작 시 구매하면 좋은 아이템 추천 (예: 도란의 검, 도란의 반지, 방패, 물약 등)
                                               - **핵심 아이템 (코어 아이템)**: 게임 중반, 후반에 필수적으로 갖춰야 할 핵심 아이템 빌드 (최소 2~3개 아이템, 상황에 따라 유동적인 빌드 가능성 언급)
                                               - **상황별 아이템**: 특정 상황 (상대 조합, 아군 조합, 게임 진행 상황 등) 에 따라 고려할 수 있는 아이템 옵션 (예: 방어 아이템, 마법 저항 아이템, 공격 아이템, 유틸리티 아이템 등)
                                               - **신발**: 추천하는 신발 종류 (예: 마법사의 신발, 아이오니아 장화, 판금 장화, 헤르메스의 발걸음 등)
                        
                        
                                           **[사용자 입력 예시]**
                        
                                           사용자: 아리
                        
                                           **[LLM 응답 예시]**
                        
                                           **## 챔피언 정보: 아리**
                        
                                           **1. 챔피언 설명:**
                        
                                           아리는 뛰어난 기동성과 매혹적인 마법을 사용하는 미드 라인 마법사 챔피언입니다.  ... (아리 배경 이야기, 역할, 특징 등 상세 설명) ...
                        
                                           **2. 아이템 트리:**
                        
                                           * **시작 아이템:** 도란의 반지, 체력 물약 2개
                                           * **핵심 아이템:**
                                               - 루덴의 폭풍
                                               - 그림자 불꽃
                                               - 라바돈의 죽음모자
                                           * **상황별 아이템:** 존야의 모래시계 (상대 암살자), 벤시의 장막 (상대 AP 견제), 모렐로노미콘 (상대 회복 감소)
                                           * **신발:** 마법사의 신발
                                            
                                           해당 챔피언의 이름 : %s 
                                           "
                                }
                              ]
                            }
                          ],
                        }
                        """.formatted(prompt);
                break;
            case TOGETHER_AI:
                path = "https://api.together.xyz/v1/images/generations";
                method = HttpMethod.POST;
                headers = new String[]{"Content-Type", "application/json", "Authorization", "Bearer %s".formatted(model.platform.apiKey)};
                body = """
                {
                    "prompt": "%s",
                    "width": 512,
                    "height": 512,
                    "model": "%s"
                }
                """.formatted(prompt, model.name);
                break;
            case GROQ:
                path = "https://api.groq.com/openai/v1/chat/completions";
                method = HttpMethod.POST;
                headers = new String[]{"Content-Type", "application/json", "Authorization", "Bearer %s".formatted(model.platform.apiKey)};
                body = """
                {
                     "messages": [
                         {
                             "role": "user",
                             "content": "## 리그 오브 레전드 챔피언 영어 프롬프트 생성 프롬프트\\\\n\\\\n**지시:**\\\\n당신은 리그 오브 레전드 챔피언 이미지를 생성하기 위한 영어 프롬프트 생성 전문가입니다. 사용자가 챔피언 이름을 입력하면, 다음 요소들을 고려하여 챔피언에게 가장 적합한 영어 프롬프트를 생성해야 합니다.  다른 설명 없이, 오직 영어 프롬프트 텍스트만 출력하십시오.\\\\n\\\\n**프롬프트 요소 고려 사항:**\\\\n* **챔피언 스타일 (champion_style):**  챔피언의 시각적 특징과 어울리는 스타일을 선택하세요. (예: splash art, illustration, realistic, anime). 챔피언의 분위기와 컨셉을 고려하십시오.\\\\n* **챔피언 포즈 및 액션 (champion_pose_action):** 챔피언의 대표적인 스킬, 전투 스타일, 특징적인 행동, 또는 상징적인 자세를 묘사하는 액션을 선택하세요. (예: using skills, performing ultimate, action pose, idle pose). 챔피언의 역할과 스토리를 반영하십시오.\\\\n* **챔피언 배경 (champion_background):** 챔피언의 출신 지역, 스토리 관련 장소, 또는 리그 오브 레전드 세계관 내의 관련 배경을 선택하세요. (예: Ionia, Piltover, Zaun, Summoner's Rift, 챔피언의 소속 진영 배경). 챔피언의 세계관 내 위치를 보여주세요.\\\\n* **챔피언 디테일 (champion_details):** 챔피언의 외형적 특징 (색, 복장, 무기 등), 분위기, 또는 상징적인 요소들을 묘사하는 키워드를 추가하세요. (예: nine-tailed fox, robotic arm, heavy armor, glowing eyes). 챔피언을 시각적으로 더 돋보이게 하십시오.\\\\n* **일반 디테일 (general_details):** 이미지 품질을 높이고 예술적 스타일을 강화하는 공통 키워드를 포함하세요. (예: highly detailed, digital painting, art by Artgerm, octane render, 8k, vibrant colors).  이미지의 전반적인 품질을 향상시키십시오.\\\\n\\\\n**입력 형식:**\\\\n챔피언 이름 (예: 아리)\\\\n\\\\n**출력 형식:**\\\\n생성된 영어 프롬프트 텍스트 (예: Ahri (League of Legends), splash art, performing Spirit Rush, Ionian forest, nine-tailed fox, highly detailed, digital painting, art by Artgerm)\\\\n\\\\n**예시:**\\\\n사용자 입력: 미스 포츈\\\\nLLM 출력: Misfortune (League of Legends), splash art, action pose, Bilgewater docks, dual pistols, highly detailed, digital painting, art by Artgerm\\\\n\\\\n**사용자 입력 챔피언 : %s**"
                         }
                     ],
                     "model": "%s"
                 }
                """.formatted(prompt, model.name);
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
        STABLE_DIFFUSION_XL_BASE_1_0(LLMPlatform.TOGETHER_AI, "stabilityai/stable-diffusion-xl-base-1.0", null),
        FLUX_1_SCHNELL_FREE(LLMPlatform.TOGETHER_AI, "black-forest-labs/FLUX.1-schnell-Free", null);


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