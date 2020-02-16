package se.techinsight.urlshortener.domain;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@JsonPropertyOrder({"id", "long_url", "shorten_key", "count", "creation_date_time", "last_modified_date"})
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
