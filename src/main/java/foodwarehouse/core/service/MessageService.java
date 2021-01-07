package foodwarehouse.core.service;

import foodwarehouse.core.data.employee.Employee;
import foodwarehouse.core.data.message.Message;
import foodwarehouse.core.data.message.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService implements MessageRepository {
    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Optional<Message> createMessage(Employee sender, Employee receiver, String content) throws SQLException {
        return messageRepository.createMessage(sender, receiver, content);
    }

    @Override
    public Optional<Message> updateMessageContent(int messageId, String content) throws SQLException {
        return messageRepository.updateMessageContent(messageId, content);
    }

    @Override
    public Optional<Message> updateMessageRead(int messageId) throws SQLException {
        return messageRepository.updateMessageRead(messageId);
    }

    @Override
    public boolean deleteMessage(int messageId) throws SQLException {
        return messageRepository.deleteMessage(messageId);
    }

    @Override
    public Optional<Message> findMessageById(int messageId) throws SQLException {
        return messageRepository.findMessageById(messageId);
    }

    @Override
    public List<Message> findAllMessages() throws SQLException {
        return messageRepository.findAllMessages();
    }
}
