package ru.neginskiy.subscounterbot.service;

import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Генерирует предсказание
 */
@Service
public class PredictionService {
    private final Random random = new Random();
    private ReplyMessagesService messagesService;

    public PredictionService(ReplyMessagesService messagesService) {
        this.messagesService = messagesService;
    }

    public String getPrediction() {
        int predictionNumber = random.nextInt(5);
        String replyMessagePropertie = String.format("%s%d", "reply.prediction", predictionNumber);
        return messagesService.getReplyText(replyMessagePropertie);
    }
}
