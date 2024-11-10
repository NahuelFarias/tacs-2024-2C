package tacs.telegram;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

@Configuration
public class BotConfig {

    @Value("${telegram.bot.webhook-path}")
    private String webhookPath;

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(webhookPath).build();
    }

    @Bean
    public TelegramBotsApi telegramBotsApi(Bot bot, SetWebhook setWebhook) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(bot, setWebhook);
        return botsApi;
    }
}