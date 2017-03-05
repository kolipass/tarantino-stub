package mobi.tarantino.stub.auto.model.database.dbo;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import mobi.tarantino.stub.auto.model.additionalData.pojo.Question;
import rx.functions.Func1;

public class AnswerQuestionConverter {

    @NonNull
    public static Func1<List<AnswerQuestion>, List<Question>> getQuestionConverter() {
        return new Func1<List<AnswerQuestion>, List<Question>>() {
            @NonNull
            @Override
            public List<Question> call(@NonNull List<AnswerQuestion> answerQuestions) {
                List<Question> questionList = new ArrayList<>();
                for (AnswerQuestion answerQuestion : answerQuestions) {
                    Question question = new Question();
                    question.setContent(answerQuestion.getContent());
                    question.setNoUrl(answerQuestion.getNoUrl());
                    question.setYesUrl(answerQuestion.getYesUrl());
                    question.setId(answerQuestion.getId());
                    question.setCreateTime(answerQuestion.getCreateTime());
                    questionList.add(question);
                }
                return questionList;
            }
        };
    }

    @NonNull
    public static Func1<List<Question>, List<AnswerQuestion>> getAnswerQuestionConverter() {
        return new Func1<List<Question>, List<AnswerQuestion>>() {
            @NonNull
            @Override
            public List<AnswerQuestion> call(@NonNull List<Question> questions) {
                List<AnswerQuestion> answerQuestionList = new ArrayList<>();
                for (Question question : questions) {
                    AnswerQuestion answerQuestion = new AnswerQuestion();
                    answerQuestion.setContent(question.getContent());
                    answerQuestion.setYesUrl(question.getYesUrl());
                    answerQuestion.setNoUrl(question.getNoUrl());
                    answerQuestion.setId(question.getId());
                    answerQuestion.setCreateTime(question.getCreateTime());
                    answerQuestionList.add(answerQuestion);
                }
                return answerQuestionList;
            }
        };
    }
}
