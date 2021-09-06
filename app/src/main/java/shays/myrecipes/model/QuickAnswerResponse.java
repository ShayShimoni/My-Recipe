package shays.myrecipes.model;

import com.google.gson.annotations.SerializedName;

/*
The object that holds the data from spoonacular api after making an http request,
using the service and GSON Converter
*/
public class QuickAnswerResponse {

    private String answer;

    public QuickAnswerResponse() {
    }

    public String getAnswer() {
        return answer;
    }


    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "\nQuickAnswerResponse{" +
                "answer='" + answer + '\'' +
                '}';
    }
}
