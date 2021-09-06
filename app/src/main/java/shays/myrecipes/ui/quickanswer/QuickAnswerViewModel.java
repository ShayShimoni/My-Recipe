package shays.myrecipes.ui.quickanswer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shays.myrecipes.model.QuickAnswerResponse;
import shays.myrecipes.repository.Repository;

public class QuickAnswerViewModel extends AndroidViewModel {
    private MutableLiveData<QuickAnswerResponse> answerLiveData;

    public QuickAnswerViewModel(@NonNull Application application) {
        super(application);
        answerLiveData = new MutableLiveData<>();
    }

    public LiveData<QuickAnswerResponse> getAnswerLiveData() {
        return answerLiveData;
    }


    public void quickAnswer(String query) {
        Repository.getRepo().quickAnswer(query).enqueue(new Callback<QuickAnswerResponse>() {
            @Override
            public void onResponse(Call<QuickAnswerResponse> call, Response<QuickAnswerResponse> response) {
                if (!response.isSuccessful()) {
                    try {
                        System.out.println(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                QuickAnswerResponse body = response.body();
                answerLiveData.postValue(body);
            }

            @Override
            public void onFailure(Call<QuickAnswerResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}