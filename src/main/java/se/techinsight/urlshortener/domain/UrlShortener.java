package se.techinsight.urlshortener.domain;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@JsonPropertyOrder({"id", "long_url", "shorten_key", "count", "creation_date_time", "last_modified_date"})
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "url_shortener")
@EntityListeners(AuditingEntityListener.class)
public class UrlShortener extends Auditable {

    //    @GeneratedValue(strategy = GenerationType.AUTO)
    //    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView({Views.Internal.class})
    private Long id;

    @Column(nullable = false)
    @JsonView({Views.Public.class})
    private String longUrl;

    @Column(nullable = false)
    @JsonView({Views.Public.class})
    private String shortenKey;

    private int count = 0;

    // Auto generated fields
    // comes from Auditable file
}
