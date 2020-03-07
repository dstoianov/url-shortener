package se.techinsight.urlshortener.domain;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@JsonPropertyOrder({"id", "long_url", "shorten_key", "count", "creation_date_time", "last_modified_date"})
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "url_shortener")
@EntityListeners(AuditingEntityListener.class)
public class UrlShortener extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String longUrl;

    @Column(nullable = false)
    private String shortenKey;

    private int count = 0;

    // Auto generated fields
    // comes from Auditable file

}
