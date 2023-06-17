package ss.entity.agrolavka;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import ss.entity.martin.DataModel;

/**
 * Telegrm user.
 * @author alex
 */
@Entity
@Table(name = "telegram_user")
public class TelegramUser extends DataModel {
    /** Chat ID. */
    @Column(name = "chat_id")
    private Long chatId;
    /** Username. */
    @Column(name = "username", length = 255)
    private String username;
    /** Bot name. */
    @Column(name = "bot_name", length = 255)
    private String botName;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBotName() {
        return botName;
    }

    public void setBotName(String botName) {
        this.botName = botName;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TelegramUser)) {
            return false;
        }
        TelegramUser other = (TelegramUser) object;
        if ((this.getId() == null && other.getId() != null) 
            || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ss.entity.agrolavka.TelegramUser[ id=" + getId() + " ]";
    }
    
}
