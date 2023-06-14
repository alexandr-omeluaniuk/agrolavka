package ss.martin.telegram.bot.exception;

import ss.martin.base.exception.PlatformException;

/**
 * TelegramBotException.
 * @author alex
 */
public class TelegramBotException extends PlatformException {
    
    public TelegramBotException(String msg) {
        super(msg);
    }
    
}
