package pl.iodkovskaya.javaProgrammingInterviewBot.repository;

import org.springframework.stereotype.Repository;
import pl.iodkovskaya.javaProgrammingInterviewBot.dto.Question;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@Repository
public class QuestionRepository {
    private final Map<String, Deque<Question>> userQuestions = new HashMap<>();
    public void addQuestion(String userName, String question) {
        Question dto = new Question();
        dto.setQuestion(question);
        userQuestions.computeIfAbsent(userName, k -> new LinkedList<>()).add(dto);
    }

    public void addAnswer(String userName, String answer) {
        if (userQuestions.containsKey(userName)) {
            Question question = userQuestions.get(userName).peekLast();
            if (question != null) {
                question.setAnswer(answer);
            } else {
                throw new IllegalStateException("There is no question being answered for user " + userName);
            }
        } else {
            throw new IllegalStateException("There is no interview session starter for user " + userName);
        }
    }
    public int getUserQuestions(String userName) {
        return userQuestions.get(userName).size();
    }
    public Deque<Question> finishInterview(String userName) {
        return userQuestions.remove(userName);
    }
}
