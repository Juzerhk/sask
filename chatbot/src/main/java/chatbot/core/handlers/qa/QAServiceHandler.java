package chatbot.core.handlers.qa;

import chatbot.core.handlers.Handler;
import chatbot.core.handlers.qa.dto.Answer;
import chatbot.io.incomingrequest.IncomingRequest;
import chatbot.io.response.EntryInformation;
import chatbot.io.response.Response;
import chatbot.io.response.ResponseList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import java.io.IOException;
import java.net.URI;

/**
 * @author sajjad
 */
public class QAServiceHandler extends Handler {

    private static final Logger log = Logger.getLogger(QAServiceHandler.class);

    private static final String URL = "http://localhost:8082/qa/{var}"; // TODO: 11/07/2018 read from application.yml later

    private final RestTemplate restTemplate;

    public QAServiceHandler() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        restTemplate = builder.build();
    }

    public ResponseList search(IncomingRequest request) throws JsonProcessingException, IOException {
        EntryInformation entry = new EntryInformation();
        ResponseList responseList = new ResponseList();
        StringBuilder stringBuilder = new StringBuilder();
        Gson gson = new GsonBuilder().create();
        String answer = askQAService(request);
        Answer answerDto = gson.fromJson(answer, Answer.class);
        answerDto.getQuestions().forEach(question -> {
            question.getAnswers().forEach(answer_ -> {
                answer_.getResults().getBindings().forEach(binding -> {
                    stringBuilder.append(binding.getResource().getValue());
                    stringBuilder.append(" ");
                    entry.setDisplayText(stringBuilder.toString());
                    entry.setButtonType(EntryInformation.Type.URL);
                    Response response = new Response();
                    response.addEntry(entry);
                    responseList.addMessage(response);
                    responseList.setMessageType(ResponseList.MessageType.TEXT_WITH_URL);
                });
            });
        });
        return responseList;
    }

    private String askQAService(IncomingRequest request) {
        log.info("Requesting an answer from QA service...");
        String query = request.getRequestContent().get(0).getText();
        URI uri = new UriTemplate(URL).expand(query);
        return exchange(uri, String.class);
    }


    // TODO: 11/07/2018 This implementation should be moved to some common package,
    // TODO: since this will be used by every component to make rest call

    /**
     * Generic method to return response from a RestCall endpoint
     *
     * @param uri
     * @param response
     * @param <T>
     * @return
     */
    private <T> T exchange(URI uri, Class<T> response) {
        RequestEntity<?> requestEntity = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON).build();
        ResponseEntity<T> responseEntity = this.restTemplate.exchange(requestEntity, response);
        return responseEntity.getBody();
    }
}
