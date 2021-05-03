package ru.neginskiy.subscounterbot.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private final ReplyMessagesService messagesService;

    public CommentService(ReplyMessagesService messagesService) {
        this.messagesService = messagesService;
    }

    public String getCommentBySubsCount(String subsCount){
        if (subsCount.equals("Неизвестно")){
            return StringUtils.EMPTY;
        }
        int subsLevel = subsCount.length();
        String comment;
        if (subsLevel <= 5) {
            comment = messagesService.getReplyText("reply.level" + subsLevel);
        } else {
            comment = messagesService.getReplyText("reply.level" + 6);
        }
        return comment;
    }
}
