package com.diyconnect.integration.bandcamp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements; // <-- IMPORT CORRECTO
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class BandcampService {

    public String getEmbedUrl(String bandcampUrl) throws IOException {
        // Descarga la página
        Document doc = Jsoup.connect(bandcampUrl)
                .userAgent("Mozilla/5.0")
                .get();

        // URL canónica del álbum o track
        Element metaUrl = doc.selectFirst("meta[property=og:url]");
        if (metaUrl == null) throw new IOException("No se encontró URL del álbum/track");
        String canonicalUrl = metaUrl.attr("content");

        // ID del álbum o track desde el og:video
        Element metaVideo = doc.selectFirst("meta[property=og:video]");
        if (metaVideo == null) throw new IOException("No se encontró meta og:video");
        String content = metaVideo.attr("content");

        String id;
        if (content.contains("album=")) {
            id = content.split("album=")[1].split("/")[0];
        } else if (content.contains("track=")) {
            id = content.split("track=")[1].split("/")[0];
        } else {
            throw new IOException("No se pudo extraer ID de Bandcamp");
        }

        // Construir la URL del embed con parámetros fijos
        String embedUrl = "https://bandcamp.com/EmbeddedPlayer/" +
                (content.contains("album=") ? "album=" + id : "track=" + id) +
                "/size=large/bgcol=ffffff/linkcol=0687f5/artwork=small/transparent=true/";

        // Devolver iframe con tu configuración fija
        return "<iframe style=\"border: 0; width: 100%; height: 100%; border-radius: 15px\" src=\"" + embedUrl + "\" seamless>" +
                "<a href=\"" + canonicalUrl + "\">Escucha en Bandcamp</a></iframe>";
    }
}

