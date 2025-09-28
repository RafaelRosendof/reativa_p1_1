import java.net.http.*;


/*
curl -X POST http://localhost:8080/api/chat \
                                        took  7s  base at  09:08:01
 -H "Content-Type: application/json" \
 -H "Authorization: Bearer sk-proj-" \
 -d '{
      "message": "Caso eu te entregue cerca de 3 notícias sobre uma determinada ação e o 
      preço de fechamento e abertura dos últimos 3 dias voce consegue me dar uma análise sobre as notícias e preços?",
      "model": "gpt-5-nano"
    }'

 */

public class RequestExemple {
    

    private String question;
    private String apiKey;
    private String model;
    private String url;


    public RequestExemple(String question, String apiKey, String model, String url) {
        this.question = question;
        this.apiKey = apiKey;
        this.model = model;
        this.url = url;
    }

    public static void main(String[] args) {
        //String question = "What is the capital of France?";
        //String apiKey = "your_api_key_here";
        String model = "gpt-4o-mini";
        String url = "https://api.openai.com/v1/chat/completions";

        if (args.length < 2) {
            System.out.println("Usage: java RequestExemple <question> <apiKey>");
            return;
        }
        String question = args[0];
        String apiKey = args[1];
        
        // java  RequestExemple "What is the capital of France?" "your_api_key_here" "gpt-4-mini" "

        RequestExemple requestExample = new RequestExemple(question, apiKey, model, url);

        try{

            HttpClient client = HttpClient.newHttpClient();
            String requestBody = String.format(
                "{\"model\": \"%s\", \"messages\": [{\"role\": \"user\", \"content\": \"%s\"}]}",
                model, question
            );

            String bearerToken = "Bearer " + apiKey;
            HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", bearerToken)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response status code: " + response.statusCode());
            System.out.println("Response body: " + response.body());

        }catch(Exception e){
            e.printStackTrace();
        }

    }


}