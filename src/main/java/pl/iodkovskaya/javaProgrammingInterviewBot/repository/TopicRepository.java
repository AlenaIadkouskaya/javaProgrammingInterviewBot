package pl.iodkovskaya.javaProgrammingInterviewBot.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TopicRepository {
    private final List<String> topics = List.of(
            // Object-Oriented Programming (OOP)
            "What is object-oriented programming and its main principles?",
            "What is the difference between inheritance and composition?",
            "How is polymorphism implemented in Java and what are its advantages?",
            "When should an abstract class be used and when should an interface be used?",
            "Which methods of the Object class are key for implementing OOP?",
            "What is method overriding and how does it differ from method overloading?",
            "How can you implement the Singleton pattern and what are its potential drawbacks?",
            "How does the principle of encapsulation help protect data within a class?",
            "How does abstraction allow hiding the complexity of implementation?",
            "Which SOLID principles do you know and how are they applied in Java?",

            // The Object Class and Its Methods
            "What is the contract between the equals() and hashCode() methods?",
            "Why is it important to override the toString() method in the Object class?",
            "How does the clone() method work and what issues can arise when using it?",
            "What does the finalize() method do and why is its use considered undesirable?",
            "What is the difference between comparing objects using the == operator and the equals() method?",
            "Why do all classes in Java inherit methods from the Object class?",
            "In which cases does it make sense to override the clone() method?",
            "How can you check if an object belongs to a specific class using the Object class capabilities?",
            "What specific considerations should be taken into account when overriding Object methods in the context of thread safety?",
            "How can Object methods be useful for debugging and logging?",

            // Exceptions
            "What is the difference between checked and unchecked exceptions?",
            "How should you properly use the try-catch-finally construct?",
            "What happens if an exception is not caught in the try block?",
            "When should you use try-with-resources and how does it work?",
            "How can you create your own exception by extending the Exception or RuntimeException class?",
            "What is the difference between the throw and throws keywords?",
            "How does exception chaining work and why is it useful?",
            "In what order do the try, catch, and finally blocks execute?",
            "What principles should be followed when handling exceptions in an application?",
            "How do exceptions contribute to the reliability of a Java application?",

            // Collections, especially HashMap
            "How does HashMap work and how does it handle hash collisions?",
            "What is a hash function and how does it affect the performance of HashMap?",
            "What role does the load factor play in HashMap and when does rehashing occur?",
            "What happens when an element with an existing key is added to a HashMap?",
            "What are the differences between HashMap and Hashtable in terms of functionality and thread safety?",
            "What ways of iterating over elements of HashMap do you know and what are their advantages?",
            "Which HashMap methods allow efficient searching of elements by key?",
            "What are fail-fast iterators and how do they work in Java collections?",
            "What issues can arise when using HashMap in a multithreaded environment?",
            "How can you implement thread-safe usage of collections similar to HashMap?",

            // Multithreading
            "How do you create a new thread in Java by implementing the Runnable interface?",
            "What is the difference between calling the start() method on a thread and calling run() directly?",
            "How do you synchronize access to shared resources using the synchronized keyword?",
            "What is a lock and how does it help prevent race conditions?",
            "How do the wait(), notify(), and notifyAll() methods work in multithreaded code?",
            "What is the volatile keyword used for and when should it be applied?",
            "What problems can arise when multiple threads simultaneously access collections?",
            "What is the difference between the sleep() method and the yield() method in multithreading?",
            "What is a deadlock and how can it be prevented in Java?",
            "How can you use the try-catch-finally construct to manage resources in a multithreaded environment?"
    );

    public String getRandomTopic() {
        return topics.get((int) (Math.random() * topics.size()));
    }
}
