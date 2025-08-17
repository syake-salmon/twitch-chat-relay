package com.syakeapps.tcr.service;

import java.io.File;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.syakeapps.tcr.application.Constraints;
import com.syakeapps.tcr.util.UriBuilder;

public class VoiceVoxService {
    private static final Logger LOG = LoggerFactory.getLogger(VoiceVoxService.class);

    private ConfigService conf = new ConfigService();

    public void tts(String text) {
        try {
            LOG.debug("TTSing : {}", text);

            URI uri = UriBuilder.create(conf.get(Constraints.VOICEVOX_API_ENDPOINT))
                    .path("audio_query")
                    .queryParam("text", URLEncoder.encode(text, "UTF-8"))
                    .queryParam("speaker", conf.get(Constraints.VOICEVOX_SPEAKER_ID))
                    .queryParam("enable_katakana_english", "true")
                    .build();

            HttpRequest request = HttpRequest.newBuilder(uri)
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());

            URI uri2 = UriBuilder.create(conf.get(Constraints.VOICEVOX_API_ENDPOINT))
                    .path("synthesis")
                    .queryParam("speaker", conf.get(Constraints.VOICEVOX_SPEAKER_ID))
                    .queryParam("enable_katakana_english", "true")
                    .build();

            HttpRequest request2 = HttpRequest.newBuilder(uri2)
                    .header("Accept", "audio/wav")
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(response.body()))
                    .build();

            HttpResponse<byte[]> response2 = HttpClient.newHttpClient().send(request2,
                    HttpResponse.BodyHandlers.ofByteArray());

            if (response2.statusCode() == 200) {
                Path wavPath = Path.of(conf.get("TMP"), String.valueOf(System.nanoTime()) + ".wav");
                Files.write(wavPath, response2.body(), StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING);

                LOG.debug("Saved WAV file : {}", wavPath.toAbsolutePath());

                playWav(wavPath.toFile());
            } else {
                throw new RuntimeException(new String(response2.body(), "UTF-8"));
            }

        } catch (Exception e) {
            LOG.error("Failed to TTS.", e);
        }
    }

    private void playWav(File wavFile) {
        try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(wavFile)) {
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

            Thread.sleep(clip.getMicrosecondLength() / 1000); // 再生が終わるまで待機
        } catch (Exception e) {
            throw new RuntimeException("Failed to play audio.", e);
        }
    }
}
