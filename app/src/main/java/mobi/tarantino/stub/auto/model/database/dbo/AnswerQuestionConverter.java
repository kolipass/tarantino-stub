package mobi.tarantino.stub.auto.model.database.dbo;

import java.util.ArrayList;
import java.util.List;

import mobi.tarantino.stub.auto.model.additionalData.pojo.Question;
import rx.functions.Func1;

public class AnswerQuestionConverter {

    public static Func1<List<AnswerQuestion>, List<Question>> getQuestionConverter() {
        return new Func1<List<AnswerQuestion>, List<Question>>() {
            @Override
            public List<Question> call(List<AnswerQuestion> answerQuestions) {
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

    public static Func1<List<Question>, List<AnswerQuestion>> getAnswerQuestionConverter() {
        return new Func1<List<Question>, List<AnswerQuestion>>() {
            @Override
            public List<AnswerQuestion> call(List<Question> questions) {
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
